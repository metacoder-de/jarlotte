package name.felixbecker.jarlotte;

import name.felixbecker.jarlotte.configuration.InitializerArtifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;

import java.util.List;


@Mojo(name = "packageJar", threadSafe = true, requiresDependencyResolution = ResolutionScope.COMPILE, requiresDependencyCollection = ResolutionScope.COMPILE)
public class JarPackagingMojo extends AbstractMojo {

    @Parameter(required = true)
    private String initializerClass;

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

    @Parameter(readonly = true)
    private List<RemoteRepository> pluginRepos;


    @Parameter(name = "initializerArtifact", readonly = true)
    private InitializerArtifact initializerArtifact;


    public String getInitializerClass() {
        return initializerClass;
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

    public List<RemoteRepository> getPluginRepos() {
        return pluginRepos;
    }

    public InitializerArtifact getInitializerArtifact() {
        return initializerArtifact;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        new JarlottePackagingMojoInARealProgrammingLanguage(this).execute();
    }

}
