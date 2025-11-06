package dev.cephelo.spartancataclysm;

import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.init.ModTag;
import com.mojang.logging.LogUtils;
import com.oblivioussp.spartanweaponry.api.WeaponTraits;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import dev.cephelo.spartancataclysm.effects.SCEffects;
import dev.cephelo.spartancataclysm.sounds.SCSounds;
import krelox.spartantoolkit.*;
import dev.cephelo.spartancataclysm.traits.*;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;
import org.slf4j.Logger;

import com.github.L_Ender.cataclysm.init.ModItems;
import com.github.L_Ender.cataclysm.items.Tooltier;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@Mod(SpartanCataclysm.MODID)
public class SpartanCataclysm extends SpartanAddon {
    public static final String MODID = "spartancataclysm";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final WeaponMap WEAPONS = new WeaponMap();
    public static final DeferredRegister<Item> ITEMS = itemRegister(MODID);
    public static final DeferredRegister<WeaponTrait> TRAITS = traitRegister(MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = tabRegister(MODID);

    // Tiers
    public static final Tier IGNITIUM_TIER = TierSortingRegistry.registerTier(
            new ForgeTier(5, -1, 9F, 7F, 20,
                    BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.of(ModItems.IGNITIUM_INGOT.get())),
            new ResourceLocation(Cataclysm.MODID, "ignitium_ingot"), List.of(Tiers.NETHERITE), List.of());

    public static final Tier CURSIUM_TIER = TierSortingRegistry.registerTier(
            new ForgeTier(5, -1, 9F, 6F, 20,
                    BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.of(ModItems.CURSIUM_INGOT.get())),
            new ResourceLocation(Cataclysm.MODID, "cursium_ingot"), List.of(Tiers.NETHERITE), List.of(IGNITIUM_TIER));

    public static final Tier WITHERITE_TIER = TierSortingRegistry.registerTier(
            new ForgeTier(5, -1, 7F, 6F, 18,
                    BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.of(ModItems.WITHERITE_INGOT.get())),
            new ResourceLocation(Cataclysm.MODID, "witherite_ingot"), List.of(Tiers.NETHERITE), List.of(CURSIUM_TIER));


    // Traits
    public static final RegistryObject<WeaponTrait> BLAZING_BRAND = registerTrait(TRAITS, new BlazingBrandTrait());
    public static final RegistryObject<WeaponTrait> ACCURSED_RAGE = registerTrait(TRAITS, new AccursedRageTrait());
    public static final RegistryObject<WeaponTrait> MECHA_PULSE = registerTrait(TRAITS, new MechaPulseTrait());
    public static final RegistryObject<WeaponTrait> MECHA_SMITE = registerTrait(TRAITS, new MechaSmiteTrait());
    public static final RegistryObject<WeaponTrait> UNBREAKABLE = registerTrait(TRAITS, new UnbreakableTrait());

    // Materials
    public static final ArrayList<SpartanMaterial> MATERIALS = new ArrayList<>();

    public static final SpartanMaterial ANCIENT_METAL = material("ancient_metal", Tooltier.ANCIENT_METAL, "spartancataclysm:ancient_metal_ingots");
    public static final SpartanMaterial BLACK_STEEL = material("black_steel", Tooltier.BLACK_STEEL, "spartancataclysm:black_steel_ingots");
    public static final SpartanMaterial CURSIUM = material("cursium", CURSIUM_TIER, "spartancataclysm:cursium_ingots", ACCURSED_RAGE, WeaponTraits.FIREPROOF, UNBREAKABLE).setRarity(Rarity.EPIC);
    public static final SpartanMaterial IGNITIUM = material("ignitium", IGNITIUM_TIER, "spartancataclysm:ignitium_ingots", BLAZING_BRAND, WeaponTraits.FIREPROOF, UNBREAKABLE).setRarity(Rarity.EPIC);
    public static final SpartanMaterial WITHERITE = material("witherite", WITHERITE_TIER, "spartancataclysm:witherite_ingots", MECHA_SMITE, MECHA_PULSE, WeaponTraits.FIREPROOF, UNBREAKABLE).setRarity(Rarity.EPIC);

    @SafeVarargs
    private static SpartanMaterial material(String name, Tier tier, String tagPath, RegistryObject<WeaponTrait>... traits) {
        SpartanMaterial material = new SpartanMaterial(name, MODID, tier, ItemTags.create(new ResourceLocation(tagPath)), traits);
        MATERIALS.add(material);
        return material;
    }

    @SuppressWarnings("unused")
    public static final RegistryObject<CreativeModeTab> SPARTAN_CATACLYSM_TAB = registerTab(TABS, MODID,
            () -> WEAPONS.get(IGNITIUM, WeaponType.GREATSWORD).get(),
            (parameters, output) -> ITEMS.getEntries().forEach(item -> output.accept(item.get())));

    public SpartanCataclysm() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SPEC);

        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        SCEffects.register(bus);
        SCSounds.register(bus);

