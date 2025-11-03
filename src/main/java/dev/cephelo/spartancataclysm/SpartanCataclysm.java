package dev.cephelo.spartancataclysm;

import com.mojang.logging.LogUtils;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import dev.cephelo.spartancataclysm.effects.SCEffects;
import dev.cephelo.spartancataclysm.sounds.SCSounds;
import krelox.spartantoolkit.*;
import dev.cephelo.spartancataclysm.traits.*;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;
import org.slf4j.Logger;

import com.github.L_Ender.cataclysm.items.ModItemTier;
import com.github.L_Ender.cataclysm.init.ModItems;
import com.github.L_Ender.cataclysm.items.Tooltier;

import java.util.*;
import java.util.function.Consumer;

@Mod(SpartanCataclysm.MODID)
public class SpartanCataclysm extends SpartanAddon {
    public static final String MODID = "spartancataclysm";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final WeaponMap WEAPONS = new WeaponMap();
    public static final DeferredRegister<Item> ITEMS = itemRegister(MODID);
    public static final DeferredRegister<WeaponTrait> TRAITS = traitRegister(MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = tabRegister(MODID);

    // Traits
    public static final RegistryObject<WeaponTrait> BLAZING_BRAND = registerTrait(TRAITS, new BlazingBrandTrait());
    public static final RegistryObject<WeaponTrait> ACCURSED_RAGE = registerTrait(TRAITS, new AccursedRageTrait());
    public static final RegistryObject<WeaponTrait> MECHA_PULSE = registerTrait(TRAITS, new MechaPulseTrait());

    // Materials
    public static final ArrayList<SpartanMaterial> MATERIALS = new ArrayList<>();

    public static final SpartanMaterial ANCIENT_METAL = material("ancient_metal", Tooltier.ANCIENT_METAL, "spartancataclysm:ancient_metal_ingots");
    public static final SpartanMaterial BLACK_STEEL = material("black_steel", Tooltier.BLACK_STEEL, "spartancataclysm:black_steel_ingots");
    public static final SpartanMaterial CURSIUM = material("cursium", Tiers.NETHERITE, "spartancataclysm:cursium_ingots", ACCURSED_RAGE).setRarity(Rarity.EPIC);
    public static final SpartanMaterial IGNITIUM = material("ignitium", Tiers.NETHERITE, "spartancataclysm:ignitium_ingots", BLAZING_BRAND).setRarity(Rarity.EPIC);
    public static final SpartanMaterial WITHERITE = material("witherite", ModItemTier.TOOL_WITHERITE, "spartancataclysm:witherite_ingots", MECHA_PULSE).setRarity(Rarity.EPIC);

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
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        SCEffects.register(bus);
        SCSounds.register(bus);

        registerSpartanWeapons(ITEMS);
        ITEMS.register(bus);
        TRAITS.register(bus);
        TABS.register(bus);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        WEAPONS.forEach((key, item) -> {
            SpartanMaterial material = key.first();
            WeaponType type = key.second();
            if (material.equals(IGNITIUM)) {
                SmithingTransformRecipeBuilder
                        .smithing(Ingredient.of(ModItems.IGNITIUM_UPGARDE_SMITHING_TEMPLATE.get()), Ingredient.of(WEAPONS.get((SpartanMaterial) SpartanMaterial.NETHERITE, type).get()),
                                Ingredient.of(ModItems.IGNITIUM_INGOT.get()), RecipeCategory.COMBAT, item.get())
                        .unlocks("has_" + ModItems.IGNITIUM_UPGARDE_SMITHING_TEMPLATE.getId().getPath(), has(ModItems.IGNITIUM_UPGARDE_SMITHING_TEMPLATE.get()))
                        .save(consumer, item.getId().withSuffix("_smithing"));
            } else if (material.equals(CURSIUM)) {
                SmithingTransformRecipeBuilder
                        .smithing(Ingredient.of(ModItems.CURSIUM_UPGARDE_SMITHING_TEMPLATE.get()), Ingredient.of(WEAPONS.get((SpartanMaterial) SpartanMaterial.NETHERITE, type).get()),
                                Ingredient.of(ModItems.CURSIUM_INGOT.get()), RecipeCategory.COMBAT, item.get())
                        .unlocks("has_" + ModItems.CURSIUM_UPGARDE_SMITHING_TEMPLATE.getId().getPath(), has(ModItems.CURSIUM_UPGARDE_SMITHING_TEMPLATE.get()))
                        .save(consumer, item.getId().withSuffix("_smithing"));
            } else {
                type.recipe.accept(WEAPONS, consumer, material);
                this.getWeaponMap().forEach((pair, itemW) -> ((WeaponType)pair.second()).recipe.accept(this.getWeaponMap(), consumer, (SpartanMaterial)pair.first()));
            }
        });
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