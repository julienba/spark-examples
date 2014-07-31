name := "Spark example"

version := "1.1"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.0.1",
  "org.apache.spark" %% "spark-streaming" % "1.0.1", 
  "com.twitter" %% "algebird-core" % "0.5.0",
  "org.reactivemongo" %% "reactivemongo" % "0.10.0",
  "com.typesafe.play" %% "play-json" % "2.3.2"
)

resolvers ++= Seq(
  "Akka Repository" at "http://repo.akka.io/releases/",
  "Typesafe" at "http://repo.typesafe.com/typesafe/releases/"
)  

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.1.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.11.3" % "test"
)
