package EsetKalenko.Halcyon.block.transmission;

import EsetKalenko.Halcyon.api.node.block.BaseEssencePointBlockEntity;
import EsetKalenko.Halcyon.api.essence.EssenceBlockEntity;
import EsetKalenko.Halcyon.api.essence.EssenceStorage;
import EsetKalenko.Halcyon.api.node.ICustomEssencePointBehaviour;
import EsetKalenko.Halcyon.config.DataNEssenceConfig;
import EsetKalenko.Halcyon.registry.BlockEntityRegistry;
import EsetKalenko.Halcyon.registry.EssenceTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultEdge;
import org.joml.Math;

import java.awt.*;
import java.util.List;

public class EssencePointBlockEntity extends BaseEssencePointBlockEntity {
    public EssencePointBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ESSENCE_POINT.get(), pos, state);
    }

    @Override
    public Color[] linkColor() {
        return new Color[] {
                new Color(EssenceTypeRegistry.ESSENCE.get().color),
                new Color(0x6910cb)
        };
    }

    @Override
    public void transfer(BaseEssencePointBlockEntity from, List<GraphPath<BlockPos, DefaultEdge>> other) {
        int transferAmount = (int) Math.floor((float)getFinalSpeed(DataNEssenceConfig.essencePointTransfer)/(float)other.size());
        for (GraphPath<BlockPos, DefaultEdge> i : other) {
            if (level.getBlockEntity(i.getEndVertex()) instanceof BaseEssencePointBlockEntity entity) {
                BlockEntity fromEnt = from.getLevel().getBlockEntity(from.getBlockPos().relative(from.getDirection().getOpposite()));
                BlockEntity toEnt = entity.getLevel().getBlockEntity(entity.getBlockPos().relative(((BaseEssencePointBlockEntity) entity).getDirection().getOpposite()));
                if (fromEnt instanceof EssenceBlockEntity fromStorage && toEnt instanceof EssenceBlockEntity toStorage) {
                    if (fromEnt instanceof ICustomEssencePointBehaviour behaviour) {
                        if (!behaviour.canExtractEssence(fromStorage.getStorage(), toStorage.getStorage(), EssenceTypeRegistry.ESSENCE.get(), transferAmount)) {
                            return;
                        }
                    }
                    if (toEnt instanceof ICustomEssencePointBehaviour behaviour) {
                        if (!behaviour.canInsertEssence(fromStorage.getStorage(), toStorage.getStorage(), EssenceTypeRegistry.ESSENCE.get(), transferAmount)) {
                            return;
                        }
                    }
                    EssenceStorage.transferEssence(fromStorage.getStorage(), toStorage.getStorage(), EssenceTypeRegistry.ESSENCE.get(), transferAmount);
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
