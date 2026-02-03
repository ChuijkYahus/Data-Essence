package com.cmdpro.datanessence.integration.pastel;

import com.cmdpro.datanessence.recipe.DataNEssenceRecipe;
import com.cmdpro.datanessence.recipe.IHasEssenceCost;
import net.minecraft.resources.ResourceLocation;

public class HueShiftRecipe implements IHasEssenceCost, DataNEssenceRecipe {

    @Override
    public ResourceLocation getMachineEntry() {
        return null;
    }
}
