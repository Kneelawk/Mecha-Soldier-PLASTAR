plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
}

configurations {
    register("neoRuntimeLibs")
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
    // TODO: ask Kneelawk how to do this with submodule
    additionalRuntimeClasspath("de.javagl:jgltf-model:2.0.4")
    jarJar("de.javagl:jgltf-model:2.0.4")
    jarJar("de.javagl:jgltf-impl-v1:2.0.4")
    jarJar("de.javagl:jgltf-impl-v2:2.0.4")
    jarJar("com.fasterxml.jackson.core:jackson-core:2.13.1")
    jarJar("com.fasterxml.jackson.core:jackson-databind:2.13.4.2")
    jarJar("com.fasterxml.jackson.core:jackson-annotations:2.13.4")
}
