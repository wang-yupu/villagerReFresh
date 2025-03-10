package com.wangyupu.mcmod.vrrf.utils;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;


public class keybinding {
    public static final KeyBinding binding1 = KeyBindingHelper.registerKeyBinding(new KeyBinding("kb.msrpp.key.lookItem", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_HOME, "kb.msrpp.category.msrpp"));
    public static void initKeybinding(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (binding1.wasPressed()) {
                if (client.player != null) {
                    // do something here...
                }
            }
        });
    }
}
