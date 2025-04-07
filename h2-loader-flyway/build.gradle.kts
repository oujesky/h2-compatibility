plugins {
    id("h2-compatibility.library-conventions")
    id("h2-compatibility.publishing-conventions")
}

description = "H2 DB compatibility functions loader - Flyway integration"

dependencies {
    api(project(":h2-loader"))

    testImplementation(testFixtures(project(":h2-test")))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    testImplementation(libs.flyway.core)

    testRuntimeOnly(libs.junit.jupiter.launcher)
    testRuntimeOnly(libs.logback.classic)
}
