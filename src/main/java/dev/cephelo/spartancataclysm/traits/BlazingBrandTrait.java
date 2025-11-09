package dev.cephelo.spartancataclysm.traits;

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
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class BlazingBrandTrait extends BetterWeaponTrait {
    public BlazingBrandTrait() {
        super("blazing_brand", SpartanCataclysm.MODID, WeaponTrait.TraitQuality.POSITIVE);
        setUniversal();
    }

    @Override
    public String getDescription() {
        return "Can stack Blazing Brand on the target, reducing armor based on the amplifier.  Attackers gain a small amount of lifesteal.";
    }

    @Override
    public float modifyDamageDealt(WeaponMaterial material, float baseDamage, DamageSource source, LivingEntity attacker, LivingEntity victim) {
        calculateBlazingBrand(attacker, victim);

        return super.modifyDamageDealt(material, baseDamage, source, attacker, victim);
    }

    @Override
    public float modifyRangedDamageDealt(WeaponMaterial material, float baseDamage, DamageSource source, LivingEntity attacker, LivingEntity victim) {
        calculateBlazingBrand(attacker, victim);

        return super.modifyDamageDealt(material, baseDamage, source, attacker, victim);
    }

    private static void calculateBlazingBrand(LivingEntity attacker, LivingEntity victim) {
        if (attacker != null && victim != null && Math.random() <= Config.blazingBrandChance) {
            float factor = (float)Config.lifestealMultiplier;

            if (factor > 0.0f && attacker instanceof Player player) {
                float speed = (float) player.getAttributeValue(Attributes.ATTACK_SPEED);
                factor = factor / Mth.clamp(speed, 0.5f, 2);
            }

            stackBlazingBrand(attacker, victim, factor);
        }
    }

    private static void stackBlazingBrand(LivingEntity attacker, LivingEntity target, float factor) {
        try {
            var brandEffect = SCEffects.BLAZING_BRAND_CUSTOM.get();
            var oldEffect = target.getEffect(brandEffect);
            int i = oldEffect == null ? 0 : Math.min(Config.blazingBrandMaximum, oldEffect.getAmplifier() + 1);

            target.addEffect(new MobEffectInstance(brandEffect, Config.blazingBrandDuration, i));

            // Lifesteal
            if (factor > 0.0f && Math.random() <= Config.blazingBrandLifestealChance) {
                attacker.heal(factor * (float) (i + 1));
                // Particles for healing
                if (attacker.level() instanceof ServerLevel serverLevel)
                    serverLevel.sendParticles(ParticleTypes.SMALL_FLAME, attacker.getX(), attacker.getY() + 1, attacker.getZ(), 6, 0.3, 0.6, 0.3, 0.02);
            }

            // Blazing Brand particles on target
            if (attacker.level() instanceof ServerLevel serverLevel)
                serverLevel.sendParticles((i == Config.blazingBrandMaximum ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME), target.getX(), target.getY() + target.getEyeHeight() - 1.0, target.getZ(), 10, 0.4, 0.7, 0.4, 0.02);

            // Custom hit sound
            if (Config.customSounds) attacker.level().playSeededSound(null, target.getX(), target.getY(), target.getZ(),
                    SCSounds.IGNITIUM_HIT.get(), SoundSource.PLAYERS, 1f, Config.getRandomPitch(), 0);
        } catch (Throwable e) {
            SpartanCataclysm.LOGGER.error(String.valueOf(e));
        }
    }

}