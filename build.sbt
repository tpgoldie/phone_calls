lazy val phoneCompany = (project in file(".")).settings(
  Seq(
    name := "disco-test-phone-company",
    version := "1.0",
    scalaVersion := "2.12.3",
    libraryDependencies ++= Seq(
      "junit" % "junit" % "4.12" % Test,
      "org.scalacheck" %% "scalacheck" % "1.14.0" % Test,
      "org.scalatest" %% "scalatest" % "3.2.0-SNAP10" % Test
    ),
    unmanagedClasspath in Runtime += baseDirectory.value / "resources"
  )
)
