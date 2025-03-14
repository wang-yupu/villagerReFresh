package com.wangyupu.mcmod.vrrf.utils;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.util.Hand;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class block {
    public static BlockState getFrontBlockState() {
        PlayerEntity player = MinecraftClient.getInstance().player;


        Direction facing = player.getHorizontalFacing();

        // 2. 计算前方方块的位置
        BlockPos frontPos = player.getBlockPos().offset(facing);

        // 3. 获取方块状态
        return player.getWorld().getBlockState(frontPos);
    }

    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static boolean startBreakingBlockInFront() throws ExecutionException, InterruptedException {
        if (client.player == null || client.interactionManager == null) {
            return false;
        }

        ClientPlayerEntity player = client.player;
        ClientPlayerInteractionManager interactionManager = client.interactionManager;
        World world = player.getWorld();

        // 获取正前方的方块
        BlockPos targetPos = getBlockInFront(player);

        if (world.isAir(targetPos)) {
            return false; // 没有方块
        }

        client.execute(()->{interactionManager.attackBlock(targetPos, Direction.UP);});

        while (!isAir(targetPos)) {
            client.execute(()->{interactionManager.updateBlockBreakingProgress(targetPos, Direction.UP);});
            Thread.sleep(50);
        }

        return true;
    }

    private static boolean isAir(BlockPos targetPos) throws ExecutionException, InterruptedException {
        CompletableFuture<Boolean> future = new CompletableFuture<Boolean>();
        client.execute(()->{
            future.complete(client.player.getWorld().isAir(targetPos));
        });
        return future.get();
    }

    public static void placeBlockInFront() {
        if (client.player == null || client.interactionManager == null) {
            return;
        }

        PlayerEntity player = client.player;
        World world = player.getWorld();
        BlockPos targetPos = getBlockInFront((ClientPlayerEntity) player); // 目标位置是玩家前方方块的上方
        Vec3d hitVec = Vec3d.ofCenter(targetPos);

        ItemStack heldItem = player.getMainHandStack();
        if (!(heldItem.getItem() instanceof BlockItem)) {
            return; // 手持物品不是方块
        }

        // 发送方块放置请求
        BlockHitResult hitResult = new BlockHitResult(hitVec, Direction.UP, targetPos, false);
        client.interactionManager.interactBlock((ClientPlayerEntity) player,Hand.MAIN_HAND,hitResult);
    }

    private static BlockPos getBlockInFront(ClientPlayerEntity player) {
        BlockPos playerPos = player.getBlockPos();
        Direction facing = player.getHorizontalFacing();
        return playerPos.offset(facing);
    }
}
