package com.wangyupu.mcmod.vrrf.utils;

import com.wangyupu.mcmod.vrrf.func.main.MainFunc;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

import com.wangyupu.mcmod.vrrf.utils.text;
import com.wangyupu.mcmod.vrrf.func.setupui.SetupScreen;
import com.wangyupu.mcmod.vrrf.VillagerReFresh;
import com.wangyupu.mcmod.vrrf.utils.VillagerCheck;
import com.wangyupu.mcmod.vrrf.utils.inventory;
import com.wangyupu.mcmod.vrrf.utils.block;

public class command {
    public static void initCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                    literal("vrrf")
                            .executes(context -> {
                                text.printHelpInformation();
                                return 1;
                            })
                            .then(literal("start").executes(context -> {
                                MainFunc.start();
                                return 1;
                            }))
                            .then(literal("stop").executes(context -> {
                                MainFunc.stop();
                                return 1;
                            }))
                            .then(literal("setup").executes(context -> {
                                MinecraftClient client = MinecraftClient.getInstance();
                                client.execute(() -> client.setScreen(new SetupScreen(VillagerReFresh.isRefresh)));
                                return 1;
                            }))
            );
        });
    }

    private static void sendClientMessage(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player != null) {
            client.player.sendMessage(Text.literal(message), false);
        }
    }
}
