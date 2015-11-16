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

package name.felixbecker.jarlotte;

import name.felixbecker.jarlotte.utils.FileUtils;
import name.felixbecker.jarlotte.utils.ZipUtil;

import java.io.File;
import java.nio.file.Files;
import java.util.Properties;

public class Stage1 {

    public static void main(String... args) throws Exception {
        Stage1 stage1 = new Stage1();
        stage1.boot();
    }

    public void boot() throws Exception {
        System.out.println("Jarlotte is running =)");

        final File tempWorkingDir = Files.createTempDirectory("jarlotte").toFile();

        tempWorkingDir.deleteOnExit();

        System.out.println("Extracting jar file to the temporary working directory " + tempWorkingDir);
        ZipUtil.unzip(new File(getClass().getProtectionDomain().getCodeSource().getLocation().getFile()), tempWorkingDir);

        // Read Manifest to get the web project name
        final Properties jarlotteProperties = new Properties();
        jarlotteProperties.load(getClass().getResourceAsStream("/META-INF/jarlotte.properties"));

        System.out.println("Path of the webapp is: " + jarlotteProperties.getProperty("Webapp-Dir-Name"));
        System.out.println("Initializer Class is: " + jarlotteProperties.getProperty("Initializer-Class"));

        System.out.println("Registering shutdown hook");

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("Shutdown recognized - deleting working directory " + tempWorkingDir);
                FileUtils.deleteRecursively(tempWorkingDir);
                System.out.println("Deleted " + tempWorkingDir + " successfully");
            }
        }));

        new Stage2().run(new File(tempWorkingDir, jarlotteProperties.getProperty("Webapp-Dir-Name")), new File(tempWorkingDir, "jarlotte-lib"), jarlotteProperties.getProperty("Initializer-Class"));

    }

}
