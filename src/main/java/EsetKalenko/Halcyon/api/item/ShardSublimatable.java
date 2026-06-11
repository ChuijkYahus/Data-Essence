package EsetKalenko.Halcyon.api.item;

import EsetKalenko.Halcyon.api.essence.EssenceType;

import java.util.Map;
import java.util.function.Supplier;

public interface ShardSublimatable {
    public Map<Supplier<EssenceType>, Float> getContainedEnergy();
}
