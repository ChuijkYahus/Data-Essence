package EsetKalenko.Halcyon.api.block;

import EsetKalenko.Halcyon.Halcyon;
import net.minecraft.resources.ResourceLocation;

public interface TraversiteBlock {
    ResourceLocation TRAVERSITE_ROAD_SPEED_UUID = Halcyon.locate("traversite_road_speed");
    float getBoost();
}
