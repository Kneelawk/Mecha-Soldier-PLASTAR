plugins {
    id("fabric-loom") apply false
    id("com.kneelawk.submodule") apply false
}

tasks.create("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}

subprojects {
    repositories {
        maven("https://maven.ithundxr.dev/hidden") { name = "Flywheel Beta" }
    }
}
