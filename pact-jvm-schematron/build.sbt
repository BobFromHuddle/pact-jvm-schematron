name := "pact-jvm-schematron"

version := "0.0.1"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "com.helger" % "ph-schematron" % "2.9.0",
  "au.com.dius" % "pact-jvm-matchers_2.10" % "2.1.10",                                                                                                   
  "org.apache.commons" % "commons-lang3" % "3.3.2",
  "org.specs2"   %% "specs2"         % "2.3.11" % "test",
  "junit"        %  "junit"          % "4.11"  % "test"
)
