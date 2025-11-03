package dev.cephelo.spartancataclysm;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = SpartanCataclysm.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.DoubleValue ACCURSED_RAGE_CHANCE = BUILDER
            .comment("Chance for Cursium weapons to stack Accursed Rage on the user.  Set to 0 to disable")
            .defineInRange("accursedRageChance", 0.75, 0.0, Double.MAX_VALUE);

    private static final ForgeConfigSpec.DoubleValue BLAZING_BRAND_CHANCE = BUILDER
            .comment("Chance for Ignitium weapons to stack Blazing Brand onto attacked entities.  Set to 0 to disable")
            .defineInRange("blazingBrandChance", 0.75, 0.0, Double.MAX_VALUE);

    private static final ForgeConfigSpec.DoubleValue MECHA_PULSE_CHANCE = BUILDER
            .comment("Chance for Witherite weapons to set the target on fire and apply Wither.  Set to 0 to disable")
            .defineInRange("mechaPulseHarmfulEffectsChance", 1.0, 0.0, Double.MAX_VALUE);

    private static final ForgeConfigSpec.DoubleValue MECHA_PULSE_REGEN_CHANCE = BUILDER
            .comment("Chance for Witherite weapons apply wither to the user when under half health.  Set to 0 to disable")
            .defineInRange("mechaPulseRegenChance", 0.5, 0.0, Double.MAX_VALUE);

    private static final ForgeConfigSpec.DoubleValue ACCURSED_RAGE_EXTRA_DAMAGE = BUILDER
            .comment("Additional damage attackers do with Cursium weapons per level of Accursed Rage.")
            .defineInRange("accursedRageExtraDamage", 2.0, 0.0, Double.MAX_VALUE);

    private static final ForgeConfigSpec.DoubleValue LIFESTEAL_MULTIPLIER = BUILDER
            .comment("Lifesteal multiplier for Ignitium weapons.  Set to 0.0 to disable")
            .defineInRange("lifestealMultiplier", 1.0, 0.0, Double.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue MECHA_PULSE_FIRE_DURATION = BUILDER
            .comment("Duration (seconds) of the Fire applied by Witherite weapons.  Set to 0 to disable.")
            .defineInRange("mechaPulseFireDuration", 5, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue MECHA_PULSE_WITHER_DURATION = BUILDER
            .comment("Duration (ticks) of the Wither effect applied to the target by Witherite weapons.  Set to 0 to disable.")
            .defineInRange("mechaPulseWitherDuration", 100, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue MECHA_PULSE_WITHER_AMP = BUILDER
            .comment("Amplifier of the Wither effect applied to the target by Witherite weapons.")
            .defineInRange("mechaPulseWitherAmplifier", 1, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.BooleanValue MECHA_PULSE_REGEN_THRESHOLD_TYPE = BUILDER
            .comment("Whether Witherite weapons should apply Regeneration to the user at half their max health (true), or at a defined health amount (false)")
            .define("mechaPulseApplyRegenAtHalfHealth", true);

    private static final ForgeConfigSpec.IntValue MECHA_PULSE_REGEN_THRESHOLD = BUILDER
            .comment("If mechaPulseApplyRegenAtHalfHealth is false, Witherite weapons will apply Regeneration to the user when they are under this amount of health.")
            .defineInRange("mechaPulseFireDuration", 10, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue MECHA_PULSE_REGEN_DURATION = BUILDER
            .comment("Duration (ticks) of the Regeneration effect applied to the user by Witherite weapons.")
            .defineInRange("mechaPulseRegenDuration", 100, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue MECHA_PULSE_REGEN_AMP = BUILDER
            .comment("Amplifier of the Regeneration effect applied to the user by Witherite weapons.")
            .defineInRange("mechaPulseRegenAmplifier", 0, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.BooleanValue CUSTOM_SOUNDS = BUILDER
            .comment("Enable/Disable Ignitium, Cursium, and Witherite weapons making custom sounds when hitting.")
            .define("customWeaponSounds", true);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static double accursedRageChance;
    public static double blazingBrandChance;
    public static double accursedRageExtraDamage;
    public static double lifestealMultiplier;

    public static double mechaPulseChance;
    public static double mechaPulseRegenChance;
    public static int mechaPulseFireDuration;
    public static int mechaPulseWitherDuration;
    public static int mechaPulseWitherAmplifier;
    public static boolean mechaPulseRegenThresholdType;
    public static int mechaPulseRegenThreshold;
    public static int mechaPulseRegenDuration;
    public static int mechaPulseRegenAmplifier;

    public static boolean customSounds;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        accursedRageChance = ACCURSED_RAGE_CHANCE.get();
        blazingBrandChance = BLAZING_BRAND_CHANCE.get();
        accursedRageExtraDamage = ACCURSED_RAGE_EXTRA_DAMAGE.get();
        lifestealMultiplier = LIFESTEAL_MULTIPLIER.get();

        mechaPulseChance = MECHA_PULSE_CHANCE.get();
        mechaPulseRegenChance = MECHA_PULSE_REGEN_CHANCE.get();
        mechaPulseFireDuration = MECHA_PULSE_FIRE_DURATION.get();
        mechaPulseWitherDuration = MECHA_PULSE_WITHER_DURATION.get();
        mechaPulseWitherAmplifier = MECHA_PULSE_WITHER_AMP.get();
        mechaPulseRegenThresholdType = MECHA_PULSE_REGEN_THRESHOLD_TYPE.get();
        mechaPulseRegenThreshold = MECHA_PULSE_REGEN_THRESHOLD.get();
        mechaPulseRegenDuration = MECHA_PULSE_REGEN_DURATION.get();
        mechaPulseRegenAmplifier = MECHA_PULSE_REGEN_AMP.get();

        customSounds = CUSTOM_SOUNDS.get();
    }
}
