name := "Game-Engine"

version := "0.1"

// Version of Scala used by the project
scalaVersion := "2.13.8"

// Add dependency on ScalaFX library
libraryDependencies += "org.scalafx" %% "scalafx" % "18.0.1-R27"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature")

mainClass := Some("hello.ScalaFXHelloWorld")

// Fork a new JVM for 'run' and 'test:run', to avoid JavaFX double initialization problems
fork := true
maintainer := "NAME <example@example.com>"
packageSummary := "PROJECT_NAME"
packageDescription := """PROJECT_DESCRIPTION."""
wixProductId := "ce07be71-510d-414a-92d4-dff47631848q"
wixProductUpgradeId := "4552fb0e-e257-4dbd-9ecb-dba9dbacf42x"
// Add JavaFX dependencies
libraryDependencies ++= {
  // Determine OS version of JavaFX binaries
  lazy val osName = System.getProperty("os.name") match {
    case n if n.startsWith("Linux") => "linux"
    case n if n.startsWith("Mac") => "mac"
    case n if n.startsWith("Windows") => "win"
    case _ => throw new Exception("Unknown platform!")
  }
  Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
    .map(m => "org.openjfx" % s"javafx-$m" % "18.0.1" classifier osName)
}
enablePlugins(JavaAppPackaging)