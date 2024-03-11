plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "second"
include("commons:base")
include("commons:servlet")
include("services:demo")
include("services:gateway")
include("test")