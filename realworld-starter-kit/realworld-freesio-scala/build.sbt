ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.navneetgupta"
ThisBuild / organizationName := "navneetgupta"
ThisBuild / name             := "realworld-freesio-scala"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:postfixOps",
  "-Xfatal-warnings",
  "-Ypartial-unification",
  "-language:higherKinds",
  "-language:implicitConversions"
)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6")
addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.0-M4")
addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M11" cross CrossVersion.full)

libraryDependencies ++= {
  val catVersion = "1.5.0"
  val freeioVersion = "0.8.2"
  val doobieVersion = "0.6.0"
  Seq(
    "io.frees" %% "frees-core" % freeioVersion,
    "io.frees" %% "frees-logging" % freeioVersion,
    "io.frees" %% "frees-effects" % freeioVersion,
    "io.frees" %% "frees-cache" % freeioVersion,
    "org.scalactic" %% "scalactic" % "3.0.5",
    "org.typelevel" %% "cats-core" % catVersion,
    "org.tpolecat" %% "doobie-core" % doobieVersion,
    "org.tpolecat" %% "doobie-postgres"  % doobieVersion,
    "org.tpolecat" %% "doobie-specs2"    % doobieVersion % "test", // Specs2 support for typechecking statements.
    "org.tpolecat" %% "doobie-scalatest" % doobieVersion % "test"  // ScalaTest support for typechecking statements.
  )
}