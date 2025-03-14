package com.wangyupu.mcmod.vrrf.func.setupui;

import com.wangyupu.mcmod.vrrf.func.setupui.ItemSelectWizard;
import com.wangyupu.mcmod.vrrf.VillagerReFresh;

import com.wangyupu.mcmod.vrrf.utils.text;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.text.Text;
import net.minecraft.registry.Registries;

import net.minecraft.util.Identifier;
import com.wangyupu.mcmod.vrrf.utils.item;
import java.util.List;

public class SetupScreen extends Screen {
    private boolean isRunning = false;

    public SetupScreen(boolean run) {
        super(Text.literal("VRRF"));
        isRunning = run;
    }

    private TextFieldWidget itemRegName = null;
    private boolean SyncTextFieldContent = false;
    @Override
    protected void init() {

        int bgWidth = 247;
        int bgHeight = 166;
        int X = (this.width - bgWidth) / 2;
        int Y = (this.height - bgHeight) / 2;

        // 绘制
        this.itemRegName = new TextFieldWidget(this.textRenderer,X+5,Y+15,bgWidth-10,20,Text.literal("Item"));
        this.itemRegName.setMaxLength(1000);
        if (!this.SyncTextFieldContent) {
            if (VillagerReFresh.targetItem != null) {
                itemRegName.setText(item.getItemStackString(VillagerReFresh.targetItem));
            }
            this.SyncTextFieldContent = true;
        }

//        ButtonWidget wizard = ButtonWidget.builder(Text.of("Item Select Wizard"), (btn) -> {
//            if (this.client != null) {
//                client.setScreen(new ItemSelectWizard(Text.literal("isw"), this));
//            }
//        }).dimensions(X+5, Y+40, bgWidth-10, 20).build();

        ButtonWidget confirm = ButtonWidget.builder(Text.of("确认"), (btn) -> {
            if (this.client != null) {
                ItemStack stack = item.parseItemStack(this.itemRegName.getText());
                if (!stack.isEmpty()){
                    VillagerReFresh.targetItem = stack;
                    text.messageToPlayerAnyThread(text.formatModMessage("成功设置目标物品"));
                    this.close();
                }
            }
        }).dimensions(X+bgWidth-50-5, Y+bgHeight-25, 50, 20).build();

        ButtonWidget cancel = ButtonWidget.builder(Text.of("取消"), (btn) -> {
            if (this.client != null) {
                this.close();
            }
        }).dimensions(X+bgWidth-100-10, Y+bgHeight-25, 50, 20).build();

        this.addDrawableChild(itemRegName);
        // this.addDrawableChild(wizard);

        this.addDrawableChild(cancel);
        this.addDrawableChild(confirm);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.client.world != null) {
            this.renderInGameBackground(context);
        }

        int bgWidth = 247;
        int bgHeight = 166;
        int bgX = (this.width - bgWidth) / 2;
        int bgY = (this.height - bgHeight) / 2;

        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        Identifier texture = Identifier.tryParse("textures/gui/demo_background.png");
        context.drawTexture(texture, bgX, bgY, 1, 1, bgWidth, bgHeight);
        context.drawText(this.textRenderer, "设置 " + (this.isRunning ? "Running" : "Stopped"), bgX + 5, bgY + 25 - this.textRenderer.fontHeight - 10, 0xFFFFFFFF, true);
        context.drawText(this.textRenderer, "minecraft:enchanted_book{StoredEnchantments:[{id:\"<附魔ID>\",lvl:<等级>s}]}", bgX + 5, bgY + 40, 0xFFFFFFFF, true);

        // 获取输入的字符串
        String inputText = itemRegName.getText().trim();
        ItemStack stack = item.parseItemStack(inputText);

        if (stack.isEmpty()) {
            context.drawText(this.textRenderer, "找不到物品 / 不合法", bgX + 5, bgY + 60, 0xFF0000, true);

        } else {
            context.drawText(this.textRenderer, "匹配", bgX + 5, bgY + 60, 0x00FF00, true);
            context.drawItemTooltip(this.textRenderer, stack,bgX-3,bgY+105);
            context.drawItem(stack, bgX+5, bgY+70);
        }

        super.render(context, mouseX, mouseY, delta);
    }


    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta){}

    @Override
    public boolean shouldPause(){
        return false;
    }
}
