plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
}

submodule {
    setRefmaps("plastar")
    val common_events_version: String by project
    xplatExternalDependency { "com.kneelawk.common-events:common-events-$it:$common_events_version" }
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
