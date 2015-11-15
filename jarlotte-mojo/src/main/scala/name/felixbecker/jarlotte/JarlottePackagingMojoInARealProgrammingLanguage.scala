package name.felixbecker.jarlotte

import org.eclipse.aether.artifact.{Artifact, DefaultArtifact}
import org.eclipse.aether.collection.CollectRequest
import org.eclipse.aether.graph.{Dependency, DependencyFilter}
import org.eclipse.aether.resolution.DependencyRequest
import org.eclipse.aether.util.artifact.JavaScopes
import org.eclipse.aether.util.filter.DependencyFilterUtils

/**
  * Created by becker on 11/15/15.
  */
class JarlottePackagingMojoInARealProgrammingLanguage(mojo: JarPackagingMojo) {

  def execute(): Unit = {

      mojo.getLog.info("===========================> " + mojo.getInitializerArtifact)

      val artifact = new DefaultArtifact(s"${mojo.getInitializerArtifact.getGroupId}:${mojo.getInitializerArtifact.getArtifactId}:${mojo.getInitializerArtifact.getVersion}")

      val classpathFlter = DependencyFilterUtils.classpathFilter(JavaScopes.COMPILE)     // ??? from example TODO - maybe thats the reason why i don't find my initializer

      val collectRequest = new CollectRequest
      collectRequest.setRoot(new Dependency(artifact, JavaScopes.COMPILE))
      collectRequest.setRepositories(mojo.getProjectRepos)

      val dependencyRequest = new DependencyRequest(collectRequest, classpathFlter)
      val artifactResults = mojo.getRepoSystem.resolveDependencies(mojo.getRepoSession, dependencyRequest).getArtifactResults

      import scala.collection.JavaConversions._

      artifactResults.foreach { artifactResult =>
        mojo.getLog.info("Initializer dependencies are: " + artifactResult.getArtifact + " resolved to " + artifactResult.getArtifact.getFile)
      }

      mojo.getProject.getArtifacts.foreach { projectArtifact =>
        println(s"Jar has to contain ${projectArtifact.getFile}")
      }
  }
}
