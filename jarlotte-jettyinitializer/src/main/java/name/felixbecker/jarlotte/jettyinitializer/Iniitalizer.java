package name.felixbecker.jarlotte.jettyinitializer;

import name.felixbecker.jarlotte.api.JarlotteInitializer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.Jetty;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;

public class Iniitalizer implements JarlotteInitializer {

    public void initialize(File extractedJarTempDir) {

        try {
            Server server = new Server(8080);

            WebAppContext webapp = new WebAppContext();
            webapp.setContextPath("/");
            webapp.setTempDirectory(extractedJarTempDir);
            server.setHandler(webapp);
            server.start();
            server.join();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
