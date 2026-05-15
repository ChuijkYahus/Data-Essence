package EsetKalenko.Halcyon.recipe;

import net.minecraft.resources.ResourceLocation;

public interface HalcyonRecipe {
    /**
     * The entry corresponding to the crafter of this recipe type. For checking whether the player even knows about this recipe type yet.
     * @return the relevant entry
     */
    ResourceLocation getMachineEntry();
}
