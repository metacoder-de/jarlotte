package name.felixbecker.jarlotte;

import name.felixbecker.jarlotte.utils.ZipUtil;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

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



        // Read Manifest to get the web project name
        final Properties jarlotteProperties = new Properties();
        jarlotteProperties.load(getClass().getResourceAsStream("/META-INF/jarlotte.properties"));

        System.out.println("Path of the webapp is: " + jarlotteProperties.getProperty("Webapp-Dir-Name"));
        System.out.println("Initializer Class is: " + jarlotteProperties.getProperty("Initializer-Class"));



        /*Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("Shutdown hook - deleting working directory " + tempWorkingDir);
                FileUtils.deleteRecursively(tempWorkingDir);
            }
        }));*/

        new Stage2().run(new File(tempWorkingDir, jarlotteProperties.getProperty("Webapp-Dir-Name")), new File(tempWorkingDir, "jarlotte-lib"), jarlotteProperties.getProperty("Initializer-Class"));

    }





}
