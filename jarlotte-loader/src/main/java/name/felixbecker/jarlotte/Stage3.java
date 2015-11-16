package name.felixbecker.jarlotte;

import java.io.File;

public class Stage3 {

    public void run(File webappDir, String initializerClassName, ClassLoader jarlotteClassLoader) throws Exception {


        /*
        we currently don't know the initializer interface here - todo extract it to the jar classes
         */

        System.out.println("Loading and starting initializer " + initializerClassName);
        Class<?> initializerClass = jarlotteClassLoader.loadClass(initializerClassName);
        Object initializer = initializerClass.newInstance();
        initializerClass.getDeclaredMethod("initialize", File.class).invoke(initializer, webappDir);

    }
}
