plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "second"
include("base")
include("servlet")
include("demo")
include("gateway")
