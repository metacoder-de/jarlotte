package name.felixbecker.jarlotte.jettyinitializer;

import name.felixbecker.jarlotte.api.JarlotteInitializer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.Jetty;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class Initializer implements JarlotteInitializer {

    public void initialize(File webAppDir) {

        System.out.println("I am the classloader " + getClass().getClassLoader());
        URLClassLoader u = (URLClassLoader) getClass().getClassLoader();
        for(URL url : u.getURLs()){
            System.out.println("Initializer: " + url);
        }

        System.out.println("Webapp root dir: " + webAppDir.getAbsolutePath());

        try {
            /*
            Server server = new Server(8080);

            WebAppContext webapp = new WebAppContext();
            webapp.setContextPath("/");
            webapp.setTempDirectory(webAppDir);
            webapp.setResourceBase(".");
            server.setHandler(webapp);
            server.start();
            server.join();
            */
            Server server = new Server(8080);


            WebAppContext wac = new WebAppContext();
            wac.setResourceBase(webAppDir.getAbsolutePath());
            wac.setDescriptor(webAppDir.getAbsolutePath() + "WEB-INF/web.xml");
            wac.setContextPath("/");
            wac.setParentLoaderPriority(true);

            server.setHandler(wac);

            server.start();
            server.join();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
