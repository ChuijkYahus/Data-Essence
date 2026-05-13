package EsetKalenko.Halcyon.block.transmission;

import EsetKalenko.Halcyon.api.node.block.BaseEssencePointBlockEntity;
import EsetKalenko.Halcyon.api.essence.EssenceBlockEntity;
import EsetKalenko.Halcyon.api.essence.EssenceStorage;
import EsetKalenko.Halcyon.api.node.ICustomEssencePointBehaviour;
import EsetKalenko.Halcyon.api.util.BlockPosEdge;
import EsetKalenko.Halcyon.config.DataNEssenceConfig;
import EsetKalenko.Halcyon.registry.BlockEntityRegistry;
import EsetKalenko.Halcyon.registry.EssenceTypeRegistry;
import com.jgalgo.alg.common.Path;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Math;

import java.awt.*;
import java.util.List;

public class NaturalEssencePointBlockEntity extends BaseEssencePointBlockEntity {
    public NaturalEssencePointBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.NATURAL_ESSENCE_POINT.get(), pos, state);
    }

    @Override
    public Color[] linkColor() {
        var base = new Color(EssenceTypeRegistry.NATURAL_ESSENCE.get().color);
        float darken = 1.5f;
        return new Color[] {base,
                new Color((int) (base.getRed() / darken), (int) (base.getGreen() / darken), (int) (base.getBlue() / darken), base.getAlpha())
        };
    }

    @Override
    public void transfer(BaseEssencePointBlockEntity from, List<Path<BlockPos, BlockPosEdge>> other) {
        int transferAmount = (int) Math.floor((float)getFinalSpeed(DataNEssenceConfig.essencePointTransfer)/(float)other.size());
        for (Path<BlockPos, BlockPosEdge> i : other) {
            if (level.getBlockEntity(i.target()) instanceof BaseEssencePointBlockEntity entity) {
                BlockEntity fromEnt = from.getLevel().getBlockEntity(from.getBlockPos().relative(from.getDirection().getOpposite()));
                BlockEntity toEnt = entity.getLevel().getBlockEntity(entity.getBlockPos().relative(((BaseEssencePointBlockEntity) entity).getDirection().getOpposite()));
                if (fromEnt instanceof EssenceBlockEntity fromStorage && toEnt instanceof EssenceBlockEntity toStorage) {
                    if (fromEnt instanceof ICustomEssencePointBehaviour behaviour) {
                        if (!behaviour.canExtractEssence(fromStorage.getStorage(), toStorage.getStorage(), EssenceTypeRegistry.NATURAL_ESSENCE.get(), transferAmount)) {
                            return;
                        }
                    }
                    if (toEnt instanceof ICustomEssencePointBehaviour behaviour) {
                        if (!behaviour.canInsertEssence(fromStorage.getStorage(), toStorage.getStorage(), EssenceTypeRegistry.NATURAL_ESSENCE.get(), transferAmount)) {
                            return;
                        }
                    }
                    EssenceStorage.transferEssence(fromStorage.getStorage(), toStorage.getStorage(), EssenceTypeRegistry.NATURAL_ESSENCE.get(), transferAmount);
                    updateBlock(fromEnt);
                    updateBlock(toEnt);
                }
            }
        }
    }

    public void updateBlock(BlockEntity ent) {
        BlockState blockState = level.getBlockState(ent.getBlockPos());
        this.level.sendBlockUpdated(ent.getBlockPos(), blockState, blockState, 3);
        this.setChanged();
    }
}
