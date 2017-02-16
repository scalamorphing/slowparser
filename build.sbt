import sbt.Keys.version

scalaVersion in ThisBuild := "2.11.8"


lazy val slowparser = {
  crossProject in file(".")
}.settings(
  Seq(
    name := "slowparser",
    version := "0.0.1",
    organization := "expert.scalamorphing",
    libraryDependencies += "com.lihaoyi" %%% "fastparse" % "0.4.1",
    libraryDependencies += "org.scalactic" %%% "scalactic" % "3.0.1" % "test",
    libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.1" % "test",
    scapegoatVersion := "1.1.0",
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
    publishTo := Some("bintray-scalamorphing-slowparser" at "https://api.bintray.com/maven/scalamorphing/maven/slowparser")
  )
)

lazy val slowparserJS = slowparser.js.settings(
  Seq(
    version := "0.0.1"
  )
)

lazy val slowparserJVM = slowparser.jvm.settings(
  Seq(
    version := "0.0.1"
  )
)
