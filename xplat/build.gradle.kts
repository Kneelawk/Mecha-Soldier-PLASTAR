plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
}

submodule {
    setRefmaps("plastar")
    val knet_version: String by project
    xplatExternalDependency { "com.kneelawk.knet:knet-$it:$knet_version" }
    val flywheel_version: String by project
    val flywheel_mc_version: String by project
    xplatExternalDependency {
        val platform = when (it) {
            "neoforge" -> "forge" // TODO: remove this line once flywheel fixes their neoforge naming
            "xplat-intermediary" -> "common-intermediary-api"
            else -> it
        }
        "dev.engine_room.flywheel:flywheel-$platform-$flywheel_mc_version:$flywheel_version"
    }
    xplatExternalDependency { "de.javagl:jgltf-model:2.0.4" }
}

kpublish {
    createPublication("intermediate")
}

loom {
    accessWidenerPath.set(file("src/main/resources/common-aw.accesswidener"))
}

sourceSets.main {
    resources.srcDir(project(":xplat").file("src/main/generated"))
}
