plugins {
    `java-library`
    `signing`
    `maven-publish`
}

java {
    withJavadocJar()
    withSourcesJar()
}

version = rootProject.version

tasks.withType<Jar> {
    archiveBaseName.set(project.name)
    archiveVersion.set(project.version.toString())
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            artifactId = project.name
            version = rootProject.version.toString()


            from(components["java"])

            pom {
                name.set(project.name)
                description.set(provider { project.description })
                url = "https://github.com/oujesky/h2-compatibility"
                inceptionYear = "2025"
                licenses {
                    license {
                        name = "MIT License"
                        url = "https://opensource.org/licenses/MIT"
                    }
                }
                developers {
                    developer {
                        id = "oujesky"
                        name = "Miroslav Oujesky"
                        email = "mira@oujesky.cz"
                    }
                }
                scm {
                    connection = "scm:git:https://github.com/oujesky/h2-compatibility.git"
                    developerConnection = "scm:git:https://github.com/oujesky/h2-compatibility.git"
                    url = "https://github.com/oujesky/h2-compatibility"
                }
            }
        }
    }
    repositories {
        mavenLocal()
        maven {
            url = layout.buildDirectory.dir("staging-deploy").get().asFile.toURI()
        }
    }
}

tasks.withType<Javadoc> {
    val options = options as StandardJavadocDocletOptions
    options.addStringOption("link", "https://h2database.com/javadoc/")
    options.tags("apiNote:a:API Note:")
    options.tags("implSpec:a:Implementation Requirements:")
    options.tags("implNote:a:Implementation Note:")

    if (JavaVersion.current().isJava9Compatible) {
        options.addBooleanOption("html5", true)
    }
}
