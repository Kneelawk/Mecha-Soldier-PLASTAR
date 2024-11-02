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
    include(project(path = ":xplat", configuration = "includeTransitive"))
}

loom {
    accessWidenerPath = project(":xplat").file("src/main/resources/common-aw.accesswidener")
}

fabricApi.configureDataGeneration { 
    outputDirectory = project(":xplat").file("src/main/generated")
    addToResources = false
    createSourceSet = true
}

// Hack to disable junit tests (which we don't use) in order to
// prevent the scanner from crashing due to it not respecting AWs
tasks.named<Test>("test") {
    exclude("**")
}
