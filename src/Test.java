import java.io.File;
import java.math.BigInteger;

/**
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