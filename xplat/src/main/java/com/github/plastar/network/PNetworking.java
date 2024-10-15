package com.github.plastar.network;

import com.kneelawk.knet.api.KNetRegistrar;

public class PNetworking {
    public static void register(KNetRegistrar registrar) {
        registrar.register(PatternSyncPayload.CHANNEL);
    }
}
