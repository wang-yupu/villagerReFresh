package com.wangyupu.mcmod.vrrf.utils;

import com.wangyupu.mcmod.vrrf.VillagerReFresh;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import com.wangyupu.mcmod.vrrf.func.setupui.SetupScreen;

public class keybinding {
    public static final KeyBinding binding1 = KeyBindingHelper.registerKeyBinding(new KeyBinding("kb.vrrf.key.setup", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_H, "kb.vrrf.category.keys"));
    public static void initKeybinding(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (binding1.wasPressed()) {
                if (client.player != null) {
                    client.execute(() -> client.setScreen(new SetupScreen(VillagerReFresh.isRefresh)));
                }
            }
        });
    }
}
