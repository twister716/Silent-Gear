package net.silentchaos512.gear.crafting.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.CommonHooks;
import net.silentchaos512.gear.api.item.GearItem;
import net.silentchaos512.gear.Config;
import net.silentchaos512.gear.gear.material.MaterialInstance;
import net.silentchaos512.gear.gear.part.RepairContext;
import net.silentchaos512.gear.item.RepairKitItem;
import net.silentchaos512.gear.setup.SgRecipes;
import net.silentchaos512.gear.setup.gear.GearProperties;
import net.silentchaos512.gear.util.GearData;
import net.silentchaos512.lib.collection.StackList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QuickRepairRecipe extends CustomRecipe {
    public QuickRepairRecipe(CraftingBookCategory bookCategory) {
        super(bookCategory);
    }

    @Override
    public boolean matches(CraftingInput inv, Level worldIn) {
        // Need 1 gear, 1 repair kit, and optional materials
        ItemStack gear = ItemStack.EMPTY;
        boolean foundKit = false;
        float repairKitEfficiency = Config.Common.missingRepairKitEfficiency.get().floatValue();
        List<ItemStack> materials = new ArrayList<>();

        for (int i = 0; i < inv.size(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                //noinspection ChainOfInstanceofChecks
                if (stack.getItem() instanceof GearItem) {
                    if (!gear.isEmpty()) {
                        return false;
                    }
                    gear = stack;
                } else if (stack.getItem() instanceof RepairKitItem) {
                    if (foundKit) {
                        return false;
                    }
                    foundKit = true;
                    repairKitEfficiency = getKitEfficiency(stack);
                } else if (MaterialInstance.from(stack) != null) {
                    materials.add(stack);
                } else {
                    return false;
                }
            }
        }

        if (gear.isEmpty() || repairKitEfficiency < 0.1E-9) return false;

        for (ItemStack stack : materials) {
            if (!SgRecipes.isRepairMaterial(gear, stack)) {
                return false;
            }
        }

        return true;
    }

    private static float getKitEfficiency(ItemStack stack) {
        if (stack.getItem() instanceof RepairKitItem) {
            return ((RepairKitItem) stack.getItem()).getRepairEfficiency(RepairContext.Type.QUICK);
        }
        return Config.Common.missingRepairKitEfficiency.get().floatValue();
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registryAccess) {
        StackList list = StackList.from(inv);
        ItemStack gear = list.uniqueOfType(GearItem.class).copy();
        ItemStack repairKit = list.uniqueOfType(RepairKitItem.class);
        Collection<ItemStack> mats = list.allMatches(mat -> SgRecipes.isRepairMaterial(gear, mat));

        // Repair with materials first
        repairWithLooseMaterials(gear, repairKit, mats);

        // Then use repair kit, if necessary
        if (gear.getDamageValue() > 0 && repairKit.getItem() instanceof RepairKitItem item) {
            int value = item.getDamageToRepair(gear, repairKit, RepairContext.Type.QUICK);
            if (value > 0) {
                gear.setDamageValue(gear.getDamageValue() - value);
            }
        }

        GearData.incrementRepairedCount(gear, 1);
        GearData.recalculateGearData(gear, CommonHooks.getCraftingPlayer());
        return gear;
    }

    private static void repairWithLooseMaterials(ItemStack gear, ItemStack repairKit, Collection<ItemStack> mats) {
        float repairValue = getRepairValueFromMaterials(gear, mats);
        float kitEfficiency = getKitEfficiency(repairKit);
        float gearRepairEfficiency = GearData.getProperties(gear).getNumber(GearProperties.REPAIR_EFFICIENCY);
        gear.setDamageValue(gear.getDamageValue() - Math.round(repairValue * kitEfficiency * gearRepairEfficiency));
    }

    private static float getRepairValueFromMaterials(ItemStack gear, Collection<ItemStack> mats) {
        float repairValue = 0f;
        for (ItemStack stack : mats) {
            MaterialInstance material = MaterialInstance.from(stack);
            if (material != null) {
                repairValue += material.getRepairValue(gear);
            }
        }

        // Repair efficiency instance tool class
        if (gear.getItem() instanceof GearItem) {
            float repairEfficiency = GearData.getProperties(gear).getNumber(GearProperties.REPAIR_EFFICIENCY);
            if (repairEfficiency > 0) {
                repairValue *= repairEfficiency;
            }
        }
        return repairValue;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput inv) {
        NonNullList<ItemStack> list = NonNullList.withSize(inv.size(), ItemStack.EMPTY);
        StackList stackList = StackList.from(inv);
        ItemStack gear = stackList.uniqueMatch(s -> s.getItem() instanceof GearItem);
        ItemStack repairKit = stackList.uniqueMatch(s -> s.getItem() instanceof RepairKitItem);

        for (int i = 0; i < list.size(); ++i) {
            ItemStack stack = inv.getItem(i);

            if (stack.getItem() instanceof RepairKitItem item) {
                repairWithLooseMaterials(gear, repairKit, stackList.allMatches(mat -> SgRecipes.isRepairMaterial(gear, mat)));
                ItemStack copy = stack.copy();
                item.removeRepairMaterials(copy, item.getRepairMaterials(gear, copy, RepairContext.Type.QUICK));
                list.set(i, copy);
            } else if (stack.hasCraftingRemainingItem()) {
                list.set(i, stack.getCraftingRemainingItem());
            }
        }

        return list;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SgRecipes.QUICK_REPAIR.get();
    }
}
