val scala3Version = "3.2.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "WebServer in Scala",
    version := "0.1.0-SNAPSHOT",
    developers := List(
      Developer(
        id    = "romaindreuilhet",
        name  = "Romain Dreuilhet",
        email = "romain.dreuilhet@edu.esiee.fr",
        url   = url("https://github.com/romain420")
      ),
      Developer(
        id    = "theogueuret",
        name  = "Théo Gueuret",
        email = "theo.gueuret@edu.esiee.fr",
        url   = url("https://github.com/to-grt")
      ),
      Developer(
        id    = "vithulaksan",
        name  = "Vithulaksan Naguleswaran",
        email = "vithulaksan.naguleswaran@edu.esiee.fr",
        url   = url("https://github.com/vithulaksan")
      ),
      Developer(
              id    = "mariusmorel",
              name  = "Marius Morel",
              email = "marius.morel@edu.esiee.fr",
              url   = url("https://github.com/usmira")
            ),
      Developer(
        id    = "mathieupochon",
        name  = "Mathieu Pochon",
        email = "mathieu.pochon@edu.esiee.fr",
        url   = url("https://github.com/pochonm")
      )
    ),
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq("org.scalameta" %% "munit" % "0.7.29" % Test, "com.google.code.gson" % "gson" % "2.9.1")
  )
