plugins {
    id("h2-compatibility.library-conventions")
    `java-test-fixtures`
}

dependencies {
    implementation(project(":h2-loader"))
    implementation(libs.h2)
    implementation(libs.junit.jupiter.api)
    implementation(libs.assertj.core)

    testFixturesImplementation(project(":h2-api"))
    testFixturesImplementation(libs.h2)
}
