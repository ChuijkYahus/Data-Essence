package EsetKalenko.Halcyon.block.technical;

import com.cmdpro.databank.model.animation.DatabankAnimationReference;
import com.cmdpro.databank.model.animation.DatabankAnimationState;
import EsetKalenko.Halcyon.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

// Literally only exists for the renderer
public class ArekkoBlockEntity extends BlockEntity {

    public DatabankAnimationState animState = new DatabankAnimationState("idle")
            .addAnim(new DatabankAnimationReference("idle", (state, anim) -> {}, (state, anim) -> {}));

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        animState.setLevel(level);
    }

    public ArekkoBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.AREKKO.get(), pos, blockState);
    }
}
