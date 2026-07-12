package EsetKalenko.Halcyon.recipe;

import net.minecraft.resources.ResourceLocation;

public interface IHasRequiredKnowledge {
    ResourceLocation getEntry();

    int getCompletionStage();

    boolean revealInEMIWhenIncomplete();
}
