package name.felixbecker.jarlotte;

import name.felixbecker.jarlotte.utils.ZipUtil;

import java.io.File;
import java.nio.file.Files;

public class Stage1 {

    public static void main(String... args) throws Exception {
        Stage1 stage1 = new Stage1();
        stage1.boot();
    }

    public void boot() throws Exception {
        System.out.println("Jarlotte is running =)");

        final File tempWorkingDir = Files.createTempDirectory("jarlotte").toFile();

        tempWorkingDir.deleteOnExit();

        ZipUtil.unzip(new File(getClass().getProtectionDomain().getCodeSource().getLocation().getFile()), tempWorkingDir);
        System.out.println(tempWorkingDir);


        /*Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("Shutdown hook - deleting working directory " + tempWorkingDir);
                Stage1.deleteRecursively(tempWorkingDir);
            }
        }));*/

    }

    public static void deleteRecursively(File f){

        if(f.isDirectory()){
            for(File child : f.listFiles()){
                deleteRecursively(child);
            }
        }

        if(!f.delete()){
            System.err.println("Deleting file " + f.getAbsolutePath() + " failed!");
        } else {
            System.out.println("Deleted file " + f.getAbsolutePath());
        }

    }



}
