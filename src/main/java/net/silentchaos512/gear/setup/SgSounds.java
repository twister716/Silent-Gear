package net.silentchaos512.gear.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.gear.SilentGear;

public class SgSounds {
    public static final DeferredRegister<SoundEvent> REGISTRAR = DeferredRegister.create(Registries.SOUND_EVENT, SilentGear.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> GEAR_DAMAGED = register("item.silentgear.gear_damaged");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return REGISTRAR.register(name, () -> SoundEvent.createVariableRangeEvent(SilentGear.getId(name)));
    }
}