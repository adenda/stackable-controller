addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.0")
addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.5.1")

scalacOptions ++= Seq("-deprecation", "-language:_", "-unchecked")
