package dev.cephelo.spartancataclysm.traits;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import dev.cephelo.spartancataclysm.Config;
import dev.cephelo.spartancataclysm.SpartanCataclysm;
import dev.cephelo.spartancataclysm.effects.SCEffects;
import dev.cephelo.spartancataclysm.sounds.SCSounds;
import krelox.spartantoolkit.BetterWeaponTrait;
import net.minecraft.network.chat.Component;
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
        return Component.translatable("tooltip.spartancataclysm.trait.accursed_rage").toString();
    }
    // Stacks Accursed Rage on attacker.  Attackers with Accursed Rage do more damage with Cursium weapons depending on the amplifier.
    // (reword)

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

        return super.modifyDamageDealt(material, baseDamage, source, attacker, victim);
    }

    private float calculateRage(LivingEntity attacker, LivingEntity target) {
        if (attacker != null) {
            try {
                var eff = SCEffects.ACCURSED_RAGE.get();
                var old = attacker.getEffect(eff);
                int i = old == null ? 0 : Math.min(4, old.getAmplifier() + 1);
                if (Math.random() <= Config.accursedRageChance) {
                    attacker.addEffect(new MobEffectInstance(eff, 100, i));
                    // Play sound when attacker reaches maximum rage
                    if (Config.customSounds && i == 4 && old.getAmplifier() == 3) attacker.level().playSeededSound(null, attacker.getX(), attacker.getY(), attacker.getZ(),
                            SCSounds.ACCURSED_RAGE_MAX.get(), SoundSource.PLAYERS, 1f, 1f, 0);
                }

                var newEffect = attacker.getEffect(eff);
                if (newEffect != null) {
                    if (Config.customSounds) attacker.level().playSeededSound(null, target.getX(), target.getY(), target.getZ(),
                            SCSounds.CURSIUM_HIT.get(), SoundSource.PLAYERS, 1f, (float)((Math.random() * 0.2) + 0.9), 0);
                    return (newEffect.getAmplifier() + 1) * (float)Config.accursedRageExtraDamage;
                }

            } catch (Throwable e) {
                SpartanCataclysm.LOGGER.error(String.valueOf(e));
            }
        }

        return 0;
    }

}