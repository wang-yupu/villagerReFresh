package com.wangyupu.mcmod.vrrf;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wangyupu.mcmod.vrrf.utils.command;
import com.wangyupu.mcmod.vrrf.utils.keybinding;

public class VillagerReFresh implements ModInitializer {
	public static final String MOD_ID = "villagerrefresh";

	public static boolean isRefresh = false;
	@Nullable
	public static Item currentWorkBlock = null;
	@Nullable
	public static ItemStack targetItem = null;

	public static final MinecraftClient client = MinecraftClient.getInstance();

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		command.initCommands();
		keybinding.initKeybinding();

		// 变量初始化
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			if (client.isInSingleplayer() || client.getNetworkHandler() == null) {
				isRefresh = false;
			}
		});

	}
}