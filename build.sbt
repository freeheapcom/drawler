organization := "com.freeheap"

name := "drawler"

version := "1.1-SNAPSHOT"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.datastax.cassandra" % "cassandra-driver-core" % "3.0.2",
  "redis.clients" % "jedis" % "2.8.1"
)

resolvers ++= Seq(
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/",
  Resolver.sonatypeRepo("releases"),
  Resolver.mavenLocal
)

mainClass in assembly := Some("Main")

test in assembly := {}

assemblyMergeStrategy in assembly := {
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
