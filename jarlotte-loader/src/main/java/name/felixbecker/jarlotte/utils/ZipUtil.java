package name.felixbecker.jarlotte.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtil {

    public static void unzip(File sourceFile, File rootDir) throws IOException {

        // Open the zip file
        final ZipFile zipFile = new ZipFile(sourceFile);
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
