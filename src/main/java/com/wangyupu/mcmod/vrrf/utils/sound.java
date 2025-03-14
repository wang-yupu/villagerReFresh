package com.wangyupu.mcmod.vrrf.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;


import net.minecraft.util.math.random.Random;

public class sound {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    public static void playVanillaSound(SoundEvent soundEventObj, float pitch){
        if (client.world != null) {
            PositionedSoundInstance soundInstance = new PositionedSoundInstance(soundEventObj,SoundCategory.MASTER,1f,pitch,Random.create(0),0,0,0);
            client.getSoundManager().play(soundInstance);
        }
    }

    public static void playVanillaSoundAnyThread(SoundEvent soundEventObj, float pitch) {
        client.execute(() -> {playVanillaSound(soundEventObj, pitch);});
    }
}
