import java.io.*;
import java.math.BigInteger;
import java.util.Objects;

/**
 * @author zhangshuan
 * @version 1.0
 * @date 2021/4/29 1:32 下午
 */
public class RenameThread extends Thread {

    public File file;

    public RenameThread(File file){
        this.file = file;
    }

    @Override
    public void run() {
        String name = file.getName();
        String flag = name.substring(0,1);
        if(Objects.equals("L",flag)){
            //第一层，去掉文件夹名第一个0
            String subStrName = name.substring(1).replaceFirst("0","");
            String newName = "L"+subStrName;
            reName(file,newName);
        }
         if (Objects.equals("R",flag)){
            //第二层，修改文件夹名R后面十六进制为十进制
            String subStrName = name.substring(1);
            BigInteger integer = new BigInteger(subStrName, 16);
            String newName = "R" + integer;
            reName(file,newName);
        }
         if(Objects.equals("C",flag)){
             //第三层，修改文件名C后面十六进制为十进制
             String subStrName = name.substring(1,name.indexOf("."));
             BigInteger integer = new BigInteger(subStrName, 16);
             String newName = "C" + integer +name.substring(name.indexOf("."));
             reName(file,newName);
         }
    }

    private void reName(File file,String newName) {
        try {
            boolean renameTo = file.renameTo(new File(file.getParentFile().getPath() + "/" + newName));
            System.out.println(renameTo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
