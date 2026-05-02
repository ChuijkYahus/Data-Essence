package EsetKalenko.Halcyon.data.datatablet.pages.crafting.types;

import EsetKalenko.Halcyon.api.DataNEssenceRegistries;
import EsetKalenko.Halcyon.api.essence.EssenceBarBackgroundTypes;
import EsetKalenko.Halcyon.api.util.client.ClientEssenceBarUtil;
import EsetKalenko.Halcyon.registry.BlockRegistry;
import EsetKalenko.Halcyon.registry.EssenceTypeRegistry;
import EsetKalenko.Halcyon.recipe.InfusionRecipe;
import EsetKalenko.Halcyon.registry.RecipeRegistry;
import EsetKalenko.Halcyon.screen.DataTabletScreen;
import EsetKalenko.Halcyon.data.datatablet.pages.CraftingPage;
import EsetKalenko.Halcyon.api.datatablet.CraftingType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

public class InfuserType extends CraftingType {
    @Override
    public void render(CraftingPage page, DataTabletScreen screen, GuiGraphics pGuiGraphics, int xOffset, int x, int yOffset, int y, Recipe recipe, int pMouseX, int pMouseY) {
        if (recipe instanceof InfusionRecipe recipe2) {
            pGuiGraphics.blit(DataTabletScreen.TEXTURE_CRAFTING, xOffset + x, yOffset + y, 10, 136, 123, 60);
            page.renderIngredientWithTooltip(screen, pGuiGraphics, Ingredient.of(BlockRegistry.INFUSER.get()), xOffset + x + 58, yOffset + y + 4, pMouseX, pMouseY);

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
            
            page.renderItemWithTooltip(pGuiGraphics, recipe.getResultItem(RegistryAccess.EMPTY), xOffset + x + 82, yOffset + y + 22, pMouseX, pMouseY);
            page.renderIngredientWithTooltip(screen, pGuiGraphics, recipe2.getIngredients().get(0), xOffset + x + 38, yOffset + y + 22, pMouseX, pMouseY);
        }
    }

    @Override
    public boolean isRecipeType(Recipe recipe) {
        return recipe.getType().equals(RecipeRegistry.INFUSION_TYPE.get());
    }
}
