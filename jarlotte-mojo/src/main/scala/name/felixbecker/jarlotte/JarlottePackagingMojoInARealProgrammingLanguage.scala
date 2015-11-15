package name.felixbecker.jarlotte

import java.io.{FileWriter, File}
import java.nio.file.Paths

import net.lingala.zip4j.core.ZipFile
import net.lingala.zip4j.model.ZipParameters
import org.apache.maven.plugin.MojoFailureException
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.collection.CollectRequest
import org.eclipse.aether.graph.Dependency
import org.eclipse.aether.resolution.{ArtifactRequest, ArtifactResult, DependencyRequest}
import org.eclipse.aether.util.artifact.JavaScopes
import org.eclipse.aether.util.filter.DependencyFilterUtils

/**
  * Created by becker on 11/15/15.
  */
class JarlottePackagingMojoInARealProgrammingLanguage(mojo: JarPackagingMojo) {

  def execute(): Unit = {

    mojo.getLog.info("===========================> " + mojo.getInitializerArtifact)


    val targetDir = Paths.get(mojo.getProject.getBuild.getDirectory).toFile
    val projectFinalName: String = mojo.getProject.getBuild.getFinalName
    val buildDir = Paths.get(mojo.getProject.getBuild.getDirectory, projectFinalName).toFile

    if(!buildDir.exists()){
      throw new MojoFailureException(s"Couldn't find target directory $targetDir. Please make sure that the maven war plugin ran in this phase before (order in xml matters, see effective pom in doubt)!")
    }

    /* Step 1: Add WAR-Content to jar */
    val zipFile = new ZipFile(Paths.get(targetDir.getAbsolutePath, "jarlotte.jar").toFile)
    val zipParameters = new ZipParameters
    zipFile.addFolder(buildDir, zipParameters)

    /* Step 2: Add Jarlotte stuff */

    val ownVersion = mojo.getPluginDescriptor.getVersion
    val loaderJarResolutionResult = resolveArtifact(s"name.felixbecker:jarlotte-loader:$ownVersion")
    println(s"Loader jar resolution ${loaderJarResolutionResult.getArtifact.getFile}")


    val loaderZipFileExtractionDir = new File(mojo.getProject.getBuild.getDirectory, "jarlotte-loader-extracted")
    val loaderZipFile = new ZipFile(loaderJarResolutionResult.getArtifact.getFile)
    println(s"Extracting ${loaderZipFile.getFile} to $loaderZipFileExtractionDir")
    loaderZipFile.extractAll(loaderZipFileExtractionDir.getAbsolutePath)

    zipFile.addFolder(Paths.get(loaderZipFileExtractionDir.getAbsolutePath, "name").toFile, zipParameters)
    val metaInfZipParameters = new ZipParameters
    metaInfZipParameters.setRootFolderInZip("META-INF")

    val manifestFile = Paths.get(loaderZipFileExtractionDir.getAbsolutePath, "MANIFEST.MF").toFile
    val manifestFileWriter = new FileWriter(manifestFile, true) // append to existing manifest

    manifestFileWriter.append(s"Webapp-Dir-Name: $projectFinalName")

    manifestFileWriter.close()

    zipFile.addFile(manifestFile, metaInfZipParameters)


    mojo.getLog.info(s"target directory is ${mojo.getProject.getBuild.getDirectory} - ${projectFinalName}")

  }

  def resolveArtifact(artifactCoords: String): ArtifactResult = {

    val artifact = new DefaultArtifact(artifactCoords)
    val artifactRequest = new ArtifactRequest()
    artifactRequest.setArtifact(artifact)
    artifactRequest.setRepositories(mojo.getProjectRepos)

    mojo.getRepoSystem.resolveArtifact(mojo.getRepoSession, artifactRequest)

  }

  def resolveArtifacts(artifactCoords: String): List[ArtifactResult] = {

    import scala.collection.JavaConversions._

    val artifact = new DefaultArtifact(artifactCoords)

    val classpathFlter = DependencyFilterUtils.classpathFilter(JavaScopes.COMPILE) // ??? from example TODO - maybe thats the reason why i don't find my initializer

    val collectRequest = new CollectRequest
    collectRequest.setRoot(new Dependency(artifact, JavaScopes.COMPILE))
    collectRequest.setRepositories(mojo.getProjectRepos)

    val dependencyRequest = new DependencyRequest(collectRequest, classpathFlter)
    val artifactResults = mojo.getRepoSystem.resolveDependencies(mojo.getRepoSession, dependencyRequest).getArtifactResults

    /*
    artifactResults.foreach { artifactResult =>
      mojo.getLog.info("Initializer dependencies are: " + artifactResult.getArtifact + " resolved to " + artifactResult.getArtifact.getFile)
    }

    mojo.getProject.getArtifacts.foreach { projectArtifact =>
      println(s"Jar has to contain artifact ${projectArtifact.getFile}")
    }

    mojo.getProject.getResources.foreach { resource =>
      println(s"Jar has to contain resource ${resource}")
    }
    */

    artifactResults.toList
  }
}
