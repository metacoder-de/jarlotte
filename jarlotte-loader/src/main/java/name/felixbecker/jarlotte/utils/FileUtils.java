package name.felixbecker.jarlotte.utils;

import java.io.File;

/**
 * Created by becker on 11/15/15.
 */
public class FileUtils {

    public static void deleteRecursively(File f) {

        if (f.isDirectory()) {
            for (File child : f.listFiles()) {
                deleteRecursively(child);
            }
        }

        if (!f.delete()) {
            System.err.println("Deleting file " + f.getAbsolutePath() + " failed!");
        } else {
            System.out.println("Deleted file " + f.getAbsolutePath());
        }

    }

}
