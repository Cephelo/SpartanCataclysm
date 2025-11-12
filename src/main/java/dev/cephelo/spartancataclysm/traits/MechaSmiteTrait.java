package dev.cephelo.spartancataclysm.traits;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import dev.cephelo.spartancataclysm.Config;
import dev.cephelo.spartancataclysm.SpartanCataclysm;
import dev.cephelo.spartancataclysm.sounds.SCSounds;
import krelox.spartantoolkit.BetterWeaponTrait;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class MechaSmiteTrait extends BetterWeaponTrait {
    public MechaSmiteTrait() {
        super("mecha_smite", SpartanCataclysm.MODID, WeaponTrait.TraitQuality.POSITIVE);
        setUniversal();
    }

    @Override
    public String getDescription() {
        return "Ignites targets and inflicts them with Wither. Can grant Regeneration when attacking while below half health.";
    } // reword

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
                if (target != null && Math.random() <= Config.mechaSmiteChance) {
                    if (Config.mechaSmiteWitherDuration > 0) target.addEffect(new MobEffectInstance(MobEffects.WITHER, Config.mechaSmiteWitherDuration, Config.mechaSmiteWitherAmplifier));
                    if (Config.mechaSmiteFireDuration > 0) target.setSecondsOnFire(Config.mechaSmiteFireDuration);
                    if (Config.customSounds) attacker.level().playSeededSound(null, target.getX(), target.getY(), target.getZ(),
                            SCSounds.WITHERITE_HIT.get(), SoundSource.PLAYERS, 1f, Config.getRandomPitch(), 0);
                }
                if (Math.random() <= Config.mechaSmiteRegenChance
                        && attacker.getHealth() < (Config.mechaSmiteRegenThresholdType ? (attacker.getMaxHealth() * Config.mechaSmiteRegenThresholdPercent) : Config.mechaSmiteRegenThreshold)) {
                    attacker.addEffect(new MobEffectInstance(MobEffects.REGENERATION, Config.mechaSmiteRegenDuration, Config.mechaSmiteRegenAmplifier));
                }

            } catch (Throwable e) {
                SpartanCataclysm.LOGGER.error(String.valueOf(e));
            }
        }
    }
}