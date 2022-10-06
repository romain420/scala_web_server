val scala3Version = "3.2.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "My beautiful project", // TODO: name your project
    version := "0.1.0-SNAPSHOT",
    developers := List( // TODO: replace the following developer by your team developers
      Developer(
        id    = "romaindreuilhet",
        name  = "Romain Dreuilhet",
        email = "romain.dreuilhet@edu.esiee.fr",
        url   = url("https://github.com/romain420")
      )
    ),
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq("org.scalameta" %% "munit" % "0.7.29" % Test)
  )
