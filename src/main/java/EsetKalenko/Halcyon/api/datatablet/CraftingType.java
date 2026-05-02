package EsetKalenko.Halcyon.api.datatablet;

import EsetKalenko.Halcyon.screen.DataTabletScreen;
import EsetKalenko.Halcyon.data.datatablet.pages.CraftingPage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.Recipe;

public abstract class CraftingType {
    public abstract void render(CraftingPage page, DataTabletScreen screen, GuiGraphics pGuiGraphics, int xOffset, int x, int yOffset, int y, Recipe recipe, int pMouseX, int pMouseY);
    public abstract boolean isRecipeType(Recipe recipe);
}
