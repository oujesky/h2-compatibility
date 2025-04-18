plugins {
    id("h2-compatibility.library-conventions")
    id("h2-compatibility.publishing-conventions")
}

description = "H2 DB MariaDB compatibility - String, Numeric, Date/Time and Misc. functions"

dependencies {
    api(project(":h2-loader"))
    implementation(project(":h2-api"))
    implementation(libs.h2)
    implementation(libs.slf4j.api)

    testImplementation(project(":h2-mariadb-test"))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.assertj.core)

    testRuntimeOnly(libs.junit.jupiter.launcher)
    testRuntimeOnly(libs.logback.classic)
}