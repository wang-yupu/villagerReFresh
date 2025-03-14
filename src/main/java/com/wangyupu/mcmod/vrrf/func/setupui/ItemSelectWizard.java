package com.wangyupu.mcmod.vrrf.func.setupui;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;

import net.minecraft.util.Identifier;


public class ItemSelectWizard extends Screen {
    private final Screen parent;

    public ItemSelectWizard(Text title,Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected void init() {

        int bgWidth = 247;
        int bgHeight = 166;
        int X = (this.width - bgWidth) / 2;
        int Y = (this.height - bgHeight) / 2;

        // 绘制
        TextFieldWidget itemRegName = new TextFieldWidget(this.textRenderer,X+5,Y+15,bgWidth-10,20,Text.literal("Item"));

        ButtonWidget confirm = ButtonWidget.builder(Text.of("Confirm"), (btn) -> {
            if (this.client != null) {
                this.close();
            }
        }).dimensions(X+bgWidth-50-5, Y+bgHeight-25, 50, 20).build();

        ButtonWidget cancel = ButtonWidget.builder(Text.of("Cancel"), (btn) -> {
            if (this.client != null) {
                this.close();
            }
        }).dimensions(X+bgWidth-100-10, Y+bgHeight-25, 50, 20).build();

        this.addDrawableChild(itemRegName);
        this.addDrawableChild(confirm);
        this.addDrawableChild(cancel);
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
        context.drawTexture(texture,bgX,bgY,1,1,bgWidth,bgHeight);
        context.drawText(this.textRenderer, "Item Choose Wizard", bgX+5, bgY+25 - this.textRenderer.fontHeight - 10, 0xFFFFFFFF, true);
        super.render(context,mouseX,mouseY,delta);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta){}

    @Override
    public boolean shouldPause(){
        return false;
    }

    @Override
    public void close() {
        if (client != null) {
            client.setScreen(parent);
        }
    }
}
