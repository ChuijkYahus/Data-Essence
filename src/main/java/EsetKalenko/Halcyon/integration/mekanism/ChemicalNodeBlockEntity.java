package EsetKalenko.Halcyon.integration.mekanism;

import EsetKalenko.Halcyon.DataNEssence;
import EsetKalenko.Halcyon.api.node.block.BaseCapabilityPointBlockEntity;
import EsetKalenko.Halcyon.api.util.BlockPosEdge;
import EsetKalenko.Halcyon.config.DataNEssenceConfig;
import EsetKalenko.Halcyon.registry.BlockEntityRegistry;
import com.jgalgo.alg.common.Path;
import mekanism.api.Action;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;

import java.awt.*;
import java.util.List;

import static EsetKalenko.Halcyon.integration.DataNEssenceIntegration.BLOCK_CHEMICAL;

public class ChemicalNodeBlockEntity extends BaseCapabilityPointBlockEntity {
    public static final ResourceLocation ALLOWED_CHEMICALS = DataNEssence.locate("allowed_chemicals");

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
    public boolean transfer(BaseCapabilityPointBlockEntity from, List<Path<BlockPos, BlockPosEdge>> other) {
        if (other.isEmpty()) {
            return false;
        }

        int transferAmount = (int) Math.floor((float)getFinalSpeed(DataNEssenceConfig.fluidPointTransfer)/(float)other.size());

        if (!(from.getAttachedCapability(IChemicalHandler.class) instanceof IChemicalHandler sender)) {
            return false;
        }

        var didWork = false;

        for (Path<BlockPos, BlockPosEdge> i : other) {
            if (level.getBlockEntity(i.target()) instanceof BaseCapabilityPointBlockEntity to) {
                if (!(to.getAttachedCapability(IChemicalHandler.class) instanceof IChemicalHandler receiver)) {
                    return false;
                }

                List<ChemicalStack> allowedChemicals = null;

                for (BlockPos j : i.vertices()) {
                    if (level.getBlockEntity(j) instanceof BaseCapabilityPointBlockEntity to2) {
                        List<ChemicalStack> value = to2.getValue(ALLOWED_CHEMICALS, null);
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

                if (other instanceof ICustomChemicalNodeBehaviour behaviour) {
                    if (!behaviour.canInsertChemical(receiver, sender)) {
                        continue;
                    }
                }

                for (int o = 0; o < sender.getChemicalTanks(); o++) {
                    ChemicalStack chemical = sender.getChemicalInTank(o).copy();
                    if (allowedChemicals != null) {
                        boolean shouldSkip = true;
                        for (var stack : allowedChemicals) {
                            if (ChemicalStack.isSameChemical(stack, chemical)) {
                                shouldSkip = false;
                                break;
                            }
                        }

                        if (shouldSkip) {
                            continue;
                        }
                    }
                    if (!chemical.isEmpty()) {
                        chemical.setAmount((long) Math.clamp(0, transferAmount, chemical.getAmount()));
                        ChemicalStack candidate = receiver.insertChemical(chemical, Action.SIMULATE);

                        if (candidate.getAmount() == 0) {
                            ChemicalStack extracted = sender.extractChemical(chemical, Action.EXECUTE);
                            receiver.insertChemical(extracted, Action.EXECUTE);
                            didWork = true;
                        }
                    }
                }
            }
        }

        return didWork;
    }

    private BlockCapabilityCache<IChemicalHandler, @Nullable Direction> capCache = null;

    @Override
    public @Nullable Object getAttachedCapability(Class<?> capabilityClass) {
        if (IChemicalHandler.class != capabilityClass) {
            return null;
        }

        if (capCache == null) {
            capCache = BlockCapabilityCache.create(BLOCK_CHEMICAL, (ServerLevel) this.getLevel(), this.getBlockPos().relative(this.getDirection().getOpposite()), this.getDirection());
        }

        return capCache.getCapability();
    }

    @Override
    public void invalidateDirectionalCaches() {
        super.invalidateDirectionalCaches();
        this.capCache = null;
    }
}
