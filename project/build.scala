import sbt._
import sbt.Keys._
import releaseit.ReleaseItPlugin._

object build extends Build {
  // ensure we use proper encoding
  private val encoding: String = sys.props("file.encoding")
  assert(encoding == "UTF-8", s"File encoding must be UTF-8 but was $encoding")

  val commonScalacOptions = Seq(
      "-target:jvm-1.7", //Target platform for object files.
      "-language:postfixOps", //Language: allows postfix operations
      "-language:higherKinds", //Language: enables higher kinds (http://stackoverflow.com/questions/6246719/what-is-a-higher-kinded-type-in-scala)
      "-language:implicitConversions", //Language: enables implicit conversions in code
      "-deprecation", //Emit warning and location for usages of deprecated APIs.
      "-unchecked", //Enable additional warnings where generated code depends on assumptions.
      "-encoding", "UTF-8", //Specify character encoding used by source files.
      "-feature", //Emit warning and location for usages of features that should be imported explicitly.
      "-Xfatal-warnings", //Fail the compilation if there are any warnings.
      "-Xlint", //Enable specific warning
      "-Yno-adapted-args", //Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
      "-Ywarn-dead-code", //Warn when dead code is identified.
      "-Ywarn-numeric-widen", //Warn when numerics are widened.
      "-Ywarn-value-discard", //Warn when non-Unit expression results are unused.
      "-Xfuture" //Turn on future language features.
    )

  // define project as aggregate of our 4 elements
  // Project is a method defined below
  lazy val root = project("project", base = ".")
    .aggregate(service, client, model, acceptance)
    .settings(publishArtifact := false)

  // declare service and its dependencies.
  lazy val service = project("service")
    .dependsOn(model)
    .settings(releaseItSettings ++ Seq(
      name := "project-service",
      scalacOptions ++= commonScalacOptions,
      libraryDependencies ++= Seq(
        logbackClassic,
        scodec,
        c3po,
        scalatest,
        h2
      ),
      resourceGenerators in Compile <+= (resourceManaged, baseDirectory, target) map {
        (managedBase: File, base: File, target: File) => ResourceGenerators.generate(managedBase, base, target)
      },
      releaseMainClass := Some("project.service.Boot"),
      releaseToEnvironments := Seq("prod", "dev", "local"),
      releasePackageBuildConfigurationName := "Service_BuildTestAndPackage",
      releaseApplicationDescription := "Project Service"
  ): _*)

  lazy val client = project("client")
    .dependsOn(model)
    .settings(Seq(
    name := "project-client",
    scalacOptions ++= commonScalacOptions,
    libraryDependencies ++= Seq(
      scalatest,
      scalacache
    )
  ): _*)

  lazy val model = project("model").settings(Seq(
    name := "project-model",
    scalacOptions ++= commonScalacOptions,
    libraryDependencies ++= Seq(
    )
  ): _*)

  lazy val acceptance = project("acceptance")
    .dependsOn(client, service)
    .settings(Seq(
      name := "project-acceptance",
      scalacOptions ++= commonScalacOptions,
      libraryDependencies ++= Seq(
        scalatest
      )): _*)

  // basic configuration common to all modules
  def project(id: String, base: String = null) = Project(id = id, base = file(Option(base).getOrElse(id))).settings(Seq(
    version := Option(System.getProperty("version")).getOrElse("dev-SNAPSHOT"),
    organization := "com.project",
    Keys.scalaVersion := "2.11.7",
    scalaBinaryVersion := "2.11",
    externalResolvers := Seq(
      Resolver.defaultLocal
    ),
    credentials += Credentials("Sonatype Nexus Repository Manager", "nexus.local", "user", "pwd"),
    publishTo := Some("Nexus repo" at "http://nexus.local:8081/nexus/content/repositories/project-releases/"),
    parallelExecution in Test := true
  ) ++ net.virtualvoid.sbt.graph.Plugin.graphSettings: _*)

  val scalacache                  = "com.github.cb372"             %% "scalacache-guava"               % "0.6.4"
  val argonaut                    = "io.argonaut"                  %% "argonaut"                       % "6.1-M5"
  val logbackClassic              = "ch.qos.logback"                % "logback-classic"                % "1.1.3"
  val c3po                        = "com.mchange"                   % "c3p0"                           % "0.9.5.1"
  val scodec                      = "org.scodec"                   %% "scodec-bits"                    % "1.0.7"
  val h2                          = "com.h2database"                % "h2"                             % "1.4.187"
  val scalatest                   = "org.scalatest"                %% "scalatest"                      % "2.2.4"           % "test"
}
