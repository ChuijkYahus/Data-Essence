package EsetKalenko.Halcyon.recipe;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public interface IHasEssenceCost {
    default Map<ResourceLocation, Float> getEssenceCost() {
        return Map.of();
    }
}
