import cz.miou.h2.gradle.FunctionList
import cz.miou.h2.gradle.FunctionScaffolding

group = "cz.miou.h2"
version = "1.0.0"

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