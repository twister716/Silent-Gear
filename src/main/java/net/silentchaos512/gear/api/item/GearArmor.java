package net.silentchaos512.gear.api.item;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.silentchaos512.gear.api.part.PartType;
import net.silentchaos512.gear.client.util.ColorUtils;
import net.silentchaos512.gear.gear.part.PartInstance;
import net.silentchaos512.gear.setup.gear.PartTypes;

public interface GearArmor extends GearItem {
    @Override
    default boolean supportsPart(ItemStack gear, PartInstance part) {
        PartType type = part.getType();
        boolean supported = GearItem.super.supportsPart(gear, part);
        return (type == PartTypes.MAIN.get() && supported)
                || type == PartTypes.TIP.get()
                || type == PartTypes.LINING.get()
                || supported;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    default ItemColor getItemColors() {
//        return (stack, tintIndex) -> Color.VALUE_WHITE;
        //noinspection OverlyLongLambda
        return (stack, tintIndex) -> {
            return switch (tintIndex) {
                case 0 -> ColorUtils.getBlendedColorForPartInGear(stack, PartTypes.MAIN.get());
                default -> 0xFFFFFFFF;
            };
        };
    }
}
