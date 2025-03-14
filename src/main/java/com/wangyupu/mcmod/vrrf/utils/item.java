package com.wangyupu.mcmod.vrrf.utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class item {
    public static ItemStack parseItemStack(String input) {
        if (input.isEmpty()) {
            return ItemStack.EMPTY;
        }

        try {
            // 分离物品 ID 和 NBT 数据
            String itemIdStr;
            String nbtStr = "";
            if (input.contains("{")) {
                itemIdStr = input.substring(0, input.indexOf("{"));
                nbtStr = input.substring(input.indexOf("{"));
            } else {
                itemIdStr = input;
            }

            // 创建 Identifier
            Identifier itemId = new Identifier(itemIdStr);
            Item item = Registries.ITEM.get(itemId);

            if (item == Items.AIR) {
                return ItemStack.EMPTY;
            }

            // 创建 ItemStack
            ItemStack stack = new ItemStack(item);

            // 解析 NBT 数据
            if (!nbtStr.isEmpty()) {
                NbtCompound nbt = StringNbtReader.parse(nbtStr);
                stack.setNbt(nbt);
            }

            return stack;
        } catch (Exception e) {
            return ItemStack.EMPTY; // 解析失败时返回空
        }
    }

    public static String getItemStackString(ItemStack itemStack) {
        Identifier itemId = Registries.ITEM.getId(itemStack.getItem());
        NbtCompound nbt = itemStack.getNbt();

        if (nbt != null && !nbt.isEmpty()) {
            return itemId.toString() + nbt.toString();
        } else {
            return itemId.toString();
        }
    }
}
