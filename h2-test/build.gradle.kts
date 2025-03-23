plugins {
    id("h2-compatibility.library-conventions")
    `java-test-fixtures`
}

dependencies {
    implementation(libs.h2)
    implementation(libs.junit.jupiter.api)

    testFixturesImplementation(project(":h2-api"))
    testFixturesImplementation(libs.h2)
}
