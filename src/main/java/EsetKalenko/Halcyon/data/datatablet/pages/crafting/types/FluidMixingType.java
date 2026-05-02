package EsetKalenko.Halcyon.data.datatablet.pages.crafting.types;

import EsetKalenko.Halcyon.api.datatablet.CraftingType;
import EsetKalenko.Halcyon.api.essence.EssenceBarBackgroundTypes;
import EsetKalenko.Halcyon.api.util.client.ClientEssenceBarUtil;
import EsetKalenko.Halcyon.data.datatablet.pages.CraftingPage;
import EsetKalenko.Halcyon.recipe.FluidMixingRecipe;
import EsetKalenko.Halcyon.registry.BlockRegistry;
import EsetKalenko.Halcyon.registry.EssenceTypeRegistry;
import EsetKalenko.Halcyon.registry.RecipeRegistry;
import EsetKalenko.Halcyon.screen.DataTabletScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

public class FluidMixingType extends CraftingType {
    @Override
    public void render(CraftingPage page, DataTabletScreen screen, GuiGraphics pGuiGraphics, int xOffset, int x, int yOffset, int y, Recipe recipe, int pMouseX, int pMouseY) {
        if (recipe instanceof FluidMixingRecipe recipe2) {
            pGuiGraphics.blit(DataTabletScreen.TEXTURE_CRAFTING, xOffset + x, yOffset + y, 10, 76, 123, 60);
            page.renderIngredientWithTooltip(screen, pGuiGraphics, Ingredient.of(BlockRegistry.FLUID_MIXER.get()), xOffset + x + 78, yOffset + y + 43, pMouseX, pMouseY);

            ClientEssenceBarUtil.drawEssenceBarTiny(pGuiGraphics, xOffset + x + 5, yOffset + y + 19, EssenceTypeRegistry.ESSENCE.get(), recipe2.getEssenceCost(), 1000, EssenceBarBackgroundTypes.INDUSTRIAL);
            Component essence = ClientEssenceBarUtil.getEssenceBarTooltipTiny(pMouseX, pMouseY, xOffset + x + 5, yOffset + y + 19, EssenceTypeRegistry.ESSENCE.get(), recipe2.getEssenceCost());
            if (essence != null) {
                page.tooltipToShow.clear();
                page.showTooltip = true;
                page.tooltipToShow.add(essence.getVisualOrderText());
            }

            page.renderFluidWithTooltip(pGuiGraphics, recipe2.getOutput(), xOffset + x + 78, yOffset + y + 22, pMouseX, pMouseY);
            if (!recipe2.getInput1().isEmpty()) {
                page.renderFluidWithTooltip(pGuiGraphics, recipe2.getInput1(), xOffset + x + 14, yOffset + y + 11, pMouseX, pMouseY);
            }
            if (!recipe2.getInput2().isEmpty()) {
                page.renderFluidWithTooltip(pGuiGraphics, recipe2.getInput2(), xOffset + x + 14, yOffset + y + 33, pMouseX, pMouseY);
            }
            if (!recipe2.getIngredients().isEmpty()) {
                page.renderIngredientWithTooltip(screen, pGuiGraphics, recipe2.getIngredients().get(0), xOffset + x + 35, yOffset + y + 22, pMouseX, pMouseY);
            }
            page.renderCraftTime(pGuiGraphics, recipe2.getTime(), xOffset + x + 78, yOffset + y + 8);
        }
    }

    @Override
    public boolean isRecipeType(Recipe recipe) {
        return recipe.getType().equals(RecipeRegistry.FLUID_MIXING_TYPE.get());
    }
}
