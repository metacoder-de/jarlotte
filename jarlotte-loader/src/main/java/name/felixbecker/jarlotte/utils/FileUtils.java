package name.felixbecker.jarlotte.utils;

import java.io.File;

public class FileUtils {

    public static void deleteRecursively(File f) {

        if (f.isDirectory()) {
            for (File child : f.listFiles()) {
                deleteRecursively(child);
            }
        }

        if (!f.delete()) {
            System.err.println("Deleting file " + f.getAbsolutePath() + " failed!");
        }


    }

}
