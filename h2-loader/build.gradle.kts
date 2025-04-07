plugins {
    id("h2-compatibility.library-conventions")
    id("h2-compatibility.publishing-conventions")
}

description = "H2 DB compatibility functions loader"

dependencies {
    implementation(project(":h2-api"))
    implementation(libs.slf4j.api)

    testImplementation(testFixtures(project(":h2-test")))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)

    testRuntimeOnly(libs.junit.jupiter.launcher)
    testRuntimeOnly(libs.logback.classic)
}
