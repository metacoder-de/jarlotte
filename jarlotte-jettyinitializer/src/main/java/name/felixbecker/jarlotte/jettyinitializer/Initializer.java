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

package name.felixbecker.jarlotte.jettyinitializer;

import name.felixbecker.jarlotte.api.JarlotteInitializer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.Jetty;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class Initializer implements JarlotteInitializer {

    public void initialize(File webAppDir) {

        URLClassLoader urlClassLoader = (URLClassLoader) getClass().getClassLoader();

        try {
            Server server = new Server(8080);

            WebAppContext wac = new WebAppContext();
            wac.setResourceBase(webAppDir.getAbsolutePath());
            wac.setDescriptor(webAppDir.getAbsolutePath() + "WEB-INF/web.xml");
            wac.setContextPath("/");
            wac.setClassLoader(new WebAppClassLoader(urlClassLoader, wac));

            server.setHandler(wac);

            server.start();
            server.join();

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
