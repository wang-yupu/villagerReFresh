package com.wangyupu.mcmod.vrrf.func.main;

import com.wangyupu.mcmod.vrrf.VillagerReFresh;
import com.wangyupu.mcmod.vrrf.utils.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MainFunc {

    @Nullable
    private static Thread loopThreadThread = null;

    public static void start(){
        text.messageToPlayerAnyThread(text.formatModMessage("开始刷新物品"));

        VillagerReFresh.isRefresh = true;
        // 第一步：获取前方村民工作方块并记录
        VillagerReFresh.currentWorkBlock = Item.BLOCK_ITEMS.get(block.getFrontBlockState().getBlock());

        loopThreadThread = new Thread(MainFunc::loopThread);
        loopThreadThread.setDaemon(true);
        loopThreadThread.start();
    }

    public static void stop() {
        if (loopThreadThread != null) {
            loopThreadThread.interrupt();
            VillagerReFresh.isRefresh = false;
            text.messageToPlayerAnyThread(text.formatModMessage("结束刷新物品"));
        }
    }

    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static void loopThread() {
        // 开始处理
        while (!Thread.currentThread().isInterrupted()){
            try {
                Thread.sleep(50);
                if (GLFW.glfwGetKey(client.getWindow().getHandle(), GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
                    stop();
                    break;
                }
                EqualsState equals = EqualsState.EmptyList;
                try {
                    equals = tradeEqualsTarget();
                } catch (Exception e) {
                    text.messageToPlayerAnyThread(text.formatModMessage(e.toString()));
                    stop();
                    break;
                }
                if (equals == EqualsState.Equals) {
                    text.messageToPlayerAnyThread(text.formatModMessage("刷到目标物品了！"));
                    sound.playVanillaSoundAnyThread(SoundEvents.BLOCK_ANVIL_PLACE, 1);
                    stop();
                    break;
                } else if (equals == EqualsState.EmptyList) {
                    continue;
                } else {
                    CompletableFuture<Boolean> workBlockFound = new CompletableFuture<Boolean>();
                    client.execute(()->{
                        inventory.moveBestToolToHotbar(block.getFrontBlockState(),0);
                        boolean workBlockFoundState = inventory.moveItemToHotbar(VillagerReFresh.currentWorkBlock,1);

                        inventory.setHotbar(0);
                        workBlockFound.complete(workBlockFoundState);
                    });
                    if (!workBlockFound.get()){
                        text.messageToPlayerAnyThread(text.formatModMessage("找不到村民的工作方块"));
                        stop();
                        break;
                    }
                    block.startBreakingBlockInFront();

                    // 等村民职业消失
                    EqualsState exists = EqualsState.Equals;
                    while (!(exists == EqualsState.EmptyList)) {
                        exists = tradeEqualsTarget();
                        Thread.sleep(40);
                    }

                    client.execute(()->{
                        inventory.setHotbar(1);
                        block.placeBlockInFront();
                    });

                    Thread.sleep(20);
                }
            } catch (InterruptedException e) {
                text.messageToPlayerAnyThread(text.formatModMessage("停止！"));
                break;
            } catch (ExecutionException e) {
                text.messageToPlayerAnyThread(text.formatModMessage("出现内部错误"));
                break;
            }
        }
    }

    public enum EqualsState {
        Equals,EmptyList,NotSetItem,NotEquals
    }

    public static EqualsState tradeEqualsTarget() throws InterruptedException {
        if (VillagerReFresh.targetItem == null) {
            return EqualsState.NotSetItem;
        }
        TradeOfferList tradeList = VillagerCheck.checkAndOpenVillagerTrade();
        if (tradeList == null) {
            closeScreen();
            return EqualsState.EmptyList;
        }

        for (TradeOffer offer : tradeList) {
            ItemStack sellItem = offer.getSellItem();
            sellItem.setCount(1);
            VillagerReFresh.LOGGER.info(item.getItemStackString(sellItem));
            if (sellItem.equals(VillagerReFresh.targetItem) || Objects.equals(item.getItemStackString(sellItem), item.getItemStackString(VillagerReFresh.targetItem))){
                closeScreen();
                return EqualsState.Equals;
            };
        }
        closeScreen();
        return EqualsState.NotEquals;
    }

    private static void closeScreen() {
        client.execute(()->{
            if (client.currentScreen != null) {
                client.currentScreen.close();
            }
        });
    }

}
