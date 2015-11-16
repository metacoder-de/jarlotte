package name.felixbecker.jarlotte;

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

        final List<URL> jarUrls = new ArrayList<URL>();

        for(File libFile: jarlotteLibDir.listFiles()){
            jarUrls.add(libFile.toURI().toURL());
        }

        final URL[] urlsAsArray = jarUrls.toArray(new URL[jarUrls.size()]);

        final ClassLoader jarlotteClassLoader = new URLClassLoader(urlsAsArray, getClass().getClassLoader());

        new Stage3().run(webappDir, initializerClassName, jarlotteClassLoader);


    }

}
