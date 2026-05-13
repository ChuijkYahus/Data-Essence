package EsetKalenko.Halcyon.block.transmission;

import EsetKalenko.Halcyon.api.node.block.BaseCapabilityPointBlockEntity;
import EsetKalenko.Halcyon.api.util.BlockPosEdge;
import EsetKalenko.Halcyon.registry.BlockEntityRegistry;
import com.jgalgo.alg.common.Path;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

public class RFNodeBlockEntity extends BaseCapabilityPointBlockEntity {

    public RFNodeBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.RF_NODE.get(), pos, state);
    }

    @Override
    public Color[] linkColor() {
        return new Color[] {
                new Color(0x64719e),
                new Color(0x332a63)
        };
    }

    @Override
    public boolean transfer(BaseCapabilityPointBlockEntity sender, List<Path<BlockPos, BlockPosEdge>> desintations) {
        if (desintations.isEmpty()) {
            return false;
        }

        int transferAmount = Integer.MAX_VALUE; // I do not believe in limits

        if (!(this.getAttachedCapability(IEnergyStorage.class) instanceof IEnergyStorage senderEnergy)) {
            return false;
        }

        var didWork = false;

        for (Path<BlockPos, BlockPosEdge> i : desintations) {
            if (level.getBlockEntity(i.target()) instanceof BaseCapabilityPointBlockEntity receiver) {
                if (!(receiver.getAttachedCapability(IEnergyStorage.class) instanceof IEnergyStorage receiverEnergy)) {
                    return false;
                }

                int candidate = receiverEnergy.receiveEnergy( Math.clamp(senderEnergy.getEnergyStored(), 0, transferAmount) , true);
                if (candidate > 0) {
                    int transferred = receiverEnergy.receiveEnergy( Math.clamp(senderEnergy.getEnergyStored(), 0, transferAmount) , false);
                    senderEnergy.extractEnergy(transferred, false);
                    didWork = true;
                }
            }
        }

        return didWork;
    }

    private BlockCapabilityCache<IEnergyStorage, Direction> capCache = null;

    @Override
    public @Nullable Object getAttachedCapability(Class<?> capabilityClass) {
        if (IEnergyStorage.class != capabilityClass) {
            return null;
        }

        if (capCache == null) {
            capCache = BlockCapabilityCache.create(Capabilities.EnergyStorage.BLOCK, (ServerLevel) this.getLevel(), this.getBlockPos().relative(this.getDirection().getOpposite()), this.getDirection());
        }

        return capCache.getCapability();
    }

    @Override
    public void invalidateDirectionalCaches() {
        super.invalidateDirectionalCaches();
        this.capCache = null;
    }
}
