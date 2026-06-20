package EsetKalenko.Halcyon.recipe;

import EsetKalenko.Halcyon.Halcyon;
import EsetKalenko.Halcyon.registry.BlockRegistry;
import EsetKalenko.Halcyon.registry.RecipeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public interface IFabricationRecipe extends Recipe<CraftingInput>, IHasRequiredKnowledge, IHasEssenceCost, HalcyonRecipe {
    @Override
    default RecipeType<?> getType() {
        return RecipeRegistry.FABRICATIONCRAFTING.get();
    }

    @Override
    default boolean isSpecial() {
        return true;
    }

    @Override
    default ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistry.FABRICATOR.get());
    }
    int getTime();

    @Override
    default ResourceLocation getMachineEntry() {
        return Halcyon.locate("basics/fabricator");
    }
}
