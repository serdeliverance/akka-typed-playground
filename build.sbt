name := "akka-typed-playground"

version := "0.1"

scalaVersion := "2.13.3"

val akkaVersion = "2.6.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion
)