package EsetKalenko.Halcyon.api.essence;

import EsetKalenko.Halcyon.Halcyon;
import net.minecraft.resources.ResourceLocation;

public class EssenceBarBackgroundType {
    public static final ResourceLocation ESSENCE_BAR_BACKGROUND_LOCATION = Halcyon.locate("textures/gui/essence_bar_backgrounds.png");
    public final EssenceType.EssenceBarSpriteLocation tinyBarSprite, bigBarSprite, iconSprite, unknownIconSprite;
    public EssenceBarBackgroundType(EssenceType.EssenceBarSpriteLocation tinyBarSprite, EssenceType.EssenceBarSpriteLocation bigBarSprite, EssenceType.EssenceBarSpriteLocation iconSprite, EssenceType.EssenceBarSpriteLocation unknownIconSprite) {
        this.tinyBarSprite = tinyBarSprite;
        this.bigBarSprite = bigBarSprite;
        this.iconSprite = iconSprite;
        this.unknownIconSprite = unknownIconSprite;
    }
}
