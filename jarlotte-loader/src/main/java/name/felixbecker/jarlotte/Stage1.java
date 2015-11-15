package name.felixbecker.jarlotte;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Stage1 {

    public static void main(String... args) throws Exception {
        Stage1 stage1 = new Stage1();
        stage1.boot();
    }

    public void boot() throws Exception {
        System.out.println("Jarlotte is running =)");

        final File tempWorkingDir = Files.createTempDirectory("jarlotte").toFile();

        tempWorkingDir.deleteOnExit();

        unzip(tempWorkingDir);
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

    public void unzip(File rootDir) throws IOException {

        // Open the zip file
        final ZipFile zipFile = new ZipFile(getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        while (entries.hasMoreElements()) {

            final ZipEntry zipEntry = entries.nextElement();

            String name = zipEntry.getName();

            final File file = new File(rootDir, name);

            if (name.endsWith("/")) { // Does that work on windows?
                if (!file.exists() && !file.mkdirs()) {
                    throw new RuntimeException("Failed to create directories for " + file.getAbsolutePath());
                }
                continue;
            }

            final File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    throw new RuntimeException("Couldn't create parent directory for file " + file.getAbsolutePath());
                }
            }

            final InputStream is = zipFile.getInputStream(zipEntry);
            final FileOutputStream fos = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int length;

            while ((length = is.read(bytes)) >= 0) {
                fos.write(bytes, 0, length);
            }

            is.close();
            fos.close();

        }

        zipFile.close();

    }

}
