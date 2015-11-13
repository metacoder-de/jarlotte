package name.felixbecker.jarlotte;

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
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;

import java.util.List;


@Mojo(name = "packageJar", threadSafe = true, requiresDependencyResolution = ResolutionScope.COMPILE, requiresDependencyCollection = ResolutionScope.COMPILE)
public class JarPackagingMojo extends AbstractMojo {

    @Parameter(required = true)
    private String initializerClass;

    @Component
    private MavenProject project;

    @Component
    private PluginDescriptor pluginDescriptor;

    @Component
    private RepositorySystem repoSystem;

    @Parameter(readonly = true, defaultValue = "${repositorySystemSession}")
    private RepositorySystemSession repoSession;

    @Parameter(readonly = true, defaultValue = "${project.remoteProjectRepositories}")
    private List<RemoteRepository> projectRepos;

    @Parameter(readonly = true)
    private List<RemoteRepository> pluginRepos;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        /*
        * The {@code <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>} of the artifact to resolve.
         */

        Artifact artifact;
        try
        {
            artifact = new DefaultArtifact( "org.icefaces:icefaces-facelets:1.8.2" );
        }
        catch ( IllegalArgumentException e )
        {
            throw new MojoFailureException( e.getMessage(), e );
        }

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRootArtifact(artifact);
        collectRequest.setRepositories(projectRepos);

        ArtifactRequest request = new ArtifactRequest();
        request.setArtifact( artifact );
        request.setRepositories(projectRepos);


        //getLog().info( "Resolving artifact " + artifact + " from " + remoteRepos );

        ArtifactResult result;
        CollectResult result2;
        try
        {
            result2 = repoSystem.collectDependencies(repoSession, collectRequest);
            result = repoSystem.resolveArtifact( repoSession, request );
        }
        catch ( ArtifactResolutionException | DependencyCollectionException e)
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }

        getLog().info( "Resolved artifact " + artifact + " to " + result.getArtifact().getFile() + " from "
                + result.getRepository() );

        for(int i = 0; i < 100; i++) {
            getLog().info("Executing Jarlotte");
        }
    }

}
