package net.silentchaos512.gear.block.salvager;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.silentchaos512.gear.SilentGear;
import net.silentchaos512.gear.setup.SgBlocks;

public class SalvagerScreen extends AbstractContainerScreen<SalvagerContainer> {
    public static final ResourceLocation TEXTURE = SilentGear.getId("textures/gui/salvager.png");

    public SalvagerScreen(SalvagerContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int x, int y) {
        MutableComponent text = SgBlocks.SALVAGER.get().getName();
        graphics.drawString(this.font, text.getString(), 28, 6, 0x404040, false);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        if (minecraft == null) return;

        RenderSystem.clearColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int posX = (this.width - this.imageWidth) / 2;
        int posY = (this.height - this.imageHeight) / 2;
        graphics.blit(TEXTURE, posX, posY, 0, 0, this.imageWidth, this.imageHeight);

        // Progress arrow
        graphics.blit(TEXTURE, posX + 32, posY + 34, 176, 14, menu.getProgressArrowScale() + 1, 16);
    }
}
