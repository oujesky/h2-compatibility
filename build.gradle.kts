import cz.miou.h2.gradle.FunctionList
import cz.miou.h2.gradle.FunctionScaffolding
import org.jreleaser.model.Active

plugins {
    `java-library`
    alias(libs.plugins.jreleaser)
}

group = "cz.miou.h2"
version = "1.0.0"
description = "H2 DB compatibility functions"

allprojects {
    tasks.register<FunctionScaffolding>("functionScaffolding") {
        group = "scaffolding"
        description = "Create classes function classes scaffolding"
        functionSignature = project.property("function.signature") as String
        packageName = project.property("function.package") as String
    }

    tasks.register<FunctionList>("functionList") {
        group = "scaffolding"
        description = "List defined functions"
    }
}

gradle.projectsEvaluated {

    jreleaser {
        dryrun = System.getenv("CI").isNullOrBlank()

        project {
            name = rootProject.name
            description = rootProject.description
            version = rootProject.version.toString()
            author("Miroslav Oujesky")
            license = "MIT"
            inceptionYear = "2025"
            links {
                homepage = "https://github.com/oujesky/h2-compatibility"
            }
        }

        release {
            github {
                repoOwner = "oujesky"
                name = "h2-compatibility"
                branch = "main"
            }
        }

        signing {
            active = Active.ALWAYS
            armored = true
            verify = true
        }

        deploy {
            maven {
                mavenCentral.create("sonatype") {
                    active = Active.ALWAYS
                    url = "https://central.sonatype.com/api/v1/publisher"
                    subprojects.filter { it.plugins.hasPlugin("maven-publish") }.forEach{ subproject ->
                        stagingRepository("${subproject.layout.buildDirectory.get()}/staging-deploy")
                    }
                    applyMavenCentralRules = true
                }
            }
        }

        distributions {
            subprojects.filter { it.plugins.hasPlugin("maven-publish") }.forEach{ subproject ->
                create(subproject.name) {
                    project {
                        description = providers.provider { subproject.description ?: rootProject.description }
                    }

                    artifact {
                        path = subproject.tasks.named<Jar>("jar").get().archiveFile.get().asFile
                    }

                    artifact {
                        path = subproject.tasks.named<Jar>("sourcesJar").get().archiveFile.get().asFile
                        platform = "java-sources"
                    }

                    artifact {
                        path = subproject.tasks.named<Jar>("javadocJar").get().archiveFile.get().asFile
                        platform = "java-docs"
                    }
                }
            }
        }
    }
}