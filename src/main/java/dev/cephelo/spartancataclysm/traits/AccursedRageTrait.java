package dev.cephelo.spartancataclysm.traits;

import com.github.L_Ender.cataclysm.init.ModParticle;
import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import dev.cephelo.spartancataclysm.Config;
import dev.cephelo.spartancataclysm.SpartanCataclysm;
import dev.cephelo.spartancataclysm.effects.SCEffects;
import dev.cephelo.spartancataclysm.sounds.SCSounds;
import krelox.spartantoolkit.BetterWeaponTrait;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class AccursedRageTrait extends BetterWeaponTrait {
    public AccursedRageTrait() {
        super("accursed_rage", SpartanCataclysm.MODID, WeaponTrait.TraitQuality.POSITIVE);
        setUniversal();
    }

    @Override
    public String getDescription() {
        return "Can stack Accursed Rage on the user. Attackers with Accursed Rage do more damage with Cursium weapons based on the amplifier.";
    }

    @Override
    public float modifyDamageDealt(WeaponMaterial material, float baseDamage, DamageSource source, LivingEntity attacker, LivingEntity victim) {
        float addedDamage = calculateRage(attacker, victim);
        baseDamage += addedDamage;

        return super.modifyDamageDealt(material, baseDamage, source, attacker, victim);
    }

    @Override
    public float modifyRangedDamageDealt(WeaponMaterial material, float baseDamage, DamageSource source, LivingEntity attacker, LivingEntity victim) {
        float addedDamage = calculateRage(attacker, victim);
        baseDamage += addedDamage;

        return super.modifyRangedDamageDealt(material, baseDamage, source, attacker, victim);
    }

    private float calculateRage(LivingEntity attacker, LivingEntity target) {
        if (attacker != null) {
            try {
                var rageEffect = SCEffects.ACCURSED_RAGE.get();
                var oldEffect = attacker.getEffect(rageEffect);
                int i = oldEffect == null ? 0 : Math.min(Config.accursedRageMaximum, oldEffect.getAmplifier() + 1);
                boolean reachedMax = (i >= Config.accursedRageMaximum && oldEffect.getAmplifier() == i - 1);

                // Targets hit with maximum Accursed Rage emit small cursed flame particles
                if (attacker.level() instanceof ServerLevel serverLevel && i >= Config.accursedRageMaximum)
                    serverLevel.sendParticles(ModParticle.SMALL_CURSED_FLAME.get(), target.getX(), target.getY() + target.getEyeHeight() - 1.0, target.getZ(), 8, 0.4, 0.7, 0.4, 0.02);

                if (Math.random() <= Config.accursedRageChance) {
                    attacker.addEffect(new MobEffectInstance(rageEffect, Config.accursedRageDuration, i));

                    // Accursed Rage particles
                    if (attacker.level() instanceof ServerLevel serverLevel) {
                        if (reachedMax)
                            serverLevel.sendParticles(ModParticle.CURSED_FLAME.get(), attacker.getX(), attacker.getY() + 1, attacker.getZ(), 15, 0.3, 0.6, 0.3, 0.02);
                        else if (i < Config.accursedRageMaximum)
                            serverLevel.sendParticles(ModParticle.SMALL_CURSED_FLAME.get(), attacker.getX(), attacker.getY() + 1, attacker.getZ(), 9, 0.3, 0.6, 0.3, 0.01);
                    }

                    // Play sound when attacker reaches maximum rage
                    if (Config.customSounds && reachedMax) attacker.level().playSeededSound(null, attacker.getX(), attacker.getY(), attacker.getZ(),
                            SCSounds.ACCURSED_RAGE_MAX.get(), SoundSource.PLAYERS, 1f, 1f, 0);
                }

                // Calculate extra damage
                var newEffect = attacker.getEffect(rageEffect);
                if (newEffect != null) {
                    if (Config.customSounds) attacker.level().playSeededSound(null, target.getX(), target.getY(), target.getZ(),
                            SCSounds.CURSIUM_HIT.get(), SoundSource.PLAYERS, 1f, Config.getRandomPitch(), 0);
                    return (newEffect.getAmplifier() + 1) * (float)Config.accursedRageExtraDamage;
                }

            } catch (Throwable e) {
                SpartanCataclysm.LOGGER.error(String.valueOf(e));
            }
        }

        return 0;
    }

}