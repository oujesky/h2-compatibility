plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}