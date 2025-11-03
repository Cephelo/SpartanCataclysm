package dev.cephelo.spartancataclysm.traits;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import dev.cephelo.spartancataclysm.Config;
import dev.cephelo.spartancataclysm.SpartanCataclysm;
import dev.cephelo.spartancataclysm.sounds.SCSounds;
import krelox.spartantoolkit.BetterWeaponTrait;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class MechaPulseTrait extends BetterWeaponTrait {
    public MechaPulseTrait() {
        super("mecha_pulse", SpartanCataclysm.MODID, WeaponTrait.TraitQuality.POSITIVE);
        setUniversal();
    }

    @Override
    public String getDescription() {
        return Component.translatable("tooltip.spartancataclysm.trait.mecha_pulse").toString();
    }
    // trait desc (reword)

    @Override
    public void onHitEntity(WeaponMaterial material, ItemStack stack, LivingEntity target, LivingEntity attacker, Entity projectile) {
        applyMechaEffects(target, attacker);

        super.onHitEntity(material, stack, target, attacker, projectile);
    }

    @Override
    public void onRangedHitEntity(WeaponMaterial material, ItemStack stack, LivingEntity target, LivingEntity attacker, Entity projectile) {
        applyMechaEffects(target, attacker);

        super.onHitEntity(material, stack, target, attacker, projectile);
    }

    private void applyMechaEffects(LivingEntity target, LivingEntity attacker) {
        if (attacker != null) {
            try {
                if (target != null && Math.random() <= Config.mechaPulseChance) {
                    if (Config.mechaPulseWitherDuration > 0) target.addEffect(new MobEffectInstance(MobEffects.WITHER, Config.mechaPulseWitherDuration, Config.mechaPulseWitherAmplifier));
                    if (Config.mechaPulseFireDuration > 0) target.setSecondsOnFire(Config.mechaPulseFireDuration);
                    if (Config.customSounds) attacker.level().playSeededSound(null, target.getX(), target.getY(), target.getZ(),
                            SCSounds.WITHERITE_HIT.get(), SoundSource.PLAYERS, 1f, (float)((Math.random() * 0.2) + 0.9), 0);
                }
                if (Math.random() <= Config.mechaPulseRegenChance
                        && attacker.getHealth() < (Config.mechaPulseRegenThresholdType ? (attacker.getMaxHealth() / 2) : Config.mechaPulseRegenThreshold)) {
                    attacker.addEffect(new MobEffectInstance(MobEffects.REGENERATION, Config.mechaPulseRegenDuration, Config.mechaPulseRegenAmplifier));
                }

            } catch (Throwable e) {
                SpartanCataclysm.LOGGER.error(String.valueOf(e));
            }
        }
    }
}