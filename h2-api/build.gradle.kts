plugins {
    id("h2-compatibility.library-conventions")
    id("h2-compatibility.publishing-conventions")
}

description = "H2 DB compatibility API"

dependencies {
    compileOnly(libs.h2)
}