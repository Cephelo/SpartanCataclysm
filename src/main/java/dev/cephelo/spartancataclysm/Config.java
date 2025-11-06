package dev.cephelo.spartancataclysm;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = SpartanCataclysm.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{

    private static final ForgeConfigSpec.BooleanValue
            MECHA_SMITE_REGEN_THRESHOLD_TYPE,
            CUSTOM_SOUNDS;

    private static final ForgeConfigSpec.DoubleValue
            ACCURSED_RAGE_CHANCE,
            ACCURSED_RAGE_EXTRA_DAMAGE,
            BLAZING_BRAND_CHANCE,
            MECHA_SMITE_CHANCE,
            MECHA_SMITE_REGEN_CHANCE,
            LIFESTEAL_MULTIPLIER,
            MECHA_PULSE_CHARGE_CHANCE,
            MECHA_PULSE_EXTRA_DAMAGE,
            MECHA_SMITE_REGEN_THRESHOLD_PERCENT,
            PITCH_VARIATION;

    private static final ForgeConfigSpec.IntValue
            ACCURSED_RAGE_DURATION,
            ACCURSED_RAGE_MAXIMUM,
            BLAZING_BRAND_DURATION,
            BLAZING_BRAND_MAXIMUM,
            MECHA_PULSE_EFFECT_DURATION,
            MECHA_PULSE_STUN_THRESHOLD,
            MECHA_PULSE_COOLDOWN,
            MECHA_PULSE_STUN_DURATION,
            MECHA_SMITE_FIRE_DURATION,
            MECHA_SMITE_WITHER_DURATION,
            MECHA_SMITE_WITHER_AMP,
            MECHA_SMITE_REGEN_THRESHOLD,
            MECHA_SMITE_REGEN_DURATION,
            MECHA_SMITE_REGEN_AMP;

    static final ForgeConfigSpec SPEC;

    static {
        final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        BUILDER.comment(" SPARTAN CATACLYSM CONFIG\n");

        // ACCURSED RAGE OPTIONS
        BUILDER.push("Accursed Rage Options");

        ACCURSED_RAGE_CHANCE = BUILDER
                .comment("Chance for Cursium weapons to stack Accursed Rage on the user.  Set to 0 to disable")
                .defineInRange("accursedRageChance", 0.66, 0.0, Double.MAX_VALUE);

        ACCURSED_RAGE_EXTRA_DAMAGE = BUILDER
                .comment("Additional damage attackers do with Cursium weapons per level of Accursed Rage.")
                .defineInRange("accursedRageExtraDamage", 1.0, 0, Double.MAX_VALUE);

        ACCURSED_RAGE_DURATION = BUILDER
                .comment("Duration (ticks) of the Accursed Rage effect granted by Cursium weapons.")
                .defineInRange("accursedRageDuration", 110, 0, Integer.MAX_VALUE);

        ACCURSED_RAGE_MAXIMUM = BUILDER
                .comment("Maximum amplifier the Accursed Rage effect can reach using Cursium weapons.")
                .defineInRange("accursedRageMaxAmplifier", 4, 0, Integer.MAX_VALUE);

        BUILDER.pop();

        // BLAZING BRAND OPTIONS
        BUILDER.push("Blazing Brand Options");

        BLAZING_BRAND_CHANCE = BUILDER
                .comment("Chance for Ignitium weapons to stack Blazing Brand onto attacked entities.  Set to 0 to disable")
                .defineInRange("blazingBrandChance", 0.75, 0.0, Double.MAX_VALUE);

        LIFESTEAL_MULTIPLIER = BUILDER
                .comment("Lifesteal multiplier for Ignitium weapons.  Lifesteal amount depends on attack speed, so rate should correlates with DPS.  Set to 0 to disable lifesteal.")
                .defineInRange("lifestealMultiplier", 1.25, 0.0, Double.MAX_VALUE);

        BLAZING_BRAND_DURATION = BUILDER
                .comment("Duration (ticks) of the Blazing Brand effect inflicted by Ignitium weapons.  Default is identical to Ignis' abilities.")
                .defineInRange("blazingBrandDuration", 240, 0, Integer.MAX_VALUE);

        BLAZING_BRAND_MAXIMUM = BUILDER
                .comment("Maximum amplifier the Blazing Brand effect can reach using Ignitium weapons.  Default is identical to Ignis' abilities.")
                .defineInRange("blazingBrandMaxAmplifier", 4, 0, Integer.MAX_VALUE);

        BUILDER.pop();

        // MECHA PULSE OPTIONS
        BUILDER.push("Mecha Pulse Options");

        MECHA_PULSE_CHARGE_CHANCE = BUILDER
                .comment("Chance for Witherite weapons to stack the Pulse Charge effect on the user.  Set to 0 to disable")
                .defineInRange("mechaPulseChargeChance", 0.75, 0.0, Double.MAX_VALUE);

        MECHA_PULSE_EFFECT_DURATION = BUILDER
                .comment("Duration (ticks) of the Pulse Charge effect granted by Witherite weapons.")
                .defineInRange("mechaPulseEffectDuration", 160, 0, Integer.MAX_VALUE);

        MECHA_PULSE_STUN_THRESHOLD = BUILDER
                .comment("Amplifier of the Pulse Charge effect at which a shockwave is released, stunning the target.")
                .defineInRange("mechaPulseStunThreshold", 9, 0, Integer.MAX_VALUE);

        MECHA_PULSE_COOLDOWN = BUILDER
                .comment("Duration (ticks) of the Pulse Cooldown effect inflicted upon Witherite weapons releasing a shockwave.  Pulse Charge cannot accumulate while this effect is active.")
                .defineInRange("mechaPulseCooldown", 900, 0, Integer.MAX_VALUE);

        MECHA_PULSE_STUN_DURATION = BUILDER
                .comment("Stun duration (ticks) inflicted by Witherite weapons when a shockwave is released.")
                .defineInRange("mechaPulseStunDuration", 200, 0, Integer.MAX_VALUE);

        MECHA_PULSE_EXTRA_DAMAGE = BUILDER
                .comment("Additional damage a shockwave does when released.")
                .defineInRange("mechaPulseStunDamage", 4.0, 0.0, Double.MAX_VALUE);

        BUILDER.pop();

        // MECHA SMITE OPTIONS
        BUILDER.push("Mecha Smite Options");

        MECHA_SMITE_CHANCE = BUILDER
                .comment("Chance for Witherite weapons to set the target on fire and apply Wither.  Set to 0 to disable")
                .defineInRange("mechaSmiteHarmfulEffectsChance", 1.0, 0.0, Double.MAX_VALUE);

        MECHA_SMITE_FIRE_DURATION = BUILDER
                .comment("Duration (seconds) of the Fire applied by Witherite weapons.  Set to 0 to disable")
                .defineInRange("mechaSmiteFireDuration", 5, 0, Integer.MAX_VALUE);

        MECHA_SMITE_WITHER_DURATION = BUILDER
                .comment("Duration (ticks) of the Wither effect applied to the target by Witherite weapons.  Set to 0 to disable")
                .defineInRange("mechaSmiteWitherDuration", 100, 0, Integer.MAX_VALUE);

        MECHA_SMITE_WITHER_AMP = BUILDER
                .comment("Amplifier of the Wither effect applied to the target by Witherite weapons.")
                .defineInRange("mechaSmiteWitherAmplifier", 1, 0, Integer.MAX_VALUE);

        MECHA_SMITE_REGEN_CHANCE = BUILDER
                .comment("Chance for Witherite weapons apply regeneration to the user when under health threshold.  Set to 0 to disable")
                .defineInRange("mechaSmiteRegenChance", 0.5, 0.0, Double.MAX_VALUE);

        MECHA_SMITE_REGEN_THRESHOLD_TYPE = BUILDER
                .comment("Whether Witherite weapons should apply Regeneration to the user at a certain health percentage (true), or at a defined health amount (false)")
                .define("mechaSmiteRegenUsesPercentage", true);

        MECHA_SMITE_REGEN_THRESHOLD_PERCENT = BUILDER
                .comment("If mechaSmiteRegenUsesPercentage is true, Witherite weapons will apply Regeneration to the user when under this percentage of health.  Set to 0 to disable")
                .defineInRange("mechaSmiteRegenPercentage", 0.5, 0.0, Double.MAX_VALUE);

        MECHA_SMITE_REGEN_THRESHOLD = BUILDER
                .comment("If mechaSmiteRegenUsesPercentage is false, Witherite weapons will apply Regeneration to the user when under this amount of health.  Set to 0 to disable")
                .defineInRange("mechaSmiteRegenThreshold", 10, 0, Integer.MAX_VALUE);

        MECHA_SMITE_REGEN_DURATION = BUILDER
                .comment("Duration (ticks) of the Regeneration effect applied to the user by Witherite weapons.")
                .defineInRange("mechaSmiteRegenDuration", 100, 0, Integer.MAX_VALUE);

        MECHA_SMITE_REGEN_AMP = BUILDER
                .comment("Amplifier of the Regeneration effect applied to the user by Witherite weapons.")
                .defineInRange("mechaSmiteRegenAmplifier", 1, 0, Integer.MAX_VALUE);

        BUILDER.pop();

        // MISC OPTIONS
        BUILDER.push("Miscellaneous Options");

        CUSTOM_SOUNDS = BUILDER
                .comment("Enable/Disable Ignitium, Cursium, and Witherite weapons making custom sounds when attacking.")
                .define("customWeaponSounds", true);

        PITCH_VARIATION = BUILDER
                .comment("Pitch variation from 1.0 for custom weapon attack sounds.  For example: Set to 0.1, pitch will be random between 0.9 and 1.1.  Set to 0.0 for no variation.")
                .defineInRange("customWeaponSoundsPitchVariation", 0.1, 0.0, 1.0);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static double accursedRageChance;
    public static double blazingBrandChance;
    public static double accursedRageExtraDamage;
    public static int accursedRageDuration;
    public static int accursedRageMaximum;
    public static double lifestealMultiplier;
    public static int blazingBrandDuration;
    public static int blazingBrandMaximum;

    public static double mechaPulseChargeChance;
    public static int mechaPulseStunThreshold;
    public static int mechaPulseCooldown;
    public static int mechaPulseStunDuration;
    public static int mechaPulseEffectDuration;
    public static double mechaPulseExtraDamage;

    public static double mechaSmiteChance;
    public static double mechaSmiteRegenChance;
    public static int mechaSmiteFireDuration;
    public static int mechaSmiteWitherDuration;
    public static int mechaSmiteWitherAmplifier;
    public static boolean mechaSmiteRegenThresholdType;
    public static double mechaSmiteRegenThresholdPercent;
    public static int mechaSmiteRegenThreshold;
    public static int mechaSmiteRegenDuration;
    public static int mechaSmiteRegenAmplifier;

    public static boolean customSounds;
    public static double pitchVariation;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        accursedRageChance = ACCURSED_RAGE_CHANCE.get();
        blazingBrandChance = BLAZING_BRAND_CHANCE.get();
        accursedRageExtraDamage = ACCURSED_RAGE_EXTRA_DAMAGE.get();
        accursedRageDuration = ACCURSED_RAGE_DURATION.get();
        accursedRageMaximum = ACCURSED_RAGE_MAXIMUM.get();
        lifestealMultiplier = LIFESTEAL_MULTIPLIER.get();
        blazingBrandDuration = BLAZING_BRAND_DURATION.get();
        blazingBrandMaximum = BLAZING_BRAND_MAXIMUM.get();

        mechaPulseChargeChance = MECHA_PULSE_CHARGE_CHANCE.get();
        mechaPulseStunThreshold = MECHA_PULSE_STUN_THRESHOLD.get();
        mechaPulseCooldown = MECHA_PULSE_COOLDOWN.get();
        mechaPulseStunDuration = MECHA_PULSE_STUN_DURATION.get();
        mechaPulseEffectDuration = MECHA_PULSE_EFFECT_DURATION.get();
        mechaPulseExtraDamage = MECHA_PULSE_EXTRA_DAMAGE.get();

        mechaSmiteChance = MECHA_SMITE_CHANCE.get();
        mechaSmiteRegenChance = MECHA_SMITE_REGEN_CHANCE.get();
        mechaSmiteFireDuration = MECHA_SMITE_FIRE_DURATION.get();
        mechaSmiteWitherDuration = MECHA_SMITE_WITHER_DURATION.get();
        mechaSmiteWitherAmplifier = MECHA_SMITE_WITHER_AMP.get();
        mechaSmiteRegenThresholdType = MECHA_SMITE_REGEN_THRESHOLD_TYPE.get();
        mechaSmiteRegenThresholdPercent = MECHA_SMITE_REGEN_THRESHOLD_PERCENT.get();
        mechaSmiteRegenThreshold = MECHA_SMITE_REGEN_THRESHOLD.get();
        mechaSmiteRegenDuration = MECHA_SMITE_REGEN_DURATION.get();
        mechaSmiteRegenAmplifier = MECHA_SMITE_REGEN_AMP.get();

        customSounds = CUSTOM_SOUNDS.get();
        pitchVariation = PITCH_VARIATION.get();
    }

    public static float getRandomPitch() {
        return (float)((Math.random() * pitchVariation * 2.0) - pitchVariation + 1.0);
    }
}