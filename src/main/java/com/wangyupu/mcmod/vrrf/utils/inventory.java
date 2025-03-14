package com.wangyupu.mcmod.vrrf.utils;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.collection.DefaultedList;

public class inventory {
    public static ItemStack findBestTool(PlayerEntity player, BlockState blockState) {
        ItemStack bestTool = null;
        float bestSpeed = 1.0f; // 默认为1.0，表示没有工具时徒手挖掘速度

        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof ToolItem tool) { // 只检查工具
                float speed = tool.getMiningSpeedMultiplier(stack, blockState);
                if (speed > bestSpeed && tool.isSuitableFor(blockState)) {
                    bestSpeed = speed;
                    bestTool = stack;
                }
            }
        }
        return bestTool;
    }

    public static boolean moveBestToolToHotbar(BlockState blockState, int hotbarTarget) {
        if (hotbarTarget < 0 || hotbarTarget > 8) return false; // 确保目标槽位合法

        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {return false;}
        // 1. 查找最佳工具
        ItemStack bestTool = findBestTool(player, blockState);
        if (bestTool == null) return false;

        // 2. 获取玩家背包
        DefaultedList<ItemStack> inventory = player.getInventory().main;

        // 3. 检查工具是否已在 Hotbar
        int toolSlot = -1;
        for (int i = 0; i < 9; i++) { // 只检查 Hotbar 0~8
            if (ItemStack.areEqual(bestTool, inventory.get(i))) {
                toolSlot = i;
                break;
            }
        }

        if (toolSlot != -1) {
            // 3.1 工具已在 Hotbar，交换目标槽位的物品
            ItemStack targetStack = inventory.get(hotbarTarget);
            inventory.set(hotbarTarget, bestTool);
            inventory.set(toolSlot, targetStack);
        } else {
            // 3.2 工具不在 Hotbar，遍历背包寻找
            for (int i = 9; i < inventory.size(); i++) {
                if (ItemStack.areEqual(bestTool, inventory.get(i))) {
                    toolSlot = i;
                    break;
                }
            }
            if (toolSlot == -1) return false; // 工具不在背包中

            // 4. 移动工具到 Hotbar
            ItemStack hotbarStack = inventory.get(hotbarTarget);
            if (!hotbarStack.isEmpty()) {
                inventory.set(toolSlot, hotbarStack); // 交换物品
            } else {
                inventory.set(toolSlot, ItemStack.EMPTY);
            }
            inventory.set(hotbarTarget, bestTool);
        }

        // 5. 选中目标槽位
        player.getInventory().selectedSlot = hotbarTarget;

        return true;
    }

    public static void setHotbar(int hotbarTarget) {
        if (hotbarTarget < 0 || hotbarTarget > 8) return;
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {return;}
        player.getInventory().selectedSlot = hotbarTarget;
    }

    public static boolean moveItemToHotbar(Item targetItem, int hotbarTarget) {
        if (hotbarTarget < 0 || hotbarTarget > 8) return false; // 确保目标槽位合法
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {return false;}

        // 1. 获取玩家背包
        DefaultedList<ItemStack> inventory = player.getInventory().main;

        // 2. 查找物品是否已经在 Hotbar
        int itemSlot = -1;
        for (int i = 0; i < 9; i++) { // 只检查 Hotbar 0~8
            if (!inventory.get(i).isEmpty() && inventory.get(i).getItem() == targetItem) {
                itemSlot = i;
                break;
            }
        }

        if (itemSlot != -1) {
            // 3.1 物品已在 Hotbar，交换到指定目标槽位
            ItemStack targetStack = inventory.get(hotbarTarget);
            inventory.set(hotbarTarget, inventory.get(itemSlot));
            inventory.set(itemSlot, targetStack);
        } else {
            // 3.2 物品不在 Hotbar，遍历背包寻找
            for (int i = 9; i < inventory.size(); i++) {
                if (!inventory.get(i).isEmpty() && inventory.get(i).getItem() == targetItem) {
                    itemSlot = i;
                    break;
                }
            }
            if (itemSlot == -1) return false; // 没有找到目标物品

            // 4. 移动物品到 Hotbar
            ItemStack hotbarStack = inventory.get(hotbarTarget);
            if (!hotbarStack.isEmpty()) {
                inventory.set(itemSlot, hotbarStack); // 交换物品
            } else {
                inventory.set(itemSlot, ItemStack.EMPTY);
            }
            inventory.set(hotbarTarget, inventory.get(itemSlot));
        }

        // 5. 选中目标槽位
        player.getInventory().selectedSlot = hotbarTarget;

        return true;
    }
}


