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

import de.metacoder.jarlotte.configuration.InitializerArtifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;

import java.util.List;


@Mojo(name = "packageJar", threadSafe = true, requiresDependencyResolution = ResolutionScope.COMPILE, requiresDependencyCollection = ResolutionScope.COMPILE)
public class JarPackagingMojo extends AbstractMojo {

    @Parameter(required = true)
    private String initializerClass;

    @Parameter(name = "initializerArtifact", readonly = true)
    private InitializerArtifact initializerArtifact;

    @Parameter(defaultValue = "${project}", readonly = true )
    private MavenProject project;

    @Parameter(defaultValue = "${plugin}", readonly = true )
    private PluginDescriptor pluginDescriptor;

    @Component
    private RepositorySystem repoSystem;

    @Parameter(readonly = true, defaultValue = "${repositorySystemSession}")
    private RepositorySystemSession repoSession;

    @Parameter(readonly = true, defaultValue = "${project.remoteProjectRepositories}")
    private List<RemoteRepository> projectRepos;

    @Component
    private MavenProjectHelper projectHelper;


    public String getInitializerClass() {
        return initializerClass;
    }

    public InitializerArtifact getInitializerArtifact() {
        return initializerArtifact;
    }

    public MavenProject getProject() {
        return project;
    }

    public PluginDescriptor getPluginDescriptor() {
        return pluginDescriptor;
    }

    public RepositorySystem getRepoSystem() {
        return repoSystem;
    }

    public RepositorySystemSession getRepoSession() {
        return repoSession;
    }

    public List<RemoteRepository> getProjectRepos() {
        return projectRepos;
    }

    public MavenProjectHelper getProjectHelper() {
        return projectHelper;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        new JarPackagingMojoExecutor(this).execute();
    }

}
