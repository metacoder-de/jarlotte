/* Copyright 2015 Felix Becker

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

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
