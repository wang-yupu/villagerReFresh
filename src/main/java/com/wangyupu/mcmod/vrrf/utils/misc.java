package com.wangyupu.mcmod.vrrf.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import java.util.UUID;

public class misc {
    public static int getPlayerPing() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() == null) return 0;

        PlayerListEntry playerEntry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
        return (playerEntry != null) ? playerEntry.getLatency() : 0;
    }
}
