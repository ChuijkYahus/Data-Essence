package EsetKalenko.Halcyon.integration.mekanism;

import EsetKalenko.Halcyon.DataNEssence;
import EsetKalenko.Halcyon.api.node.block.BaseCapabilityPointBlockEntity;
import EsetKalenko.Halcyon.config.DataNEssenceConfig;
import EsetKalenko.Halcyon.registry.BlockEntityRegistry;
import mekanism.api.Action;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultEdge;
import org.joml.Math;

import java.awt.*;
import java.util.List;

import static EsetKalenko.Halcyon.integration.DataNEssenceIntegration.BLOCK_CHEMICAL;

public class ChemicalNodeBlockEntity extends BaseCapabilityPointBlockEntity {

    public ChemicalNodeBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.CHEMICAL_NODE.get(), pPos, pBlockState);
    }

    @Override
    public Color[] linkColor() {
        var base = new Color(0x65EA0C); // a mad science-y radioactive green
        float darken = 1.5f;
        return new Color[] {base,
                new Color((int) (base.getRed() / darken), (int) (base.getGreen() / darken), (int) (base.getBlue() / darken), base.getAlpha())
        };
    }

    @Override
    public boolean transfer(BaseCapabilityPointBlockEntity from, List<GraphPath<BlockPos, DefaultEdge>> other) {
        if (other.isEmpty()) {
            return false;
        }

        int transferAmount = (int) Math.floor((float)getFinalSpeed(DataNEssenceConfig.fluidPointTransfer)/(float)other.size());

        IChemicalHandler resolved2 = level.getCapability(BLOCK_CHEMICAL, from.getBlockPos().relative(from.getDirection().getOpposite()), from.getDirection());
        if (resolved2 == null) {
            return false;
        }

        var didWork = false;

        for (GraphPath<BlockPos, DefaultEdge> i : other) {
            if (level.getBlockEntity(i.getEndVertex()) instanceof BaseCapabilityPointBlockEntity to) {
                List<ChemicalStack> allowedChemicals = null;

                for (BlockPos j : i.getVertexList()) {
                    if (level.getBlockEntity(j) instanceof BaseCapabilityPointBlockEntity to2) {
                        List<ChemicalStack> value = to2.getValue(DataNEssence.locate("allowed_chemicals"), null);
                        if (allowedChemicals == null) {
                            allowedChemicals = value;
                        } else if (value != null) {
                            var iter = allowedChemicals.listIterator();
                            while (iter.hasNext()) {
                                var next = iter.next();
                                for (var stack : value) {
                                    if (!ChemicalStack.isSameChemical(next, stack)) {
                                        iter.remove();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                IChemicalHandler resolved = level.getCapability(BLOCK_CHEMICAL, to.getBlockPos().relative(to.getDirection().getOpposite()), to.getDirection());

                if (resolved == null) {
                    continue;
                }

                if (other instanceof ICustomChemicalNodeBehaviour behaviour) {
                    if (!behaviour.canInsertChemical(resolved, resolved2)) {
                        continue;
                    }
                }

                for (int o = 0; o < resolved2.getChemicalTanks(); o++) {
                    ChemicalStack copy = resolved2.getChemicalInTank(o).copy();
                    if (allowedChemicals != null) {
                        boolean shouldSkip = true;
                        for (var stack : allowedChemicals) {
                            if (ChemicalStack.isSameChemical(stack, copy)) {
                                shouldSkip = false;
                                break;
                            }
                        }

                        if (shouldSkip) {
                            continue;
                        }
                    }
                    if (!copy.isEmpty()) {
                        copy.setAmount((long) Math.clamp(0, transferAmount, copy.getAmount()));
                        ChemicalStack filled = resolved.insertChemical(copy, Action.EXECUTE);
                        resolved2.extractChemical(new ChemicalStack(copy.getChemical(), filled.getAmount()), Action.EXECUTE);
                        didWork = true;
                    }
                }
            }
        }

        return didWork;
    }
}
