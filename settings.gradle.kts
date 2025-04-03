rootProject.name = "h2-compatibility"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            startParameter.projectProperties["h2.version"]?.let {
                logger.lifecycle("Overriding H2 version: {}", it)
                version("h2", it)
            }
        }
    }
}

include("h2-api")
include("h2-loader")
include("h2-test")
include("h2-mariadb-test")
include("h2-mariadb-functions")
include("h2-mariadb-inet")
include("h2-mariadb-crypto")
include("h2-mariadb-json")
include("h2-mariadb-uuid")
