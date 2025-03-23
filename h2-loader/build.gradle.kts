plugins {
    id("h2-compatibility.library-conventions")
}

dependencies {
    implementation(project(":h2-api"))
    implementation(libs.slf4j.api)

    testImplementation(testFixtures(project(":h2-test")))
//    testImplementation(libs.h2)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)

    testRuntimeOnly(libs.junit.jupiter.launcher)
    testRuntimeOnly(libs.logback.classic)
}
