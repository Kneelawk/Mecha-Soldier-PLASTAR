import net.neoforged.moddevgradle.tasks.JarJar

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
    additionalRuntimeClasspath("de.javagl:jgltf-model:2.0.4")
}

neoForge {
    accessTransformers.from(project(":xplat").file("src/main/resources/common-at.cfg"))
}

tasks.named<JarJar>("jarJar") {
    configuration(project(":xplat").configurations["includeTransitive"])
}

// Hack to disable junit tests (which we don't use) in order to
// prevent the scanner from crashing due to it not respecting AWs
tasks.named<Test>("test") {
    exclude("**")
}
