import bintray.BintrayPlugin.autoImport.{bintrayOrganization, bintrayRepository, bintrayOmitLicense}
import play.routes.compiler.InjectedRoutesGenerator
import play.sbt.routes.RoutesKeys.routesGenerator
import sbt.Keys._
import sbt._

object StackableControllerProjects extends Build {

  lazy val _organization = "com.adendamedia"

  lazy val _version = "0.7.0"

  def _publishTo(v: String) = {
    val nexus = "https://oss.sonatype.org/"
    if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")  
    else Some("releases" at nexus + "service/local/staging/deploy/maven2")
  }

  lazy val _resolvers = Seq(
    "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
    "sonatype releases" at "http://oss.sonatype.org/content/repositories/releases",
    "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
    Resolver.jcenterRepo
  )

  lazy val _scalacOptions = Seq("-unchecked")

  lazy val _pomExtra = {
    <url>https://github.com/t2v/stackable-controller</url>
    <licenses>
      <license>
        <name>Apache License, Version 2.0</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:t2v/stackable-controller</url>
      <connection>scm:git:git@github.com:t2v/stackable-controller</connection>
    </scm>
    <developers>
      <developer>
        <id>gakuzzzz</id>
        <name>gakuzzzz</name>
        <url>https://github.com/gakuzzzz</url>
      </developer>
    </developers>
  }

  val Scala211 = "2.11.11"
  val Scala212 = "2.12.2"

  lazy val core = Project(
    id = "core", 
    base = file("core")
  ).enablePlugins().settings(
    organization := _organization,
    name := "stackable-controller",
    version := _version,
    scalaVersion := Scala212,
    crossScalaVersions := Seq(Scala211, Scala212),
    //publishTo <<= version { (v: String) => _publishTo(v) },
    publishMavenStyle := true,
    resolvers ++= _resolvers,
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play" % play.core.PlayVersion.current % "provided"
    ),
    sbtPlugin := false,
    licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html")),
    bintrayOrganization := Some("adenda"),
    bintrayRepository := "stackable-controller",
    bintrayOmitLicense := true,
    scalacOptions ++= _scalacOptions,
    publishArtifact in Test := false,
    pomIncludeRepository := { x => false }
    //pomExtra := _pomExtra
  )

  lazy val sample = Project("sample", file("sample")).enablePlugins(play.sbt.PlayScala).settings(
    version := _version,
    scalaVersion := Scala212,
    resolvers ++= _resolvers,
    routesGenerator := InjectedRoutesGenerator,
    libraryDependencies ++= Seq(
      play.sbt.Play.autoImport.jdbc,
      play.sbt.Play.autoImport.guice,
      play.sbt.Play.autoImport.specs2 % "test",
      "com.typesafe.play"  %% "play"                         % play.core.PlayVersion.current,
      "org.scalikejdbc"    %% "scalikejdbc"                  % "3.0.0",
      "org.scalikejdbc"    %% "scalikejdbc-config"           % "3.0.0",
      "org.scalikejdbc"    %% "scalikejdbc-play-initializer" % "2.6.0",
      "org.slf4j"          %  "slf4j-simple"                 % "[1.7,)"
    )
  ) dependsOn(core)

  lazy val root = Project(id = "root", base = file(".")).settings(
    scalaVersion := Scala212
  ).aggregate(core, sample) 

}
