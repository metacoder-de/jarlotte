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

package de.metacoder.jarlotte;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Build classloader on extracted structure and load the initializer
 */
public class Stage2 {

    public void run(File webappDir, File jarlotteLibDir, String initializerClassName) throws Exception {

        System.out.println("Starting stage 2 on data structure " + webappDir.getAbsolutePath());

        System.out.println("Building class loader for initializer");

        final List<URL> jarUrls = new ArrayList<URL>();

        for(File libFile: jarlotteLibDir.listFiles()){
            jarUrls.add(libFile.toURI().toURL());
        }

        final URL[] urlsAsArray = jarUrls.toArray(new URL[jarUrls.size()]);

        final ClassLoader jarlotteClassLoader = new URLClassLoader(urlsAsArray, getClass().getClassLoader());

        System.out.println("Entering Stage 3");
        new Stage3().run(webappDir, initializerClassName, jarlotteClassLoader);

    }

}
