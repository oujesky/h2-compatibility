plugins {
    id("h2-compatibility.library-conventions")
    id("h2-compatibility.publishing-conventions")
}

description = "H2 DB MariaDB compatibility"

dependencies {
    api(project(":h2-loader"))
    implementation(project(":h2-mariadb-crypto"))
    implementation(project(":h2-mariadb-functions"))
    implementation(project(":h2-mariadb-inet"))
    implementation(project(":h2-mariadb-json"))
    implementation(project(":h2-mariadb-uuid"))
}