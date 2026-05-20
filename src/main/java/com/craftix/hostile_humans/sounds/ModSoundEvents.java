package com.craftix.hostile_humans.sounds;

import com.craftix.hostile_humans.HostileHumans;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, HostileHumans.MOD_ID);

    protected ModSoundEvents() {
    }
}
