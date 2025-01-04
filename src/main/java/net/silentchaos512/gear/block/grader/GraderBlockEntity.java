package net.silentchaos512.gear.block.grader;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.silentchaos512.gear.SilentGear;
import net.silentchaos512.gear.api.part.MaterialGrade;
import net.silentchaos512.gear.block.SgContainerBlockEntity;
import net.silentchaos512.gear.gear.material.MaterialInstance;
import net.silentchaos512.gear.setup.SgBlockEntities;
import net.silentchaos512.gear.setup.SgTags;
import net.silentchaos512.gear.setup.gear.MaterialModifiers;
import net.silentchaos512.lib.util.EnumUtils;
import net.silentchaos512.lib.util.InventoryUtils;
import net.silentchaos512.lib.util.TimeUtils;

import java.util.stream.IntStream;

public class GraderBlockEntity extends SgContainerBlockEntity {
    static final int BASE_ANALYZE_TIME = TimeUtils.ticksFromSeconds(SilentGear.isDevBuild() ? 1 : 5);

    static final int INPUT_SLOT = 0;
    static final int CATALYST_SLOT = 1;
    private static final int[] SLOTS_INPUT = {INPUT_SLOT, CATALYST_SLOT};
    private static final int[] SLOTS_OUTPUT = {2, 3, 4, 5};
    static final int INVENTORY_SIZE = SLOTS_INPUT.length + SLOTS_OUTPUT.length;
    private static final int[] SLOTS_ALL = IntStream.rangeClosed(0, INVENTORY_SIZE - 1).toArray();

    private int progress = 0;
    private MaterialGrade lastGradeAttempt = MaterialGrade.NONE;

    private final ContainerData fields = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> lastGradeAttempt.ordinal();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    progress = value;
                    break;
                case 1:
                    lastGradeAttempt = EnumUtils.byOrdinal(value, MaterialGrade.NONE);
                    break;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public GraderBlockEntity(BlockPos pos, BlockState state) {
        super(SgBlockEntities.MATERIAL_GRADER.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, GraderBlockEntity blockEntity) {
        // Don't waste time if there is no input or no free output slots
        ItemStack input = blockEntity.getInputStack();
        if (input.isEmpty()) return;

        int outputSlot = blockEntity.getFreeOutputSlot();
        if (outputSlot < 0) return;

        ItemStack catalyst = blockEntity.getCatalystStack();
        int catalystTier = getCatalystTier(catalyst);
        if (catalystTier < 1) return;

        MaterialInstance material = MaterialInstance.from(input);
        if (material != null && canGrade(input)) {
            if (blockEntity.progress < BASE_ANALYZE_TIME) {
                ++blockEntity.progress;
            }

            if (blockEntity.progress >= BASE_ANALYZE_TIME && !level.isClientSide) {
                blockEntity.progress = 0;
                catalyst.shrink(1);
                blockEntity.tryGradeItem(input, catalystTier, material);
            }
        } else {
            blockEntity.progress = 0;
        }
    }

    private void tryGradeItem(ItemStack input, int catalystTier, MaterialInstance material) {
        MaterialGrade targetGrade = MaterialGrade.selectWithCatalyst(SilentGear.RANDOM, catalystTier);
        this.lastGradeAttempt = targetGrade;
        var currentGradeMod = material.getModifier(MaterialModifiers.GRADE.get());

        if (currentGradeMod == null || targetGrade.ordinal() > currentGradeMod.grade().ordinal()) {
            // Assign grade, move to output slot
            ItemStack stack = input.split(1);
            targetGrade.setGradeOnStack(stack);

            InventoryUtils.mergeItem(this, 2, 2 + SLOTS_OUTPUT.length, stack);
        }
    }

    public static boolean canGrade(ItemStack stack) {
        var material = MaterialInstance.from(stack);
        if (material == null) return false;

        var gradeMod = material.getModifier(MaterialModifiers.GRADE.get());
        return gradeMod == null || gradeMod.grade() != MaterialGrade.MAX;
    }

    private ItemStack getInputStack() {
        ItemStack stack = getItem(INPUT_SLOT);
        if (!stack.isEmpty() && canGrade(stack)) {
            return stack;
        }
        return ItemStack.EMPTY;
    }

    private ItemStack getCatalystStack() {
        return getItem(CATALYST_SLOT);
    }

    private int getFreeOutputSlot() {
        for (int slot : SLOTS_OUTPUT) {
            if (getItem(slot).isEmpty()) {
                return slot;
            }
        }
        return -1;
    }

    public static int getCatalystTier(ItemStack stack) {
        if (!stack.isEmpty()) {
            for (int i = SgTags.Items.GRADER_CATALYSTS_TIERS.size() - 1; i >= 0; --i) {
                if (stack.is(SgTags.Items.GRADER_CATALYSTS_TIERS.get(i))) {
                    return i + 1;
                }
            }
        }
        return 0;
    }

    @Override
    public void setChanged() {
        this.progress = 0;
        super.setChanged();
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        this.progress = pTag.getInt("Progress");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.putInt("Progress", this.progress);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tags = super.getUpdateTag(pRegistries);
        tags.putInt("Progress", this.progress);

        ItemStack input = getInputStack();
        if (!input.isEmpty()) {
            tags.put("input_item", input.save(pRegistries, new CompoundTag()));
        }
        return tags;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);

        CompoundTag tags = pkt.getTag();
        if (tags == null) return;

        this.progress = tags.getInt("Progress");

        if (tags.contains("input_item")) {
            var inputItem = ItemStack.parse(lookupProvider, tags.getCompound("input_item")).orElse(ItemStack.EMPTY);
            setItem(INPUT_SLOT, inputItem);
        } else {
            setItem(INPUT_SLOT, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        if (index != INPUT_SLOT && index != CATALYST_SLOT) {
            return false;
        }

        ItemStack stackInSlot = getItem(index);
        if (stack.isEmpty() || (!stackInSlot.isEmpty() && !ItemStack.isSameItemSameComponents(stackInSlot, stack))) {
            return false;
        }

        if (index == INPUT_SLOT) {
            return canGrade(stack);
        } else {
            return getCatalystTier(stack) > 0;
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.silentgear.material_grader");
    }

    @Override
    public ItemStackHandler createItemHandler() {
        return new ItemStackHandler(INVENTORY_SIZE) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return (slot == INPUT_SLOT && canGrade(stack)) ||
                        (slot == CATALYST_SLOT && getCatalystTier(stack) > 0);
            }

            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                if (slot == INPUT_SLOT || slot == CATALYST_SLOT) return ItemStack.EMPTY;
                return super.extractItem(slot, amount, simulate);
            }
        };
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
        return new GraderContainer(id, playerInventory, this, fields);
    }
}
