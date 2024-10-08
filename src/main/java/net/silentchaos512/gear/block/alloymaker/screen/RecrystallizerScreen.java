package net.silentchaos512.gear.block.alloymaker.screen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.silentchaos512.gear.SilentGear;
import net.silentchaos512.gear.block.alloymaker.AlloyMakerContainer;
import net.silentchaos512.gear.block.alloymaker.AlloyMakerScreen;

public class RecrystallizerScreen extends AlloyMakerScreen {
    public static final ResourceLocation TEXTURE = SilentGear.getId("textures/gui/recrystallizer.png");

    public RecrystallizerScreen(AlloyMakerContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE;
    }
}