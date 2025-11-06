package dev.cephelo.spartancataclysm.traits;

import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import dev.cephelo.spartancataclysm.SpartanCataclysm;
import krelox.spartantoolkit.BetterWeaponTrait;

public class UnbreakableTrait extends BetterWeaponTrait {
    public UnbreakableTrait() {
        super("unbreakable", SpartanCataclysm.MODID, WeaponTrait.TraitQuality.POSITIVE);
        setUniversal();
    }

    // Does nothing - durability is managed in the main file via custom tiers
    // This is just here so players know

    @Override
    public String getDescription() {
        return "Has no durability, never breaks";
    }
}