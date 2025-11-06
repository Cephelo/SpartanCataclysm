package dev.cephelo.spartancataclysm.effects;

import dev.cephelo.spartancataclysm.SpartanCataclysm;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class SCEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, SpartanCataclysm.MODID);

    public static final RegistryObject<MobEffect> ACCURSED_RAGE = MOB_EFFECTS.register("accursed_rage",
            () -> new AccursedMarkEffect(MobEffectCategory.BENEFICIAL, 0x39d2b2));
    public static final RegistryObject<MobEffect> PULSE_CHARGE = MOB_EFFECTS.register("pulse_charge",
            () -> new StunPulseChargeEffect(MobEffectCategory.BENEFICIAL, 0xae2334));
    public static final RegistryObject<MobEffect> PULSE_COOLDOWN = MOB_EFFECTS.register("pulse_cooldown",
            () -> new StunPulseCooldownEffect(MobEffectCategory.NEUTRAL, 0x575757));

    public static void register(IEventBus bus) {
        MOB_EFFECTS.register(bus);
    }

    // Effect does nothing, everything is inside traits/AccursedRageTrait
    public static class AccursedMarkEffect extends MobEffect {
        public AccursedMarkEffect(MobEffectCategory category, int color) {
            super(category, color);
        }
    }

    // Effect does nothing, everything is inside traits/MechaPulseTrait
    public static class StunPulseChargeEffect extends MobEffect {
        public StunPulseChargeEffect(MobEffectCategory category, int color) {
            super(category, color);
        }
    }

    // Effect does nothing, everything is inside traits/MechaPulseTrait
    public static class StunPulseCooldownEffect extends MobEffect {
        public StunPulseCooldownEffect(MobEffectCategory category, int color) {
            super(category, color);
        }
        @Override
        public List<ItemStack> getCurativeItems() {
            return List.of(); // Cannot be removed with milk
        }
    }
}
