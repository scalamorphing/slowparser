scalaVersion in ThisBuild := "2.11.8"

lazy val slowparser = {
  crossProject in file(".")
}.settings(
  Seq(
    name := "slowparser",
    version := "0.0.1",
    organization := "expert.scalamorphing",
    libraryDependencies += "com.lihaoyi" %%% "fastparse" % "0.4.1",
    libraryDependencies += "org.scalactic" %%% "scalactic" % "3.0.1",
    libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.1" % "test",
    scapegoatVersion := "1.1.0"
  )
)

lazy val slowparserJS = slowparser.js
lazy val slowparserJVM = slowparser.jvm
