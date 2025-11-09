package dev.cephelo.spartancataclysm.effects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;

public class CustomIconMobEffectExtensions implements IClientMobEffectExtensions {
    private final MobEffect effect;

    public CustomIconMobEffectExtensions(MobEffect eff) {
        this.effect = eff;
    }

    public boolean renderInventoryIcon(MobEffectInstance instance, EffectRenderingInventoryScreen<?> screen, GuiGraphics guiGraphics, int x, int y, int blitOffset)
    {
        TextureAtlasSprite textureatlassprite = Minecraft.getInstance().getMobEffectTextures().get(this.effect);
        guiGraphics.blit(x, y + 7, 0, 18, 18, textureatlassprite);
        return true;
    }

    public boolean renderGuiIcon(MobEffectInstance instance, Gui gui, GuiGraphics guiGraphics, int x, int y, float z, float alpha)
    {
        TextureAtlasSprite textureatlassprite = Minecraft.getInstance().getMobEffectTextures().get(this.effect);

        guiGraphics.setColor(1.0F, 1.0F, 1.0F, alpha);
        guiGraphics.blit(x + 3, y + 3, 0, 18, 18, textureatlassprite);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        return true;
    }
}