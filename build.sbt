organization := "com.micronautics"

name := "beth"

version := "0.1.0"

scalaVersion := "2.12.4"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-target:jvm-1.8",
  "-unchecked",
  "-Ywarn-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Xlint"
)

scalacOptions in (Compile, doc) ++= baseDirectory.map {
  (bd: File) => Seq[String](
     "-sourcepath", bd.getAbsolutePath,
     "-doc-source-url", "https://github.com/mslinn/{name.value}/tree/master€{FILE_PATH}.scala"
  )
}.value

javacOptions ++= Seq(
  "-Xlint:deprecation",
  "-Xlint:unchecked",
  "-source", "1.8",
  "-target", "1.8",
  "-g:vars"
)

libraryDependencies ++= Seq(
  "io.circe"          %% "circe-config"     % "0.3.0" withSources(),
  "io.circe"          %% "circe-generic"    % "0.8.0" withSources(),
  "org.jline"         %  "jline"            % "3.5.1" withSources(),
  "org.eclipse.jgit"  %  "org.eclipse.jgit" % "4.9.0.201710071750-r" withSources(),
  "ch.qos.logback"    %  "logback-classic"  % "1.2.3",
  //
  "org.scalatest"     %% "scalatest"   % "3.0.3" % Test withSources(),
  "junit"             %  "junit"       % "4.12"  % Test
)

fork in Test := true // https://stackoverflow.com/a/23575337/553865; forked tests prevents IDEA from attaching a debugger when launching tests via sbt tasks

logLevel := Level.Warn

// Only show warnings and errors on the screen for compilations.
// This applies to both test:compile and compile and is Info by default
logLevel in compile := Level.Warn

// Level.INFO is needed to see detailed output when running tests
logLevel in test := Level.Info

// define the statements initially evaluated when entering 'console', 'console-quick', but not 'console-project'
initialCommands in console := """
                                |""".stripMargin

cancelable := true

sublimeTransitive := true
