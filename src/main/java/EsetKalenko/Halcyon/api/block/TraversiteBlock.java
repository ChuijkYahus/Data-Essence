package EsetKalenko.Halcyon.api.block;

import EsetKalenko.Halcyon.DataNEssence;
import net.minecraft.resources.ResourceLocation;

public interface TraversiteBlock {
    ResourceLocation TRAVERSITE_ROAD_SPEED_UUID = DataNEssence.locate("traversite_road_speed");
    float getBoost();
}
