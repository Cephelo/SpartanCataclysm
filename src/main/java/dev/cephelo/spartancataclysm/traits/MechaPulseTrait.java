package dev.cephelo.spartancataclysm.traits;

import com.github.L_Ender.cataclysm.init.ModEffect;
import com.github.L_Ender.cataclysm.init.ModParticle;
import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import dev.cephelo.spartancataclysm.Config;
import dev.cephelo.spartancataclysm.SpartanCataclysm;
import dev.cephelo.spartancataclysm.effects.SCEffects;
import dev.cephelo.spartancataclysm.sounds.SCSounds;
import krelox.spartantoolkit.BetterWeaponTrait;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class MechaPulseTrait extends BetterWeaponTrait {
    public MechaPulseTrait() {
        super("mecha_pulse", SpartanCataclysm.MODID, WeaponTrait.TraitQuality.POSITIVE);
        setUniversal();
    }

    @Override
    public String getDescription() {
        return "Can stack Pulse Charge on the attacker.  Upon reaching a sufficient amplifier, a shockwave stuns and deals damage to the target";
    } // reword

    @Override
    public float modifyDamageDealt(WeaponMaterial material, float baseDamage, DamageSource source, LivingEntity attacker, LivingEntity victim) {
        float addedDamage = chargeStunPulse(attacker, victim);
        baseDamage += addedDamage;

        return super.modifyDamageDealt(material, baseDamage, source, attacker, victim);
    }

    @Override
    public float modifyRangedDamageDealt(WeaponMaterial material, float baseDamage, DamageSource source, LivingEntity attacker, LivingEntity victim) {
        float addedDamage = chargeStunPulse(attacker, victim);
        baseDamage += addedDamage;

        return super.modifyRangedDamageDealt(material, baseDamage, source, attacker, victim);
    }

    private float chargeStunPulse(LivingEntity attacker, LivingEntity target) {
        if (attacker != null) {
            try {
                // Return if player has cooldown effect
                var cooldownEffect = SCEffects.PULSE_COOLDOWN.get();
                var oldCooldownEffect = attacker.getEffect(cooldownEffect);
                if (oldCooldownEffect != null) return 0;

                var chargeEffect = SCEffects.PULSE_CHARGE.get();
                var oldChargeEffect = attacker.getEffect(chargeEffect);
                int i = oldChargeEffect == null ? 0 : Math.min(Config.mechaPulseStunThreshold, oldChargeEffect.getAmplifier() + 1);
                boolean reachedMax = (i >= Config.mechaPulseStunThreshold && oldChargeEffect.getAmplifier() == i - 1);

                if (Math.random() <= Config.mechaPulseChargeChance) {
                    attacker.addEffect(new MobEffectInstance(chargeEffect, Config.mechaPulseEffectDuration, i));

                    // Pulse Charge particles
                    if (attacker.level() instanceof ServerLevel serverLevel)
                        serverLevel.sendParticles(ParticleTypes.ANGRY_VILLAGER, attacker.getX(), attacker.getY() + 1.4, attacker.getZ(), 3, 0.4, 0.4, 0.4, 0);

                    // Shockwave logic
                    if (reachedMax) {
                        attacker.removeEffect(chargeEffect);
                        attacker.addEffect(new MobEffectInstance(cooldownEffect, Config.mechaPulseCooldown, 0, false, false, true)); // no particles
                        target.addEffect(new MobEffectInstance(ModEffect.EFFECTSTUN.get(), Config.mechaPulseStunDuration, 0));

                        // Shockwave particle
                        if (attacker.level() instanceof ServerLevel serverLevel)
                            serverLevel.sendParticles(ModParticle.EM_PULSE.get(), target.getX(), target.getY() + target.getEyeHeight() - 1.25, target.getZ(), 1, 0, 0, 0, 0);

                        if (Config.customSounds) {
                            attacker.level().playSeededSound(null, attacker.getX(), attacker.getY(), attacker.getZ(),
                                    SCSounds.MECHA_PULSE_SHOCKWAVE.get(), SoundSource.PLAYERS, 1f, 1f, 0);
                            attacker.level().playSeededSound(null, target.getX(), target.getY(), target.getZ(),
                                    SCSounds.WITHERITE_HIT.get(), SoundSource.PLAYERS, 2f, 1f, 0);
                        }

                        return (float)Config.mechaPulseExtraDamage;
                    }
                }

            } catch (Throwable e) {
                SpartanCataclysm.LOGGER.error(String.valueOf(e));
            }
        }

        return 0;
    }
}