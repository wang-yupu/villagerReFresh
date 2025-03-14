package com.wangyupu.mcmod.vrrf.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class text {
    public static Text formatModMessage(String rawMessage){
        MutableText part0 = Text.literal("[")
                .formatted(Formatting.WHITE, Formatting.BOLD);
        MutableText part1 = Text.literal("VillagerReFresh")
                .formatted(Formatting.AQUA);
        MutableText part2 = Text.literal("] ")
                .formatted(Formatting.WHITE, Formatting.BOLD);
        MutableText part3 = Text.literal(rawMessage)
                .formatted(Formatting.RESET,Formatting.GRAY);


        return part0.append(part1).append(part2).append(part3);
    };

    private static final MinecraftClient client = MinecraftClient.getInstance();
    public static void messageToPlayer(Text text){
        if (client.player != null) {
            client.player.sendMessage(text, false);
        }
    };

    public static void messageToPlayerAnyThread(Text text){
        client.execute(()->{messageToPlayer(text);});
    }

    public static void printHelpInformation(){
        MutableText part0 = (MutableText) formatModMessage("");
        MutableText part1 = Text.translatable("message.vrrf.helpTitle");
        MutableText part2 = Text.translatable("message.vrrf.helpContent");

        MutableText partVersionPrefix = Text.translatable("message.vrrf.versionPrefix");
        MutableText partVersion = Text.translatable("message.vrrf.version");
        MutableText all = part0.append(part1).append("\n").append(part2).append("\n").append(partVersionPrefix).append(partVersion);

        messageToPlayer(all);
    }
}
