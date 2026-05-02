package EsetKalenko.Halcyon.item.equipment;

import com.cmdpro.databank.rendering.ColorUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class TransGrapplingHook extends GrapplingHook {
    public TransGrapplingHook(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public Color getHookColor(Player player, ItemStack stack, float partialTick) {
        Color blue = new Color(0xff6ab3fc);
        Color white = new Color(0xffffffff);
        Color pink = new Color(0xfffc92bb);
        int transitionTicks = 200;
        float transitionProgress = (player.level().getGameTime() % transitionTicks)+partialTick;
        transitionProgress = transitionProgress/(transitionTicks/4f);
        if (transitionProgress <= 1f) {
            return ColorUtil.blendColors(blue, white, transitionProgress);
        } else if (transitionProgress <= 2f) {
            return ColorUtil.blendColors(white, pink, transitionProgress-1f);
        } else if (transitionProgress <= 3f) {
            return ColorUtil.blendColors(pink, white, transitionProgress-2f);
        } else if (transitionProgress <= 4f) {
            return ColorUtil.blendColors(white, blue, transitionProgress-3f);
        }
        return white;
    }
}
