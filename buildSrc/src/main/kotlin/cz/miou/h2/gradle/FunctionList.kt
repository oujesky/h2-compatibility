package cz.miou.h2.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class FunctionList : DefaultTask() {

    @TaskAction
    fun exec() {
        val packagePattern = Regex("package (?<package>.+?);")
        val linkPattern = Regex("<a href=\"(?<link>https://mariadb\\.com/kb/en/.+?)\">(?<name>.+?)</a>")

        project.layout.projectDirectory.dir("src/main/java/cz/miou/h2")
            .asFileTree
            .matching { include("**/*.java") }
            .sorted()
            .mapNotNull {
                val content = it.readText()

                if (!content.contains("implements FunctionDefinition")) {
                    return@mapNotNull null
                }

                val packageName = packagePattern.find(content)?.groups?.get("package")?.value
                val match = linkPattern.find(content)
                val link = match?.groups?.get("link")?.value
                val name = match?.groups?.get("name")?.value

                if (packageName != null && name != null && link != null) {
                    Coordinates(packageName, name, link)
                } else {
                    println("WARNING: no documentation link found for file: $it")
                    null
                }
            }
            .groupBy { it.packageName }
            .forEach { group ->
                println("\nPackage ${group.key}")
                group.value
                    .sortedBy { it.name }
                    .forEach {
                    println(" * [`${it.name}`](${it.link})")
                }
            }
    }

    data class Coordinates(val packageName: String, val name: String, val link: String)
}