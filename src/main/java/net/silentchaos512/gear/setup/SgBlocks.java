package net.silentchaos512.gear.setup;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.gear.SilentGear;
import net.silentchaos512.gear.block.*;
import net.silentchaos512.gear.block.charger.ChargerTileEntity;
import net.silentchaos512.gear.block.charger.StarlightChargerBlock;
import net.silentchaos512.gear.block.compounder.CompounderBlock;
import net.silentchaos512.gear.block.grader.GraderBlock;
import net.silentchaos512.gear.block.press.MetalPressBlock;
import net.silentchaos512.gear.block.salvager.SalvagerBlock;
import net.silentchaos512.gear.config.Config;
import net.silentchaos512.gear.crafting.recipe.compounder.FabricCompoundingRecipe;
import net.silentchaos512.gear.crafting.recipe.compounder.GemCompoundingRecipe;
import net.silentchaos512.gear.crafting.recipe.compounder.MetalCompoundingRecipe;
import net.silentchaos512.gear.util.Const;
import net.silentchaos512.lib.util.NameUtils;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = SilentGear.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class SgBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SilentGear.MOD_ID);

    private static final Map<Block, Block> STRIPPED_WOOD = new HashMap<>();

    public static final DeferredBlock<DropExperienceBlock> BORT_ORE = register("bort_ore", () ->
            getOre(SoundType.STONE));
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_BORT_ORE = register("deepslate_bort_ore", () ->
            getOre(SoundType.STONE));
    public static final DeferredBlock<DropExperienceBlock> CRIMSON_IRON_ORE = register("crimson_iron_ore", () ->
            getOre(SoundType.NETHER_GOLD_ORE));
    public static final DeferredBlock<DropExperienceBlock> BLACKSTONE_CRIMSON_IRON_ORE = register("blackstone_crimson_iron_ore", () ->
            getOre(SoundType.GILDED_BLACKSTONE));
    public static final DeferredBlock<DropExperienceBlock> AZURE_SILVER_ORE = register("azure_silver_ore", () ->
            getOre(SoundType.STONE));

    public static final DeferredBlock<Block> RAW_CRIMSON_IRON_BLOCK = register("raw_crimson_iron_block", () ->
            getRawOreBlock(SoundType.NETHER_GOLD_ORE));
    public static final DeferredBlock<Block> RAW_AZURE_SILVER_BLOCK = register("raw_azure_silver_block", () ->
            getRawOreBlock(SoundType.STONE));

    public static final DeferredBlock<Block> BORT_BLOCK = register("bort_block",
            SgBlocks::getStorageBlock);
    public static final DeferredBlock<Block> CRIMSON_IRON_BLOCK = register("crimson_iron_block",
            SgBlocks::getStorageBlock);
    public static final DeferredBlock<Block> CRIMSON_STEEL_BLOCK = register("crimson_steel_block",
            SgBlocks::getStorageBlock);
    public static final DeferredBlock<Block> BLAZE_GOLD_BLOCK = register("blaze_gold_block",
            SgBlocks::getStorageBlock);
    public static final DeferredBlock<Block> AZURE_SILVER_BLOCK = register("azure_silver_block",
            SgBlocks::getStorageBlock);
    public static final DeferredBlock<Block> AZURE_ELECTRUM_BLOCK = register("azure_electrum_block",
            SgBlocks::getStorageBlock);
    public static final DeferredBlock<Block> TYRIAN_STEEL_BLOCK = register("tyrian_steel_block",
            SgBlocks::getStorageBlock);

    public static final DeferredBlock<Block> GEAR_SMITHING_TABLE = register("gear_smithing_table", () ->
            new GearSmithingTableBlock(BlockBehaviour.Properties.of()
                    .strength(2.5F)
                    .sound(SoundType.WOOD)));

    public static final DeferredBlock<GraderBlock> MATERIAL_GRADER = register("material_grader", () ->
            new GraderBlock(BlockBehaviour.Properties.of()
                    .strength(5, 30)));

    public static final DeferredBlock<SalvagerBlock> SALVAGER = register("salvager", () ->
            new SalvagerBlock(BlockBehaviour.Properties.of()
                    .strength(5, 30)));

    public static final DeferredBlock<StarlightChargerBlock> STARLIGHT_CHARGER = register("starlight_charger", () ->
            new StarlightChargerBlock(ChargerTileEntity::createStarlightCharger,
                    BlockBehaviour.Properties.of()
                            .strength(5, 30)));

    public static final DeferredBlock<CompounderBlock<MetalCompoundingRecipe>> METAL_ALLOYER = register("metal_alloyer", () ->
            new CompounderBlock<>(Const.METAL_COMPOUNDER_INFO,
                    BlockBehaviour.Properties.of()
                            .strength(4, 20)
                            .sound(SoundType.METAL)));

    public static final DeferredBlock<CompounderBlock<GemCompoundingRecipe>> RECRYSTALLIZER = register("recrystallizer", () ->
            new CompounderBlock<>(Const.GEM_COMPOUNDER_INFO,
                    BlockBehaviour.Properties.of()
                            .strength(4, 20)
                            .sound(SoundType.METAL)));

    public static final DeferredBlock<CompounderBlock<FabricCompoundingRecipe>> REFABRICATOR = register("refabricator", () ->
            new CompounderBlock<>(Const.FABRIC_COMPOUNDER_INFO,
                    BlockBehaviour.Properties.of()
                            .strength(4, 20)
                            .sound(SoundType.METAL)));

    public static final DeferredBlock<MetalPressBlock> METAL_PRESS = register("metal_press", () ->
            new MetalPressBlock(BlockBehaviour.Properties.of()
                    .strength(4, 20)
                    .sound(SoundType.METAL)));

    public static final DeferredBlock<ModCropBlock> FLAX_PLANT = registerNoItem("flax_plant", () ->
            new ModCropBlock(SgItems.FLAX_SEEDS::get, BlockBehaviour.Properties.of()
                    .strength(0)
                    .noCollission()
                    .randomTicks()
                    .sound(SoundType.CROP)));
    public static final DeferredBlock<BushBlock> WILD_FLAX_PLANT = registerNoItem("wild_flax_plant", () ->
            new BushBlock(BlockBehaviour.Properties.of()
                    .strength(0)
                    .noCollission()
                    .sound(SoundType.CROP)));
    public static final DeferredBlock<ModCropBlock> FLUFFY_PLANT = registerNoItem("fluffy_plant", () ->
            new ModCropBlock(SgItems.FLUFFY_SEEDS::get, BlockBehaviour.Properties.of()
                    .strength(0)
                    .noCollission()
                    .randomTicks()
                    .sound(SoundType.CROP)));
    public static final DeferredBlock<BushBlock> WILD_FLUFFY_PLANT = registerNoItem("wild_fluffy_plant", () ->
            new BushBlock(BlockBehaviour.Properties.of()
                    .strength(0)
                    .noCollission()
                    .sound(SoundType.CROP)));

    public static final DeferredBlock<FluffyBlock> WHITE_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.WHITE);
    public static final DeferredBlock<FluffyBlock> ORANGE_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.ORANGE);
    public static final DeferredBlock<FluffyBlock> MAGENTA_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.MAGENTA);
    public static final DeferredBlock<FluffyBlock> LIGHT_BLUE_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.LIGHT_BLUE);
    public static final DeferredBlock<FluffyBlock> YELLOW_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.YELLOW);
    public static final DeferredBlock<FluffyBlock> LIME_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.LIME);
    public static final DeferredBlock<FluffyBlock> PINK_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.PINK);
    public static final DeferredBlock<FluffyBlock> GRAY_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.GRAY);
    public static final DeferredBlock<FluffyBlock> LIGHT_GRAY_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.LIGHT_GRAY);
    public static final DeferredBlock<FluffyBlock> CYAN_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.CYAN);
    public static final DeferredBlock<FluffyBlock> PURPLE_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.PURPLE);
    public static final DeferredBlock<FluffyBlock> BLUE_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.BLUE);
    public static final DeferredBlock<FluffyBlock> BROWN_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.BROWN);
    public static final DeferredBlock<FluffyBlock> GREEN_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.GREEN);
    public static final DeferredBlock<FluffyBlock> RED_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.RED);
    public static final DeferredBlock<FluffyBlock> BLACK_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.BLACK);

    public static final DeferredBlock<TorchBlock> STONE_TORCH = register("stone_torch",
            () -> new TorchBlock(BlockBehaviour.Properties.of()
                    .noCollission()
                    .strength(0)
                    .lightLevel(state -> 14)
                    .sound(SoundType.STONE),
                    ParticleTypes.FLAME),
            bro -> getStoneTorchItem());
    public static final DeferredBlock<WallTorchBlock> WALL_STONE_TORCH = registerNoItem("wall_stone_torch", () ->
            new WallTorchBlock(BlockBehaviour.Properties.of()
                    .noCollission()
                    .strength(0)
                    .lightLevel(state -> 14)
                    .sound(SoundType.STONE)
                    .lootFrom(STONE_TORCH::get),
                    ParticleTypes.FLAME));

    public static final DeferredBlock<Block> NETHERWOOD_CHARCOAL_BLOCK = register("netherwood_charcoal_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(5, 6)),
            bro -> () -> new BlockItem(bro.get(), new Item.Properties()) {
                @Override
                public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                    return 10 * Config.Common.netherwoodCharcoalBurnTime.get();
                }
            });

    public static final DeferredBlock<WoodBlock> NETHERWOOD_LOG = register("netherwood_log", () ->
            new WoodBlock(STRIPPED_WOOD::get, netherWoodProps(2f, 2f)));
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_NETHERWOOD_LOG = register("stripped_netherwood_log", () ->
            new RotatedPillarBlock(netherWoodProps(2f, 2f)));
    public static final DeferredBlock<WoodBlock> NETHERWOOD_WOOD = register("netherwood_wood", () ->
            new WoodBlock(STRIPPED_WOOD::get, netherWoodProps(2f, 2f)));
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_NETHERWOOD_WOOD = register("stripped_netherwood_wood", () ->
            new RotatedPillarBlock(netherWoodProps(2f, 2f)));

    public static final DeferredBlock<Block> NETHERWOOD_PLANKS = register("netherwood_planks", () ->
            new Block(netherWoodProps(2f, 3f)));
    public static final DeferredBlock<SlabBlock> NETHERWOOD_SLAB = register("netherwood_slab", () ->
            new SlabBlock(netherWoodProps(2f, 3f)));
    public static final DeferredBlock<StairBlock> NETHERWOOD_STAIRS = register("netherwood_stairs", () ->
            new StairBlock(NETHERWOOD_PLANKS::asBlockState, netherWoodProps(2f, 3f)));
    public static final DeferredBlock<FenceBlock> NETHERWOOD_FENCE = register("netherwood_fence", () ->
            new FenceBlock(netherWoodProps(2f, 3f)));
    public static final DeferredBlock<FenceGateBlock> NETHERWOOD_FENCE_GATE = register("netherwood_fence_gate", () ->
            new FenceGateBlock(netherWoodProps(2f, 3f), SoundEvents.NETHER_WOOD_FENCE_GATE_CLOSE, SoundEvents.NETHER_WOOD_FENCE_GATE_OPEN));
    public static final DeferredBlock<DoorBlock> NETHERWOOD_DOOR = register("netherwood_door", () ->
            new DoorBlock(netherWoodProps(3f, 3f).noOcclusion(), BlockSetType.CRIMSON));
    public static final DeferredBlock<TrapDoorBlock> NETHERWOOD_TRAPDOOR = register("netherwood_trapdoor", () ->
            new TrapDoorBlock(netherWoodProps(3f, 3f).noOcclusion(), BlockSetType.CRIMSON));
    public static final DeferredBlock<LeavesBlock> NETHERWOOD_LEAVES = register("netherwood_leaves", () ->
            new LeavesBlock(BlockBehaviour.Properties.of()
                    .strength(0.2f)
                    .randomTicks()
                    .noOcclusion()
                    .sound(SoundType.GRASS)));
    public static final DeferredBlock<NetherwoodSapling> NETHERWOOD_SAPLING = register("netherwood_sapling", () ->
            new NetherwoodSapling(BlockBehaviour.Properties.of()
                    .strength(0)
                    .noCollission()
                    .randomTicks()
                    .sound(SoundType.GRASS)));

    public static final DeferredBlock<FlowerPotBlock> POTTED_NETHERWOOD_SAPLING = registerNoItem("potted_netherwood_sapling", () ->
            makePottedPlant(NETHERWOOD_SAPLING));
    public static final DeferredBlock<PhantomLight> PHANTOM_LIGHT = register("phantom_light",
            PhantomLight::new);

    private SgBlocks() {
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        STRIPPED_WOOD.put(NETHERWOOD_LOG.get(), STRIPPED_NETHERWOOD_LOG.get());
        STRIPPED_WOOD.put(NETHERWOOD_WOOD.get(), STRIPPED_NETHERWOOD_WOOD.get());
    }

    private static DropExperienceBlock getOre(SoundType soundType) {
        return new ModOreBlock(BlockBehaviour.Properties.of()
                .strength(4, 10)
                .requiresCorrectToolForDrops()
                .sound(soundType));
    }

    private static Block getRawOreBlock(SoundType soundType) {
        return new ModOreBlock(BlockBehaviour.Properties.of()
                .strength(4, 20)
                .requiresCorrectToolForDrops()
                .sound(soundType));
    }

    private static Block getStorageBlock() {
        return new Block(BlockBehaviour.Properties.of()
                .strength(3, 6)
                .sound(SoundType.METAL));
    }

    private static <T extends Block> DeferredBlock<T> registerNoItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> DeferredBlock<T> register(String name, Supplier<T> block) {
        return register(name, block, SgBlocks::defaultItem);
    }

    private static <T extends Block> DeferredBlock<T> register(String name, Supplier<T> block, Function<DeferredBlock<T>, Supplier<? extends BlockItem>> item) {
        DeferredBlock<T> ret = registerNoItem(name, block);
        SgItems.ITEMS.register(name, item.apply(ret));
        return ret;
    }

    private static DeferredBlock<FluffyBlock> registerFluffyBlock(DyeColor color) {
        return register(color.getName() + "_fluffy_block", () -> new FluffyBlock(color));
    }

    private static <T extends Block> Supplier<BlockItem> defaultItem(DeferredBlock<T> block) {
        return () -> new BlockItem(block.get(), new Item.Properties());
    }

    private static Supplier<BlockItem> getStoneTorchItem() {
        return () -> new StandingAndWallBlockItem(STONE_TORCH.get(), WALL_STONE_TORCH.get(), new Item.Properties(), Direction.DOWN);
    }

    @SuppressWarnings("SameParameterValue")
    private static FlowerPotBlock makePottedPlant(Supplier<? extends Block> flower) {
        FlowerPotBlock potted = new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, flower, Block.Properties.of().strength(0));
        ResourceLocation flowerId = NameUtils.fromBlock(flower.get());
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(flowerId, () -> potted);
        return potted;
    }

    private static BlockBehaviour.Properties netherWoodProps(float hardnessIn, float resistanceIn) {
        return BlockBehaviour.Properties.of()
                .strength(hardnessIn, resistanceIn)
                .sound(SoundType.WOOD);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Block> Collection<T> getBlocks(Class<T> clazz) {
        return BLOCKS.getEntries().stream()
                .map(DeferredHolder::get)
                .filter(clazz::isInstance)
                .map(block -> (T) block)
                .collect(Collectors.toList());
    }
}