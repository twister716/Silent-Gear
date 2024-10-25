package net.silentchaos512.gear.core;

import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.silentchaos512.gear.SilentGear;
import net.silentchaos512.gear.api.material.Material;
import net.silentchaos512.gear.api.property.HarvestTier;
import net.silentchaos512.gear.api.util.DataResource;
import net.silentchaos512.gear.setup.SgTags;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public enum BuiltinMaterials {
    WOOD("wood", "0", Tiers.WOOD),
    NETHERWOOD("netherwood", "0", Tiers.WOOD),
    BAMBOO("bamboo", "0", Tiers.WOOD),
    BONE("bone", "1", Tiers.STONE),
    STONE("stone", "1", Tiers.STONE),
    BASALT("basalt", "1", Tiers.STONE),
    BLACKSTONE("blackstone", "1", Tiers.STONE),
    END_STONE("end_stone", "1", Tiers.STONE),
    FLINT("flint", "1", Tiers.STONE),
    NETHERRACK("netherrack", "1", Tiers.STONE),
    OBSIDIAN("obsidian", "1", Tiers.STONE),
    SANDSTONE("sandstone", "1", Tiers.STONE),
    TERRACOTTA("terracotta", "1", Tiers.STONE),
    COPPER("copper", "1.5", Tiers.STONE, SgTags.Blocks.NEEDS_COPPER_TOOL),
    GOLD("gold", "1", Tiers.GOLD),
    IRON("iron", "2", Tiers.IRON),
    DIAMOND("diamond", "3", Tiers.DIAMOND),
    EMERALD("emerald", "2", Tiers.IRON),
    LAPIS_LAZULI("lapis_lazuli", "1.5", Tiers.STONE, SgTags.Blocks.NEEDS_COPPER_TOOL),
    QUARTZ("quartz", "2", Tiers.IRON),
    AMETHYST("amethyst", "1.5", Tiers.STONE, SgTags.Blocks.NEEDS_COPPER_TOOL),
    DIMERALD("dimerald", "3", Tiers.DIAMOND),
    BLAZE_GOLD("blaze_gold", "2", Tiers.IRON),
    BRONZE("bronze", "2", Tiers.IRON),
    HIGH_CARBON_STEEL("high_carbon_steel", "2", Tiers.IRON),
    CRIMSON_IRON("crimson_iron", "3", Tiers.DIAMOND),
    CRIMSON_STEEL("crimson_steel", "4", Tiers.NETHERITE),
    AZURE_SILVER("azure_silver", "3", Tiers.DIAMOND),
    AZURE_ELECTRUM("azure_electrum", "4", Tiers.NETHERITE),
    TYRIAN_STEEL("tyrian_steel", "4", Tiers.NETHERITE)
    ;

    public static final List<BuiltinMaterials> EXAMPLE_SUB_ITEM_MATERIALS = List.of(
            WOOD,
            NETHERWOOD,
            STONE,
            FLINT,
            COPPER,
            GOLD,
            IRON,
            DIAMOND,
            EMERALD,
            DIMERALD,
            BLAZE_GOLD,
            CRIMSON_IRON,
            CRIMSON_STEEL,
            AZURE_SILVER,
            AZURE_ELECTRUM,
            TYRIAN_STEEL
    );

    private final ResourceLocation id;
    private final DataResource<Material> material;
    private final HarvestTier harvestTier;
    private final TagKey<Block> equivalentIncorrectForToolTag;
    @Nullable private final TagKey<Block> additionalBlocksForTool;

    BuiltinMaterials(String path, String levelHint, Tier equivalentTier) {
        this(path, path, levelHint, equivalentTier);
    }

    BuiltinMaterials(String path, String levelHint, Tier equivalentTier, @Nullable TagKey<Block> additionalBlocksForTool) {
        this(path, path, levelHint, equivalentTier, additionalBlocksForTool);
    }

    BuiltinMaterials(String path, String harvestTierName, String levelHint, Tier equivalentTier) {
        this(path, harvestTierName, levelHint, equivalentTier, null);
    }

    BuiltinMaterials(String path, String harvestTierName, String levelHint, Tier equivalentTier, @Nullable TagKey<Block> additionalBlocksForTool) {
        this.id = SilentGear.getId(path);
        this.material = DataResource.material(this.id);
        this.harvestTier = HarvestTier.create(harvestTierName, levelHint);
        this.equivalentIncorrectForToolTag = equivalentTier.getIncorrectBlocksForDrops();
        this.additionalBlocksForTool = additionalBlocksForTool;
    }

    public DataResource<Material> getMaterial() {
        return material;
    }

    public HarvestTier getHarvestTier() {
        return harvestTier;
    }

    // Used by data generators
    public void generateTag(Function<TagKey<Block>, IntrinsicHolderTagsProvider.IntrinsicTagAppender<Block>> tagProvider) {
        var intrinsicTagAppender = tagProvider.apply(this.harvestTier.incorrectForTool());
        intrinsicTagAppender.addTag(this.equivalentIncorrectForToolTag);
        if (this.additionalBlocksForTool != null) {
            intrinsicTagAppender.remove(this.additionalBlocksForTool);
        }
    }
}
