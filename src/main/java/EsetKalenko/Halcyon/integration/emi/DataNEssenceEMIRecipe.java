package EsetKalenko.Halcyon.integration.emi;

import EsetKalenko.Halcyon.Halcyon;
import EsetKalenko.Halcyon.moddata.ClientPlayerUnlockedEntries;
import EsetKalenko.Halcyon.recipe.HalcyonRecipe;
import EsetKalenko.Halcyon.recipe.IHasRequiredKnowledge;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

// Heavily derived from https://github.com/DaFuqs/Spectrum/blob/1.20.1-aria-for-painters/src/main/java/de/dafuqs/spectrum/compat/emi/SpectrumEmiRecipe.java
public abstract class DataNEssenceEMIRecipe implements EmiRecipe {
    public static final Component lockedText1 = Component.translatable("emi.halcyon.recipe_locked_line_1");
    public static final Component lockedText2 = Component.translatable("emi.halcyon.recipe_locked_line_2");

    public final EmiRecipeCategory category;
    public final ResourceLocation recipe, recipeUnlockEntry;
    public final int width, height;
    public List<EmiIngredient> inputs = List.of();
    public List<EmiStack> outputs = List.of();

    public DataNEssenceEMIRecipe(EmiRecipeCategory category, ResourceLocation recipe, ResourceLocation recipeUnlockEntry, int width, int height) {
        this.category = category;
        this.recipe = recipe;
        this.recipeUnlockEntry = recipeUnlockEntry;
        this.width = width;
        this.height = height;
    }

    public boolean isUnlocked() {
        return recipeUnlockEntry == null || hasData(recipeUnlockEntry, recipe);
    }

    public boolean hasData(ResourceLocation dataEntry, ResourceLocation recipe) {
        var dataLockedRecipe = (IHasRequiredKnowledge) getBackingRecipe().value();
        var known = false;
        var hasBaseUnderstanding = false;

        // first, do we even know about the machine the recipe is in? TODO separate method
        if ( dataLockedRecipe instanceof HalcyonRecipe halcyonRecipe )
            hasBaseUnderstanding = ClientPlayerUnlockedEntries.getUnlocked().contains(halcyonRecipe.getMachineEntry());

        // second, is this unlocked, or incomplete and we are at the right stage?
        known = ClientPlayerUnlockedEntries.getUnlocked().contains(dataEntry)
                || ( ClientPlayerUnlockedEntries.getIncomplete().containsKey(dataEntry)
                && ClientPlayerUnlockedEntries.getIncomplete().get(dataEntry) >= dataLockedRecipe.getCompletionStage());

        // third, is this recipe marked to show up in EMI before completion at all?

        return hasBaseUnderstanding && known;
    }

    public abstract void addUnlockedWidgets(WidgetHolder widgets);

    @Override
    public void addWidgets(WidgetHolder widgets) {
        if (!isUnlocked()) {
            ResourceLocation background = Halcyon.locate("textures/gui/emi_recipe_locked.png");
            int lightColor = 0xFFFF96B5;
            int darkColor = 0xFFF25EA1;

            widgets.addTexture(background, 0, 0, 123, 60, 0, 0);
            widgets.addText(lockedText1, 123 - 4, 60 / 2, lightColor, false).horizontalAlign(TextWidget.Alignment.END);
            widgets.addText(lockedText2, 123 - 4, 60 / 2 + 10, darkColor, false).horizontalAlign(TextWidget.Alignment.END);
        } else {
            addUnlockedWidgets(widgets);
        }
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return category;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return recipe;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return outputs;
    }

    @Override
    public boolean supportsRecipeTree() {
        return EmiRecipe.super.supportsRecipeTree() && isUnlocked();
    }
}
