package EsetKalenko.Halcyon.data.datatablet.pages.crafting.types;

import EsetKalenko.Halcyon.api.datatablet.CraftingType;
import EsetKalenko.Halcyon.data.datatablet.pages.CraftingPage;
import EsetKalenko.Halcyon.recipe.MeltingRecipe;
import EsetKalenko.Halcyon.registry.BlockRegistry;
import EsetKalenko.Halcyon.registry.RecipeRegistry;
import EsetKalenko.Halcyon.screen.DataTabletScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

public class MeltingType extends CraftingType {

    @Override
    public void render(CraftingPage page, DataTabletScreen screen, GuiGraphics pGuiGraphics, int xOffset, int x, int yOffset, int y, Recipe recipe, int pMouseX, int pMouseY) {
        if (recipe instanceof MeltingRecipe meltingRecipe) {
            pGuiGraphics.blit(DataTabletScreen.TEXTURE_CRAFTING2, xOffset + x, yOffset + y, 10, 196, 123, 60);
            page.renderIngredientWithTooltip(screen, pGuiGraphics, Ingredient.of(BlockRegistry.MELTER.get()), xOffset + x + 74, yOffset + y + 43, pMouseX, pMouseY);
            page.renderFluidWithTooltip(pGuiGraphics, meltingRecipe.getOutput(), xOffset + x + 74, yOffset + y + 22, pMouseX, pMouseY);
            page.renderIngredientWithTooltip(screen, pGuiGraphics, meltingRecipe.getIngredients().get(0), xOffset + x + 30, yOffset + y + 22, pMouseX, pMouseY);
            page.renderCraftTime(pGuiGraphics, meltingRecipe.getTime(), xOffset + x + 74, yOffset + y + 8);
        }
    }

    @Override
    public boolean isRecipeType(Recipe recipe) {
        return recipe.getType().equals(RecipeRegistry.MELTING_TYPE.get());
    }
}
