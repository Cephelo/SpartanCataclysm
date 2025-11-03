package dev.cephelo.spartancataclysm.sounds;

import dev.cephelo.spartancataclysm.SpartanCataclysm;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SCSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SpartanCataclysm.MODID);

    public static final RegistryObject<SoundEvent> IGNITIUM_HIT = registerSoundEvent("ignitium_hit");
    public static final RegistryObject<SoundEvent> CURSIUM_HIT = registerSoundEvent("cursium_hit");
    public static final RegistryObject<SoundEvent> WITHERITE_HIT = registerSoundEvent("witherite_hit");
    public static final RegistryObject<SoundEvent> ACCURSED_RAGE_MAX = registerSoundEvent("accursed_rage_max");


    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () ->
                SoundEvent.createVariableRangeEvent(new ResourceLocation(SpartanCataclysm.MODID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}