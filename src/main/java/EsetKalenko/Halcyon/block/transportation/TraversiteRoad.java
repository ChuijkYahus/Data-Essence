package EsetKalenko.Halcyon.block.transportation;

import EsetKalenko.Halcyon.api.block.TraversiteBlock;
import net.minecraft.world.level.block.Block;

public class TraversiteRoad extends Block implements TraversiteBlock {
    public float boost;
    public TraversiteRoad(Properties pProperties, float boost) {
        super(pProperties);
        this.boost = boost;
    }

    @Override
    public float getBoost() {
        return boost;
    }
}
