package com.github.plastar.platform;

import java.util.ServiceLoader;

public interface Platform {
    Platform INSTANCE = ServiceLoader.load(Platform.class).findFirst()
        .orElseThrow(() -> new RuntimeException("No PLASTAR platform provided"));
}
