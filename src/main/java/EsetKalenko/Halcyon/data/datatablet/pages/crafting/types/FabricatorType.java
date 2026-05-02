package EsetKalenko.Halcyon.data.datatablet.pages.crafting.types;

import EsetKalenko.Halcyon.api.DataNEssenceRegistries;
import EsetKalenko.Halcyon.api.essence.EssenceBarBackgroundTypes;
import EsetKalenko.Halcyon.api.util.client.ClientEssenceBarUtil;
import EsetKalenko.Halcyon.registry.BlockRegistry;
import EsetKalenko.Halcyon.registry.EssenceTypeRegistry;
import EsetKalenko.Halcyon.registry.RecipeRegistry;
import EsetKalenko.Halcyon.recipe.IFabricationRecipe;
import EsetKalenko.Halcyon.recipe.ShapedFabricationRecipe;
import EsetKalenko.Halcyon.recipe.ShapelessFabricationRecipe;
import EsetKalenko.Halcyon.screen.DataTabletScreen;
import EsetKalenko.Halcyon.data.datatablet.pages.CraftingPage;
import EsetKalenko.Halcyon.api.datatablet.CraftingType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.*;

public class FabricatorType extends CraftingType {
    @Override
    public void render(CraftingPage page, DataTabletScreen screen, GuiGraphics pGuiGraphics, int xOffset, int x, int yOffset, int y, Recipe recipe, int pMouseX, int pMouseY) {
        if (recipe instanceof IFabricationRecipe recipe2) {
            pGuiGraphics.blit(DataTabletScreen.TEXTURE_CRAFTING, xOffset + x, yOffset + y, 10, 196, 123, 60);
            page.renderIngredientWithTooltip(screen, pGuiGraphics, Ingredient.of(BlockRegistry.FABRICATOR.get()), xOffset + x + 98, yOffset + y + 43, pMouseX, pMouseY);

            ClientEssenceBarUtil.drawEssenceBarTiny(pGuiGraphics, xOffset + x+5, yOffset + y+6, EssenceTypeRegistry.ESSENCE.get(), recipe2.getEssenceCost().getOrDefault(DataNEssenceRegistries.ESSENCE_TYPE_REGISTRY.getKey(EssenceTypeRegistry.ESSENCE.get()), 0f), 1000, EssenceBarBackgroundTypes.INDUSTRIAL);
            ClientEssenceBarUtil.drawEssenceBarTiny(pGuiGraphics, xOffset + x+13, yOffset + y+6, EssenceTypeRegistry.LUNAR_ESSENCE.get(), recipe2.getEssenceCost().getOrDefault(DataNEssenceRegistries.ESSENCE_TYPE_REGISTRY.getKey(EssenceTypeRegistry.LUNAR_ESSENCE.get()), 0f), 1000, EssenceBarBackgroundTypes.INDUSTRIAL);
            ClientEssenceBarUtil.drawEssenceBarTiny(pGuiGraphics, xOffset + x+5, yOffset + y+32, EssenceTypeRegistry.NATURAL_ESSENCE.get(), recipe2.getEssenceCost().getOrDefault(DataNEssenceRegistries.ESSENCE_TYPE_REGISTRY.getKey(EssenceTypeRegistry.NATURAL_ESSENCE.get()), 0f), 1000, EssenceBarBackgroundTypes.INDUSTRIAL);
            ClientEssenceBarUtil.drawEssenceBarTiny(pGuiGraphics, xOffset + x+13, yOffset + y+32, EssenceTypeRegistry.EXOTIC_ESSENCE.get(), recipe2.getEssenceCost().getOrDefault(DataNEssenceRegistries.ESSENCE_TYPE_REGISTRY.getKey(EssenceTypeRegistry.EXOTIC_ESSENCE.get()), 0f), 1000, EssenceBarBackgroundTypes.INDUSTRIAL);

            Component essence = ClientEssenceBarUtil.getEssenceBarTooltipTiny(pMouseX, pMouseY, xOffset + x+5, yOffset + y+6, EssenceTypeRegistry.ESSENCE.get(), recipe2.getEssenceCost().getOrDefault(DataNEssenceRegistries.ESSENCE_TYPE_REGISTRY.getKey(EssenceTypeRegistry.ESSENCE.get()), 0f));
            if (essence != null) {
                page.tooltipToShow.clear();
                page.showTooltip = true;
                page.tooltipToShow.add(essence.getVisualOrderText());
            }
            Component lunarEssence = ClientEssenceBarUtil.getEssenceBarTooltipTiny(pMouseX, pMouseY, xOffset + x+13, yOffset + y+6, EssenceTypeRegistry.LUNAR_ESSENCE.get(), recipe2.getEssenceCost().getOrDefault(DataNEssenceRegistries.ESSENCE_TYPE_REGISTRY.getKey(EssenceTypeRegistry.LUNAR_ESSENCE.get()), 0f));
            if (lunarEssence != null) {
                page.tooltipToShow.clear();
                page.showTooltip = true;
                page.tooltipToShow.add(lunarEssence.getVisualOrderText());
            }
            Component naturalEssence = ClientEssenceBarUtil.getEssenceBarTooltipTiny(pMouseX, pMouseY, xOffset + x+5, yOffset + y+32, EssenceTypeRegistry.NATURAL_ESSENCE.get(), recipe2.getEssenceCost().getOrDefault(DataNEssenceRegistries.ESSENCE_TYPE_REGISTRY.getKey(EssenceTypeRegistry.NATURAL_ESSENCE.get()), 0f));
            if (naturalEssence != null) {
                page.tooltipToShow.clear();
                page.showTooltip = true;
                page.tooltipToShow.add(naturalEssence.getVisualOrderText());
            }
            Component exoticEssence = ClientEssenceBarUtil.getEssenceBarTooltipTiny(pMouseX, pMouseY, xOffset + x+13, yOffset + y+32, EssenceTypeRegistry.EXOTIC_ESSENCE.get(), recipe2.getEssenceCost().getOrDefault(DataNEssenceRegistries.ESSENCE_TYPE_REGISTRY.getKey(EssenceTypeRegistry.EXOTIC_ESSENCE.get()), 0f));
            if (exoticEssence != null) {
                page.tooltipToShow.clear();
                page.showTooltip = true;
                page.tooltipToShow.add(exoticEssence.getVisualOrderText());
            }

            page.renderItemWithTooltip(pGuiGraphics, recipe.getResultItem(RegistryAccess.EMPTY), xOffset + x + 98, yOffset + y + 22, pMouseX, pMouseY);
            if (recipe2 instanceof ShapelessFabricationRecipe) {
                pGuiGraphics.blit(DataTabletScreen.TEXTURE_CRAFTING, xOffset + x + 93, yOffset + y + 4, 242, 185, 14, 11);
            }
            int x2 = 1;
            int y2 = 1;
            int p = 0;
            int wrap = 3;
            if (recipe2 instanceof ShapedFabricationRecipe shaped) {
                wrap = shaped.getWidth();
            }
            for (Ingredient o : recipe2.getIngredients()) {
                page.renderIngredientWithTooltip(screen, pGuiGraphics, o, xOffset + x + 20 + x2, yOffset + y + 4 + y2, pMouseX, pMouseY);
                x2 += 17;
                p++;
                if (p >= wrap) {
                    p = 0;
                    x2 = 1;
                    y2 += 17;
                }
            }
            page.renderCraftTime(pGuiGraphics, recipe2.getTime(), xOffset + x + 88, yOffset + y + 8);
        }
    }

    @Override
    public boolean isRecipeType(Recipe recipe) {
        return recipe.getType().equals(RecipeRegistry.FABRICATIONCRAFTING.get());
    }
}