        registerSpartanWeapons(ITEMS);
        ITEMS.register(bus);
        TRAITS.register(bus);
        TABS.register(bus);
    }

    // Weapon item tags
    public static final TagKey<Item> ANCIENT_METAL_WEAPONS = registerItemTag("ancient_metal_weapons");
    public static final TagKey<Item> BLACK_STEEL_WEAPONS = registerItemTag("black_steel_weapons");
    public static final TagKey<Item> CURSIUM_WEAPONS = registerItemTag("cursium_weapons");
    public static final TagKey<Item> IGNITIUM_WEAPONS = registerItemTag("ignitium_weapons");
    public static final TagKey<Item> WITHERITE_WEAPONS = registerItemTag("witherite_weapons");

    private static TagKey<Item> registerItemTag(String name) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(SpartanCataclysm.MODID, name));
    }

    @Override
    protected void addItemTags(ItemTagsProvider provider, Function<TagKey<Item>, IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item>> tag) {
        Function<SpartanMaterial, Item[]> func = material -> WEAPONS.entrySet().stream()
                .filter(entry -> entry.getKey().first().equals(material))
                .map(entry -> entry.getValue().get())
                .toArray(Item[]::new);
        tag.apply(ModTag.EXPLOSION_IMMUNE_ITEM)
                .add(func.apply(CURSIUM))
                .add(func.apply(IGNITIUM))
                .add(func.apply(WITHERITE));

        tag.apply(ANCIENT_METAL_WEAPONS).add(func.apply(ANCIENT_METAL));
        tag.apply(BLACK_STEEL_WEAPONS).add(func.apply(BLACK_STEEL));
        tag.apply(CURSIUM_WEAPONS).add(func.apply(CURSIUM));
        tag.apply(IGNITIUM_WEAPONS).add(func.apply(IGNITIUM));
        tag.apply(WITHERITE_WEAPONS).add(func.apply(WITHERITE));
    }

    private void generateSmeltingRecipes(Consumer<FinishedRecipe> consumer, TagKey<Item> tag, Item result, String key) {
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(tag), RecipeCategory.MISC, result, 1, 100)
                .unlockedBy("criteria", RecipeUnlockedTrigger.unlocked(ResourceLocation.tryParse("spartancataclysm:" + key + "_nugget_from_blasting_" + key + "_weapons")))
                .save(consumer, "spartancataclysm:" + key + "_nugget_from_blasting_" + key + "_weapons");

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(tag), RecipeCategory.MISC, result, 1, 200)
                .unlockedBy("criteria", RecipeUnlockedTrigger.unlocked(ResourceLocation.tryParse("spartancataclysm:" + key + "_nugget_from_smelting_" + key + "_weapons")))
                .save(consumer, "spartancataclysm:" + key + "_nugget_from_smelting_" + key + "_weapons");
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        generateSmeltingRecipes(consumer, ANCIENT_METAL_WEAPONS, ModItems.ANCIENT_METAL_NUGGET.get(), "ancient_metal");
        generateSmeltingRecipes(consumer, BLACK_STEEL_WEAPONS, ModItems.BLACK_STEEL_NUGGET.get(), "black_steel");
        generateSmeltingRecipes(consumer, WITHERITE_WEAPONS, ModItems.WITHERITE_INGOT.get(), "witherite");

        WEAPONS.forEach((key, item) -> {
            SpartanMaterial material = key.first();
            WeaponType type = key.second();
            if (material.equals(IGNITIUM)) {
                SmithingTransformRecipeBuilder
                        .smithing(Ingredient.of(ModItems.IGNITIUM_UPGARDE_SMITHING_TEMPLATE.get()), Ingredient.of(ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse("spartanweaponry:netherite_" + type.toString().toLowerCase()))),
                                Ingredient.of(ModItems.IGNITIUM_INGOT.get()), RecipeCategory.COMBAT, item.get())
                        .unlocks("has_" + ModItems.IGNITIUM_UPGARDE_SMITHING_TEMPLATE.getId().getPath(), has(ModItems.IGNITIUM_UPGARDE_SMITHING_TEMPLATE.get()))
                        .save(consumer, item.getId().withSuffix("_smithing"));
            } else if (material.equals(CURSIUM)) {
                SmithingTransformRecipeBuilder
                        .smithing(Ingredient.of(ModItems.CURSIUM_UPGARDE_SMITHING_TEMPLATE.get()), Ingredient.of(ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse("spartanweaponry:netherite_" + type.toString().toLowerCase()))),
                                Ingredient.of(ModItems.CURSIUM_INGOT.get()), RecipeCategory.COMBAT, item.get())
                        .unlocks("has_" + ModItems.CURSIUM_UPGARDE_SMITHING_TEMPLATE.getId().getPath(), has(ModItems.CURSIUM_UPGARDE_SMITHING_TEMPLATE.get()))
                        .save(consumer, item.getId().withSuffix("_smithing"));
            } else type.recipe.accept(WEAPONS, consumer, material);
        });
    }

    @Override
    protected void addTranslations(LanguageProvider provider, Function<RegistryObject<?>, String> formatName) {
        super.addTranslations(provider, formatName);
        provider.addEffect(SCEffects.ACCURSED_RAGE, "Accursed Rage");
        provider.addEffect(SCEffects.PULSE_CHARGE, "Pulse Charge");
        provider.addEffect(SCEffects.PULSE_COOLDOWN, "Pulse Cooldown");
        provider.add("effect.spartancataclysm.accursed_rage.desc", "You deal more damage with Cursium weapons.");
        provider.add("effect.spartancataclysm.pulse_charge.desc", "A shockwave is released at a sufficient amplifier.");
        provider.add("effect.spartancataclysm.pulse_cooldown.desc", "Pulse Charge cannot be accumulated until this effect ceases.");
        provider.add("sounds.spartancataclysm.accursed_rage_max", "Accursed Rage reaches maximum");
        provider.add("sounds.spartancataclysm.mecha_pulse_shockwave", "Shockwave released");
    }

    @Override
    public String modid() {
        return MODID;
    }

    @Override
    public List<SpartanMaterial> getMaterials() {
        return MATERIALS;
    }

    @Override
    public WeaponMap getWeaponMap() {
        return WEAPONS;
    }
}