package com.wangyupu.mcmod.vrrf.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.village.TradeOfferList;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import org.jetbrains.annotations.Nullable;


import java.util.List;

public class VillagerCheck {

    @Nullable
    public static TradeOfferList checkAndOpenVillagerTrade() throws InterruptedException {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || client.world == null)
            return null;

        ClientPlayerEntity player = client.player;
        ClientWorld world = client.world;

        BlockPos secondBlockPos = getSecondBlockPos(player);

        Entity entity = getVillagerOnBlock(world, secondBlockPos);

        TradeOfferList tradeList = null;
        if (entity instanceof VillagerEntity villager) {
            client.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.interact(villager, false, Hand.MAIN_HAND)); // 打开界面
                Thread.sleep(misc.getPlayerPing() + 30);
                if (client.currentScreen != null) {
                    if (client.currentScreen instanceof MerchantScreen) {
                        MerchantScreenHandler handler = ((MerchantScreen) client.currentScreen).getScreenHandler();
                        tradeList = handler.getRecipes();
                    }
                }
        } else {
            text.messageToPlayerAnyThread(text.formatModMessage("前方第二个方块上没有村民！"));
            return null;
        }
        return tradeList;
    }

    private static BlockPos getSecondBlockPos(ClientPlayerEntity player) {
        Vec3d eyePos = player.getEyePos();
        Vec3d lookVec = player.getRotationVec(1.0F);
        return BlockPos.ofFloored(eyePos.add(lookVec.multiply(2.0)));
    }

    private static Entity getVillagerOnBlock(ClientWorld world, BlockPos pos) {
        List<Entity> entities = world.getEntitiesByClass(
                Entity.class,
                new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1),
                e -> e instanceof VillagerEntity
        );
        return (entities.size() == 1) ? entities.get(0) : null;
    }
}
