name := "Spark example"

version := "1.0"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "0.9.0-incubating",
  "com.twitter" %% "algebird-core" % "0.5.0"
)

resolvers += "Akka Repository" at "http://repo.akka.io/releases/"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.1.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.11.3" % "test"
)
