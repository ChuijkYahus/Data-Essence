package EsetKalenko.Halcyon.api.item;

import EsetKalenko.Halcyon.api.DataNEssenceRegistries;
import EsetKalenko.Halcyon.api.essence.EssenceType;
import EsetKalenko.Halcyon.registry.DataComponentRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.IItemDecorator;

import java.util.ArrayList;
import java.util.List;

public class ItemDecorators {

    public static final IItemDecorator essenceBarDecoration = (guiGraphics, font, stack, xOffset, yOffset) -> {
        if (!stack.isEmpty()) {
            if (stack.has(DataComponentRegistry.ESSENCE_STORAGE)) {
                ItemEssenceContainer essenceStorage = stack.get(DataComponentRegistry.ESSENCE_STORAGE);
                List<ResourceLocation> essenceTypes = new ArrayList<>(essenceStorage.storedEssence.keySet());
                if (!essenceTypes.isEmpty()) {
                    int typeIndex = (int)((Minecraft.getInstance().level.getGameTime()/20) % essenceTypes.size());
                    ResourceLocation essence = essenceTypes.get(typeIndex);
                    EssenceType essenceType = DataNEssenceRegistries.ESSENCE_TYPE_REGISTRY.get(essence);
                    if (essenceType != null) {
                        guiGraphics.pose().pushPose();
                        int width = (int) Math.floor((ItemEssenceContainer.getEssence(stack, essence) / ItemEssenceContainer.getMaxEssence(stack)) * 13f);
                        int color = essenceType.color;
                        int x = xOffset + 2;
                        int k = yOffset + 13;
                        if (stack.isBarVisible()) {
                            k -= 2;
                        }
                        guiGraphics.fill(RenderType.guiOverlay(), x, k, x + 13, k + 2, -16777216);
                        guiGraphics.fill(RenderType.guiOverlay(), x, k, x + width, k + 1, (color & 0x00ffffff) | (255 << 24));
                        guiGraphics.pose().popPose();
                        return true;
                    }
                }
            }
        }
        return false;
    };

    // Renders the output item of a Shaping Impression
    public static final IItemDecorator shapingImpression = (guiGraphics, font, stack, xOffset, yOffset) -> {
        var player = Minecraft.getInstance().player;
        var level = Minecraft.getInstance().level;
        if ( player == null || level == null )
            return false;

        if ( stack.getComponents().has( DataComponentRegistry.SHAPING_IMPRESSION.get() ) ) {
            var recipeLocation = stack.get( DataComponentRegistry.SHAPING_IMPRESSION );
            if (recipeLocation == null)
                return false;

            var recipe = level.getRecipeManager().byKey(recipeLocation);
            if (recipe.isPresent()) {
                var impression = recipe.get().value().getResultItem( level.registryAccess() );
                PoseStack poseStack = guiGraphics.pose();
                poseStack.pushPose();
                poseStack.translate(xOffset, yOffset, 100);
                var scale = player.isShiftKeyDown() ? 0.8f : 0.5f;
                poseStack.scale(scale, scale, scale);
                guiGraphics.renderItem(impression, 16, 0);
                poseStack.popPose();
                return true;
            }
        }

        return false;
    };
}
