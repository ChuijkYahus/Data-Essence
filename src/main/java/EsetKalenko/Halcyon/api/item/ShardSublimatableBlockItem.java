package EsetKalenko.Halcyon.api.item;

import EsetKalenko.Halcyon.api.essence.EssenceType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.function.Supplier;

public class ShardSublimatableBlockItem extends BlockItem implements ShardSublimatable {
    private final Map<Supplier<EssenceType>, Float> containedEnergy;

    public ShardSublimatableBlockItem(Block block, Properties properties, Map<Supplier<EssenceType>, Float> containedEnergy) {
        super(block, properties);
        this.containedEnergy = containedEnergy;
    }


    @Override
    public Map<Supplier<EssenceType>, Float> getContainedEnergy() {
        return containedEnergy;
    }
}
