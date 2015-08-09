// add required resolvers for plugins
fullResolvers := (projectResolver.value +: Seq(
  Resolver.defaultLocal
))

// allows generation of fat jars (https://github.com/sbt/sbt-assembly)
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.12.0")

// customisable release process (https://github.com/sbt/sbt-release)
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.0")

// displays dependencies that have available updates (https://github.com/rtimush/sbt-updates)
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.8")

// scala linting tool (https://github.com/puffnfresh/wartremover)
addSbtPlugin("org.brianmckenna" % "sbt-wartremover" % "0.13")

// displays dependencies in a graph, requires GraphViz in the machine running it (https://github.com/gilt/sbt-dependency-graph-sugar)
addSbtPlugin("com.gilt" % "sbt-dependency-graph-sugar" % "0.7.5-1")

// watches for changes in code and relaunches app automatically (https://github.com/spray/sbt-revolver)
addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.2")

// plugin to create Eclipse definitions (https://github.com/typesafehub/sbteclipse)
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "3.0.0")

// reports test results to Teamcity (https://github.com/guardian/sbt-teamcity-test-reporting-plugin)
addSbtPlugin("com.gu" % "sbt-teamcity-test-reporting-plugin" % "1.5")
