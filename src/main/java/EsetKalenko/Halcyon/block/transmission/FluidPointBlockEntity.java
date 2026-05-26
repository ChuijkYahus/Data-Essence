package EsetKalenko.Halcyon.block.transmission;

import EsetKalenko.Halcyon.DataNEssence;
import EsetKalenko.Halcyon.api.node.ICustomFluidPointBehaviour;
import EsetKalenko.Halcyon.api.node.block.BaseCapabilityPointBlockEntity;
import EsetKalenko.Halcyon.api.util.BlockPosEdge;
import EsetKalenko.Halcyon.config.DataNEssenceConfig;
import EsetKalenko.Halcyon.registry.BlockEntityRegistry;
import com.jgalgo.alg.common.Path;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;

import java.awt.*;
import java.util.List;

public class FluidPointBlockEntity extends BaseCapabilityPointBlockEntity {
    public static final ResourceLocation ALLOWED_FLUIDSTACKS = DataNEssence.locate("allowed_fluidstacks");

    public FluidPointBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.FLUID_POINT.get(), pos, state);
    }

    @Override
    public Color[] linkColor() {
        return new Color[] {
                new Color(0x56bae9),
                new Color(0x2e3ee7)
        };
    }

    @Override
    public boolean transfer(BaseCapabilityPointBlockEntity from, List<Path<BlockPos, BlockPosEdge>> other) {
        if (other.isEmpty()) {
            return false;
        }

        int transferAmount = (int)Math.floor((float)getFinalSpeed(DataNEssenceConfig.fluidPointTransfer)/(float)other.size());

        var sourceDirection = from.getDirection();
        var sourceTile = from.getBlockPos().relative(sourceDirection.getOpposite());
        if (!(from.getAttachedCapability(IFluidHandler.class) instanceof IFluidHandler resolved2)) {
            return false;
        }

        var didWork = false;

        for (Path<BlockPos, BlockPosEdge> i : other) {
            if (level.getBlockEntity(i.target()) instanceof BaseCapabilityPointBlockEntity ent) {
                var destinationDirection = ent.getDirection();
                var destTile = ent.getBlockPos().relative(destinationDirection.getOpposite());
                if (!(ent.getAttachedCapability(IFluidHandler.class) instanceof IFluidHandler resolved)) {
                    return false;
                }

                List<FluidStack> allowedFluidstacks = null;
                for (BlockPos j : i.vertices()) {
                    if (level.getBlockEntity(j) instanceof BaseCapabilityPointBlockEntity ent2) {
                        List<FluidStack> value = ent2.getValue(ALLOWED_FLUIDSTACKS, null);
                        if (allowedFluidstacks == null) {
                            allowedFluidstacks = value;
                        } else if (value != null) {
                            var iter = allowedFluidstacks.listIterator();
                            while (iter.hasNext()) {
                                var next = iter.next();
                                for (var stack : value) {
                                    if (!FluidStack.isSameFluid(next, stack)) {
                                        iter.remove();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                if (level.getBlockEntity(sourceTile) instanceof ICustomFluidPointBehaviour behaviour) {
                    if (!behaviour.canExtractFluid(resolved, resolved2)) {
                        continue;
                    }
                }

                if (level.getBlockEntity(destTile) instanceof ICustomFluidPointBehaviour behaviour) {
                    if (!behaviour.canInsertFluid(resolved, resolved2)) {
                        continue;
                    }
                }

                for (int o = 0; o < resolved2.getTanks(); o++) {
                    FluidStack copy = resolved2.getFluidInTank(o).copy();
                    if (allowedFluidstacks != null) {
                        boolean shouldSkip = true;
                        for (FluidStack stack : allowedFluidstacks) {
                            if (FluidStack.isSameFluid(stack, copy)) {
                                shouldSkip = false;
                                break;
                            }
                        }

                        if (shouldSkip) {
                            continue;
                        }
                    }
                    if (!copy.isEmpty()) {
                        copy.setAmount(Math.clamp(0, transferAmount, copy.getAmount()));
                        int filled = resolved.fill(copy, IFluidHandler.FluidAction.EXECUTE);
                        resolved2.drain(new FluidStack(copy.getFluid(), filled), IFluidHandler.FluidAction.EXECUTE);
                        didWork = true;
                    }
                }
            }
        }

        return didWork;
    }

    private BlockCapabilityCache<IItemHandler, @Nullable Direction> capCache = null;

    @Override
    public @Nullable Object getAttachedCapability(Class<?> capabilityClass) {
        if (IFluidHandler.class != capabilityClass) {
            return null;
        }

        if (capCache == null) {
            capCache = BlockCapabilityCache.create(Capabilities.ItemHandler.BLOCK, (ServerLevel) this.getLevel(), this.getBlockPos().relative(this.getDirection().getOpposite()), this.getDirection());
        }

        return capCache.getCapability();
    }

    @Override
    public void invalidateDirectionalCaches() {
        super.invalidateDirectionalCaches();
        this.capCache = null;
    }
}
