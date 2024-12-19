package net.silentchaos512.gear.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.silentchaos512.gear.SilentGear;
import net.silentchaos512.gear.network.payload.client.KeyPressOnItemPayload;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;

@EventBusSubscriber(modid = SilentGear.MOD_ID, value = Dist.CLIENT)
public class KeyTracker {
    public static final KeyMapping DISPLAY_PROPERTIES = createKeyBinding("displayItemProperties", GLFW.GLFW_KEY_LEFT_CONTROL);
    @Deprecated
    public static final KeyMapping DISPLAY_TRAITS = createKeyBinding("displayTraits", GLFW.GLFW_KEY_LEFT_SHIFT);
    public static final KeyMapping DISPLAY_CONSTRUCTION = createKeyBinding("displayItemConstruction", GLFW.GLFW_KEY_LEFT_ALT);
    public static final KeyMapping OPEN_ITEM = createKeyBinding("openItem", GLFW.GLFW_KEY_X);
    public static final KeyMapping CYCLE_BACK = createKeyBinding("cycle.back", GLFW.GLFW_KEY_Z);
    public static final KeyMapping CYCLE_NEXT = createKeyBinding("cycle.next", GLFW.GLFW_KEY_C);

    private static int materialCycleCount = 0;

    @EventBusSubscriber(modid = SilentGear.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    static final class Registration {
        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(CYCLE_BACK);
            event.register(CYCLE_NEXT);
            event.register(DISPLAY_PROPERTIES);
            event.register(DISPLAY_TRAITS);
            event.register(DISPLAY_CONSTRUCTION);
            event.register(OPEN_ITEM);
        }
    }

    @Nonnull
    private static KeyMapping createKeyBinding(String description, int key) {
        return new KeyMapping(
                "key.silentgear." + description,
                KeyConflictContext.GUI,
                InputConstants.Type.KEYSYM,
                key,
                "key.categories.silentgear"
        );
    }

    public static int getMaterialCycleIndex(int total) {
        int i = materialCycleCount % total;
        return i < 0 ? i + total : i;
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (event.getAction() == GLFW.GLFW_RELEASE && (event.getKey() == DISPLAY_PROPERTIES.getKey().getValue())) {
            materialCycleCount = 0;
        }
        if (event.getAction() == GLFW.GLFW_PRESS && event.getKey() == CYCLE_NEXT.getKey().getValue()) {
            if (isDisplayPropertiesDown()) {
                ++materialCycleCount;
            }
            ItemStack hovered = getHoveredItem();
            if (!hovered.isEmpty()) {
                PacketDistributor.sendToServer(new KeyPressOnItemPayload(KeyPressOnItemPayload.KeyPressType.CYCLE_NEXT, getHoveredSlot()));
            }
        }
        if (event.getAction() == GLFW.GLFW_PRESS && event.getKey() == CYCLE_BACK.getKey().getValue()) {
            if (isDisplayPropertiesDown()) {
                --materialCycleCount;
            }
            ItemStack hovered = getHoveredItem();
            if (!hovered.isEmpty()) {
                PacketDistributor.sendToServer(new KeyPressOnItemPayload(KeyPressOnItemPayload.KeyPressType.CYCLE_BACK, getHoveredSlot()));
            }
        }
        if (event.getAction() == GLFW.GLFW_PRESS && event.getKey() == OPEN_ITEM.getKey().getValue()) {
            ItemStack hovered = getHoveredItem();
            if (!hovered.isEmpty()) {
                PacketDistributor.sendToServer(new KeyPressOnItemPayload(KeyPressOnItemPayload.KeyPressType.OPEN_ITEM, getHoveredSlot()));
            }
        }

        // Toggle isDown for tooltip keybindings
        if (event.getAction() == GLFW.GLFW_PRESS) {
            if (event.getKey() == DISPLAY_PROPERTIES.getKey().getValue()) {
                DISPLAY_PROPERTIES.setDown(true);
            }
            if (event.getKey() == DISPLAY_CONSTRUCTION.getKey().getValue()) {
                DISPLAY_CONSTRUCTION.setDown(true);
            }
        }
        if (event.getAction() == GLFW.GLFW_RELEASE) {
            if (event.getKey() == DISPLAY_PROPERTIES.getKey().getValue()) {
                DISPLAY_PROPERTIES.setDown(false);
            }
            if (event.getKey() == DISPLAY_CONSTRUCTION.getKey().getValue()) {
                DISPLAY_CONSTRUCTION.setDown(false);
            }
        }
    }

    private static ItemStack getHoveredItem() {
        Screen currentScreen = Minecraft.getInstance().screen;
        if (currentScreen instanceof AbstractContainerScreen<?> containerScreen) {
            Slot slot = containerScreen.getSlotUnderMouse();
            if (slot != null) {
                return slot.getItem();
            }
        }
        return ItemStack.EMPTY;
    }

    private static int getHoveredSlot() {
        Screen currentScreen = Minecraft.getInstance().screen;
        if (currentScreen instanceof AbstractContainerScreen<?> containerScreen) {
            Slot slot = containerScreen.getSlotUnderMouse();
            if (slot != null) {
                return slot.getSlotIndex();
            }
        }
        return -1;
    }

    public static boolean isDisplayPropertiesDown() {
        int code = DISPLAY_PROPERTIES.getKey().getValue();
        if (code == GLFW.GLFW_KEY_LEFT_CONTROL || code == GLFW.GLFW_KEY_RIGHT_CONTROL || DISPLAY_PROPERTIES.isUnbound()) {
            // Maintain old behavior of checking both ctrl keys
            return isControlDown();
        }
        return DISPLAY_PROPERTIES.isDown();
    }

    public static boolean isDisplayConstructionDown() {
        int code = DISPLAY_CONSTRUCTION.getKey().getValue();
        if (code == GLFW.GLFW_KEY_LEFT_ALT || code == GLFW.GLFW_KEY_RIGHT_ALT || DISPLAY_CONSTRUCTION.isUnbound()) {
            return isAltDown();
        }
        return DISPLAY_CONSTRUCTION.isDown();
    }

    @Deprecated
    public static boolean isDisplayTraitsDown() {
        int code = DISPLAY_TRAITS.getKey().getValue();
        if (code == GLFW.GLFW_KEY_LEFT_SHIFT || code == GLFW.GLFW_KEY_RIGHT_SHIFT || DISPLAY_TRAITS.isUnbound()) {
            return isShiftDown();
        }
        return DISPLAY_TRAITS.isDown();
    }

    public static boolean isShiftDown() {
        long h = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(h, GLFW.GLFW_KEY_LEFT_SHIFT) || InputConstants.isKeyDown(h, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    public static boolean isControlDown() {
        long h = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(h, GLFW.GLFW_KEY_LEFT_CONTROL) || InputConstants.isKeyDown(h, GLFW.GLFW_KEY_RIGHT_CONTROL);
    }

    public static boolean isAltDown() {
        long h = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(h, GLFW.GLFW_KEY_LEFT_ALT) || InputConstants.isKeyDown(h, GLFW.GLFW_KEY_RIGHT_ALT);
    }
}
