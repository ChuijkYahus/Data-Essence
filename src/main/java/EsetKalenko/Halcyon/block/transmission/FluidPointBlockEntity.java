package EsetKalenko.Halcyon.block.transmission;

import EsetKalenko.Halcyon.DataNEssence;
import EsetKalenko.Halcyon.api.node.block.BaseCapabilityPointBlockEntity;
import EsetKalenko.Halcyon.api.node.ICustomFluidPointBehaviour;
import EsetKalenko.Halcyon.api.util.BlockPosEdge;
import EsetKalenko.Halcyon.config.DataNEssenceConfig;
import EsetKalenko.Halcyon.registry.BlockEntityRegistry;
import com.jgalgo.alg.common.Path;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.joml.Math;

import java.awt.*;
import java.util.List;

public class FluidPointBlockEntity extends BaseCapabilityPointBlockEntity {
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

        IFluidHandler resolved2 = level.getCapability(Capabilities.FluidHandler.BLOCK, from.getBlockPos().relative(from.getDirection().getOpposite()), from.getDirection());
        if (resolved2 == null) {
            return false;
        }

        var didWork = false;

        for (Path<BlockPos, BlockPosEdge> i : other) {
            if (level.getBlockEntity(i.target()) instanceof BaseCapabilityPointBlockEntity ent) {
                List<FluidStack> allowedFluidstacks = null;
                for (BlockPos j : i.vertices()) {
                    if (level.getBlockEntity(j) instanceof BaseCapabilityPointBlockEntity ent2) {
                        List<FluidStack> value = ent2.getValue(DataNEssence.locate("allowed_fluidstacks"), null);
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

                IFluidHandler resolved = level.getCapability(Capabilities.FluidHandler.BLOCK, ent.getBlockPos().relative(ent.getDirection().getOpposite()), ent.getDirection());
                if (resolved == null) {
                    continue;
                }

                if (level.getBlockEntity(from.getBlockPos().relative(from.getDirection().getOpposite())) instanceof ICustomFluidPointBehaviour behaviour) {
                    if (!behaviour.canExtractFluid(resolved, resolved2)) {
                        continue;
                    }
                }

                if (level.getBlockEntity(ent.getBlockPos().relative(ent.getDirection().getOpposite())) instanceof ICustomFluidPointBehaviour behaviour) {
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
}
