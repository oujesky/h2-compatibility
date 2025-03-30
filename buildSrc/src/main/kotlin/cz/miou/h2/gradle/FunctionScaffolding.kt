package cz.miou.h2.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.support.uppercaseFirstChar

abstract class FunctionScaffolding : DefaultTask() {

    @get:Input
    abstract val functionSignature: Property<String>

    @get:Input
    abstract val packageName: Property<String>

    @TaskAction
    fun execute() {
        val functionSignature = functionSignature.get()
        val packageName = packageName.get()

        val regex = Regex("^(?<name>[A-Z][A-Z0-9_]*)\\((?<params>[a-zA-Z0-9-_]+(?:,\\s*[a-zA-Z0-9-_]+)*)?(?:,\\s*...)?\\)\$")
        val match = regex.matchEntire(functionSignature)!!

        val functionName = match.groups["name"]!!.value
        val functionNameUpper = functionName.uppercase()
        val functionNameLower = functionNameUpper.lowercase()
        val functionNameCamel = functionNameUpper.snakeToCamelCase().uppercaseFirstChar()
        val functionNamePascal = functionNameLower.snakeToCamelCase()

        val params = match.groups["params"]?.value ?: ""
        val arguments = params.split(",\\s*".toRegex())
            .filter { it.isNotEmpty() }
            .map { it.lowercase() }
        val parameters = arguments.joinToString(", ") { "String $it" }

        val classTemplate = """
            package ${packageName};
            
            import cz.miou.h2.api.FunctionDefinition;
            
            /**
             * <a href="https://mariadb.com/kb/en/${functionNameLower}/">${functionNameUpper}</a>
             */
            public class $functionNameCamel implements FunctionDefinition {
                
                @Override
                public String getName() {
                    return "$functionNameUpper";
                }
            
                @Override
                public String getMethodName() {
                    return "$functionNamePascal";
                }
            
                @Override
                public boolean isDeterministic() {
                    return true;
                }
                
                @SuppressWarnings("unused")
                public static String ${functionNamePascal}(${parameters}) {
                    // @todo implementation
                    return null;
                }
                
            }
        """.trimIndent()

        val placeholders = arguments.joinToString(", ") { "?" }
        val binds = if (arguments.size == 1) "st.setString(1, ${arguments.first()})"
            else "{\n${arguments.withIndex().joinToString("\n") { "                            st.setString(${it.index + 1}, ${it.value});" }}\n                        }"

        val testTemplate = """
            package ${packageName};
            
            import org.junit.jupiter.params.ParameterizedTest;
            import org.junit.jupiter.params.provider.CsvSource;

            import java.sql.SQLException;

            import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
            import static org.assertj.core.api.Assertions.assertThat;
            
            class ${functionNameCamel}Test {
                
                @ParameterizedTest
                @CsvSource({
                    // @todo test cases
                })
                void test${functionNameCamel}(${parameters}, String expected) throws SQLException {
                    verifyQuery(
                        "SELECT ${functionNameUpper}(${placeholders})",
                        st -> ${binds},
                        rs -> assertThat(rs.getString(1)).isEqualTo(expected)
                    );
                }
                
            }
        """.trimIndent()

        writeFile("${functionNameCamel}.java", "src/main/java", packageName, classTemplate)
        writeFile("${functionNameCamel}Test.java", "src/test/java", packageName, testTemplate)

        appendServiceLoader(packageName, functionNameCamel)
    }

    private fun writeFile(fileName: String, sourcePath: String, packageName: String, testTemplate: String) {
        val packagePath = packageName.replace(".", "/")
        val filePath = project.layout.projectDirectory.file("$sourcePath/$packagePath/$fileName").asFile

        if (filePath.exists()) {
            println("$fileName already exists")
        } else {
            println("Writing $fileName")
            filePath.parentFile.mkdirs()
            filePath.writeText(testTemplate)
        }
    }

    private fun appendServiceLoader(packageName: String, functionNameCamel: String) {
        val serviceLoaderPath = project.layout.projectDirectory.file("src/main/resources/META-INF/services/cz.miou.h2.api.FunctionDefinition").asFile
        serviceLoaderPath.parentFile.mkdirs()
        val classFullName = "${packageName}.${functionNameCamel}"
        if (!serviceLoaderPath.exists()) {
            serviceLoaderPath.createNewFile()
        }
        if (serviceLoaderPath.isFile && serviceLoaderPath.canRead() && serviceLoaderPath.canWrite() && !serviceLoaderPath.readText().contains(classFullName)) {
            println("Adding $classFullName to service loader cz.miou.h2.api.FunctionDefinition")
            serviceLoaderPath.appendText("\n$classFullName")
        } else {
            println("Service loader cz.miou.h2.api.FunctionDefinition already contains class $classFullName")
        }
    }

    private fun String.snakeToCamelCase(): String {
        val pattern = "_([a-z])".toRegex()
        return lowercase()
            .replace(pattern) { it.groupValues[1].uppercase() }
    }

}