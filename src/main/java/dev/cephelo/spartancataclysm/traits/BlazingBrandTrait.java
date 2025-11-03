package dev.cephelo.spartancataclysm.traits;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import dev.cephelo.spartancataclysm.Config;
import dev.cephelo.spartancataclysm.SpartanCataclysm;
import dev.cephelo.spartancataclysm.sounds.SCSounds;
import krelox.spartantoolkit.BetterWeaponTrait;
import com.github.L_Ender.cataclysm.config.CMConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import com.github.L_Ender.cataclysm.init.ModEffect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import com.oblivioussp.spartanweaponry.init.ModDamageTypes;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class BlazingBrandTrait extends BetterWeaponTrait {
    public BlazingBrandTrait() {
        super("blazing_brand", SpartanCataclysm.MODID, WeaponTrait.TraitQuality.POSITIVE);
        setUniversal();
    }

    @Override
    public String getDescription() {
        return Component.translatable("tooltip.spartancataclysm.trait.blazing_brand").toString();
    }// Stacks Blazing Brand on target and heals on attack.  (reword)

    @Override
    public float modifyDamageDealt(WeaponMaterial material, float baseDamage, DamageSource source, LivingEntity attacker, LivingEntity victim) {
        calculateBlazingBrand(source, attacker, victim);

        return super.modifyDamageDealt(material, baseDamage, source, attacker, victim);
    }

    @Override
    public float modifyRangedDamageDealt(WeaponMaterial material, float baseDamage, DamageSource source, LivingEntity attacker, LivingEntity victim) {
        calculateBlazingBrand(source, attacker, victim);

        return super.modifyDamageDealt(material, baseDamage, source, attacker, victim);
    }

    private static void calculateBlazingBrand(DamageSource source, LivingEntity attacker, LivingEntity victim) {
        if (attacker != null && victim != null && Math.random() <= Config.blazingBrandChance) {
            float factor = (float)Config.lifestealMultiplier;

            SpartanCataclysm.LOGGER.info(source.type().msgId());
            SpartanCataclysm.LOGGER.info(ModDamageTypes.KEY_THROWN_WEAPON_PLAYER.toString());
            SpartanCataclysm.LOGGER.info(ModDamageTypes.KEY_THROWN_WEAPON_MOB.toString());

            if (factor > 0.0f) {
                if (source.type().msgId().equals(ModDamageTypes.KEY_THROWN_WEAPON_PLAYER.toString())
                        || source.type().msgId().equals(ModDamageTypes.KEY_THROWN_WEAPON_PLAYER.toString())) {
                    factor = factor * 2;
                } else if (attacker instanceof Player player) {
                    float speed = (float) player.getAttributeValue(Attributes.ATTACK_SPEED);
                    factor = factor / Mth.clamp(speed, 0.5f, 2);
                }
            }

            stackBlazingBrand(attacker, victim, factor);
        }
    }

    private static void stackBlazingBrand(LivingEntity attacker, LivingEntity target, float factor) {
        try {
            var eff = ModEffect.EFFECTBLAZING_BRAND.get();
            var old = target.getEffect(eff);
            int i = old == null ? 0 : Math.min(4, old.getAmplifier() + 1);
            target.addEffect(new MobEffectInstance(eff, 240, i));
            if (factor > 0.0f) attacker.heal(factor * (float) CMConfig.IgnisHealingMultiplier * (float) (i + 1));
            if (Config.customSounds) attacker.level().playSeededSound(null, target.getX(), target.getY(), target.getZ(),
                    SCSounds.IGNITIUM_HIT.get(), SoundSource.PLAYERS, 1f, (float)((Math.random() * 0.2) + 0.9), 0);
        } catch (Throwable e) {
            SpartanCataclysm.LOGGER.error(String.valueOf(e));
        }
    }

}