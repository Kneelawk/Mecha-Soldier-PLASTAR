plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
}

submodule {
    setLibsDirectory()
    applyXplatConnection(":xplat")
    generateRuns()
}

kpublish {
    createPublication()
}

dependencies {
    val mod_menu_version: String by project
    modLocalRuntime("com.terraformersmc:modmenu:$mod_menu_version") {
        exclude(group = "net.fabricmc")
        exclude(group = "net.fabricmc.fabric-api")
    }
    // TODO: ask Kneelawk how to do this with submodule
    include("de.javagl:jgltf-model:2.0.4")
    include("de.javagl:jgltf-impl-v1:2.0.4")
    include("de.javagl:jgltf-impl-v2:2.0.4")
    include("com.fasterxml.jackson.core:jackson-core:2.13.1")
    include("com.fasterxml.jackson.core:jackson-databind:2.13.4.2")
    include("com.fasterxml.jackson.core:jackson-annotations:2.13.4")
}

loom {
    accessWidenerPath = project(":xplat").file("src/main/resources/common-aw.accesswidener")
}

fabricApi.configureDataGeneration { 
    outputDirectory = project(":xplat").file("src/main/generated")
    addToResources = false
    createSourceSet = true
}
