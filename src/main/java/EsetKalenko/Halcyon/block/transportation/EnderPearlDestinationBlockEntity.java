package EsetKalenko.Halcyon.block.transportation;

import com.cmdpro.databank.model.animation.DatabankAnimationReference;
import com.cmdpro.databank.model.animation.DatabankAnimationState;
import EsetKalenko.Halcyon.api.pearlnetwork.PearlNetworkBlockEntity;
import EsetKalenko.Halcyon.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EnderPearlDestinationBlockEntity extends PearlNetworkBlockEntity {public DatabankAnimationState animState = new DatabankAnimationState("idle")
        .addAnim(new DatabankAnimationReference("idle", (state, anim) -> {}, (state, anim) -> {}));

    public EnderPearlDestinationBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.ENDER_PEARL_DESTINATION.get(), pos, blockState);
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        animState.setLevel(level);
    }

    @Override
    public Vec3 getLinkShift() {
        return new Vec3(0, -0.5, 0);
    }
}
