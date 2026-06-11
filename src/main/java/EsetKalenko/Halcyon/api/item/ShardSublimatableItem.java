package EsetKalenko.Halcyon.api.item;

import EsetKalenko.Halcyon.api.essence.EssenceType;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.function.Supplier;

public class ShardSublimatableItem extends Item implements ShardSublimatable {
    private final Map<Supplier<EssenceType>, Float> containedEnergy;

    public ShardSublimatableItem(Properties pProperties, Map<Supplier<EssenceType>, Float> containedEnergy) {
        super(pProperties);
        this.containedEnergy = containedEnergy;
    }

    @Override
    public Map<Supplier<EssenceType>, Float> getContainedEnergy() {
        return containedEnergy;
    }
}
