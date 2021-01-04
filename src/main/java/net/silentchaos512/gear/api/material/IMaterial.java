package net.silentchaos512.gear.api.material;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.silentchaos512.gear.api.item.GearType;
import net.silentchaos512.gear.api.part.PartType;
import net.silentchaos512.gear.api.util.IGearComponent;
import net.silentchaos512.gear.api.util.StatGearKey;
import net.silentchaos512.gear.network.SyncMaterialCraftingItemsPacket;
import net.silentchaos512.gear.util.GearHelper;
import net.silentchaos512.lib.event.ClientTicks;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * A material used for crafting gear parts. Provides stats and traits for different part types.
 */
public interface IMaterial extends IGearComponent<IMaterialInstance> {
    /**
     * The name of the data pack this material is from. For mods, this is the name of the JAR file.
     * In userdev, it's "main".
     *
     * @return The data pack name
     */
    String getPackName();

    /**
     * Gets the material ID (eg "silentgear:iron")
     *
     * @return The material ID
     */
    ResourceLocation getId();

    /**
     * Gets the material serializer. Most materials should not require custom serializers.
     *
     * @return The serializer
     */
    IMaterialSerializer<?> getSerializer();

    /**
     * Gets the parent of the material. Child materials will inherit some properties of their
     * parent, such as stats and traits. This is useful for creating materials that vary only in
     * color (eg wood). Most materials should not have a parent. Methods will need to check this if
     * they need to inherit properties.
     *
     * @return The parent, or null if there is no parent
     */
    @Nullable
    IMaterial getParent();

    /**
     * Gets the parent material, wrapped in an optional. See {@link #getParent()} for more
     * information on parent materials.
     *
     * @return The parent in an optional, or an empty optional if there is no parent
     */
    default Optional<IMaterial> getParentOptional() {
        return Optional.ofNullable(getParent());
    }

    /**
     * Gets the categories this material belongs to.
     *
     * @return Collection of categories
     */
    Collection<IMaterialCategory> getCategories();

    /**
     * Gets the tier of the material. Currently, the tier never depends on the part type.
     *
     * @param partType The part type
     * @return The tier
     */
    int getTier(PartType partType);

    Optional<Ingredient> getPartSubstitute(PartType partType);

    boolean hasPartSubstitutes();

    boolean canSalvage();

    /**
     * Gets the part types this material supports. In general, a material will support a part type
     * if the type is present in the stats JSON object (even if the value is empty).
     *
     * @return Supported part types
     */
    Set<PartType> getPartTypes();

    /**
     * Determine if the material can be used to craft parts of the given type. This should include a
     * parent check.
     *
     * @param partType The part type
     * @return True if and only if crafting should be allowed
     */
    boolean allowedInPart(PartType partType);

    /**
     * Used to retain data on integrated server which is not sent on connect.
     *
     * @param oldMaterial The material object being overwritten
     */
    default void retainData(@Nullable IMaterial oldMaterial) {}

    Collection<StatGearKey> getStatKeys(PartType type);

    /**
     * Gets the rendering properties of the material, including textures and colors.
     *
     * @param gear     The gear item
     * @param partType The part type
     * @return Material display properties
     */
    @Deprecated
    IMaterialLayerList getMaterialDisplay(ItemStack gear, PartType partType);

    IFormattableTextComponent getDisplayName(PartType partType, ItemStack gear);

    default IFormattableTextComponent getDisplayName(PartType partType) {
        return getDisplayName(partType, ItemStack.EMPTY);
    }

    @Nullable
    IFormattableTextComponent getDisplayNamePrefix(ItemStack gear, PartType partType);

    default int getNameColor(PartType partType, ItemStack gear) {
        return getNameColor(partType, GearHelper.getType(gear, GearType.ALL));
    }

    int getNameColor(PartType partType, GearType gearType);

    default boolean isVisible(PartType partType) {
        return true;
    }

    /**
     * Get an {@code ItemStack} which matches the normal ingredient. The {@code ticks} parameter can
     * be used to cycle between possible matches.
     *
     * @param type  The part type
     * @param ticks Used to index into matching stacks. If on the client, {@link
     *              ClientTicks#totalTicks()} can be used. Zero will consistently return the first
     *              item in the matching stacks array.
     * @return An item matching the normal ingredient, or {@link ItemStack#EMPTY} if there are none
     */
    default ItemStack getDisplayItem(PartType type, int ticks) {
        ItemStack[] stacks = getIngredient().getMatchingStacks();
        if (stacks.length == 0) return ItemStack.EMPTY;
        return stacks[(ticks / 20) % stacks.length];
    }

    void updateIngredient(SyncMaterialCraftingItemsPacket msg);
}
