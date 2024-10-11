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
}

kpublish {
    createPublication("intermediate")
}
