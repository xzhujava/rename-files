import java.io.File;
import java.math.BigInteger;

/**
 * 一个三层文件夹按照一定规则修改该文件夹及文件的名称
 * 第一层(文件夹)：以L开头后面是十进制数字要求把L00-L09文件夹中的第二个字符去掉即去掉中间的0重命名文件夹，如L10则不需重命名
 * 第二层(文件夹)：以R开头后面是十六进制的数字，要求把R后十六进制转换为十进制数字重命名文件夹
 * 第三层(文件-图片)：以C开头后面是十六进制数字，要求把C后十六进制转换为十进制数字重命名文件
 * @author zhangshuan
 * @version 1.0
 * @date 2021/4/29 10:03 上午
 */
public class Test {
    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        String property = System.getProperty("user.dir");
        reNameFiles(new File(property+"/"+"/src/lay"));
        System.out.println(System.currentTimeMillis()-l);
    }

    /**
     * 单线程重命名
     * @param filePath
     */
    public static void modeFiles(String filePath){
        File fileDir = new File(filePath);
        if(fileDir.exists()){
            File[] firstFiles = fileDir.listFiles();
            assert firstFiles != null;
            for (File file : firstFiles) {
                String name = file.getName();
                String firstFlag = name.substring(0,1);
                if("L".equals(firstFlag)){
                    //第一层，去掉文件夹名第一个0
                    String subStrName = name.substring(1).replaceFirst("0","");
                    String newName = "L"+subStrName;
                    file.renameTo(new File(filePath + "/" + newName));
                    //递归找第二级文件夹
                    modeFiles(filePath+"/"+newName);
                }else if ("R".equals(firstFlag)){
                    //第二层，修改文件夹名R后面十六进制为十进制
                    String subStrName = name.substring(1);
                    BigInteger integer = new BigInteger(subStrName, 16);
                    String newName = "R" + integer;
                    file.renameTo(new File(filePath+"/"+newName));
                    //递归找第三级文件
                    modeFiles(filePath+"/"+newName);
                }else if("C".equals(firstFlag)){
                    //第三层，修改文件名C后面十六进制为十进制
                    String subStrName = name.substring(1,name.indexOf("."));
                    BigInteger integer = new BigInteger(subStrName, 16);
                    String newName = "C" + integer +name.substring(name.indexOf("."));
                    file.renameTo(new File(filePath+"/"+newName));
                }
            }
        }
    }

    /**
     * 多线程重命名
     * @param file
     */
    public static void reNameFiles(File file){
        if (!file.exists()) {
            System.out.println("文件不存在！");
            return;
        }
        File[] files = file.listFiles();
        if(files == null){
            System.out.println("空文件");
            return;
        }
        for (File eachRenamefile : files) {
            if(eachRenamefile.isDirectory()){
                //先递归到最底层修改文件名称
                reNameFiles(eachRenamefile);
                //修改文件夹名称
                RenameThread thread = new RenameThread(eachRenamefile);
                thread.start();
            }
            if(eachRenamefile.isFile()){
                RenameThread thread = new RenameThread(eachRenamefile);
                thread.start();
            }
        }
    }
}