package dev.cephelo.spartancataclysm.effects;

import dev.cephelo.spartancataclysm.SpartanCataclysm;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SCEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, SpartanCataclysm.MODID);

    public static final RegistryObject<MobEffect> ACCURSED_RAGE = MOB_EFFECTS.register("accursed_rage",
            () -> new AccursedMarkEffect(MobEffectCategory.BENEFICIAL, 10101001));
    // effect.spartancataclysm.accursed_mark

    public static void register(IEventBus bus) {
        MOB_EFFECTS.register(bus);
    }

    // Effect does nothing, everything is inside traits/AccursedRageTrait
    public static class AccursedMarkEffect extends MobEffect {
        public AccursedMarkEffect(MobEffectCategory category, int color) {
            super(category, color);
        }
    }
}
