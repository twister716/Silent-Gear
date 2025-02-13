package net.silentchaos512.gear.item.blueprint;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.silentchaos512.gear.Config;

import java.util.List;
import java.util.Locale;

public abstract class AbstractBlueprintItem extends Item implements IBlueprint {
    final BlueprintType type;

    AbstractBlueprintItem(Properties properties, BlueprintType type) {
        super(properties);
        this.type = type;
        if (this.type == BlueprintType.BLUEPRINT) {
            properties.component(DataComponents.RARITY, Rarity.UNCOMMON);
        }
    }

    public abstract TagKey<Item> getItemTag();

    protected abstract Component getCraftedName(ItemStack stack);

    public boolean isSingleUse() {
        return this.type == BlueprintType.TEMPLATE;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        ItemStack copy = itemStack.copy();
        copy.setCount(1);
        return copy;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return this.type == BlueprintType.BLUEPRINT;
    }

    boolean isDisabled() {
        BlueprintType.ConfigOption config = Config.Common.blueprintTypes.get();
        return this.type == BlueprintType.TEMPLATE && !config.allowTemplate()
                || this.type == BlueprintType.BLUEPRINT && !config.allowBlueprint();
    }

    @Override
    public Component getName(ItemStack stack) {
        String key = "item.silentgear." + this.type.name().toLowerCase(Locale.ROOT);
        return Component.translatable(key, this.getCraftedName(stack));
    }

    public boolean hasStandardModel() {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flags) {
    }
}
