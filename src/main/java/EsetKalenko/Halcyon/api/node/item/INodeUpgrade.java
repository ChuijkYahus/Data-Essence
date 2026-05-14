package EsetKalenko.Halcyon.api.node.item;

import EsetKalenko.Halcyon.api.util.BlockPosEdge;
import com.jgalgo.alg.common.Path;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public interface INodeUpgrade {
    default Object getValue(ItemStack upgrade, ResourceLocation id, Object originalValue, BlockEntity node) { return null; };
    default boolean preTransfer(ItemStack upgrade, BlockEntity from, List<Path<BlockPos, BlockPosEdge>> other, boolean canceled) { return canceled; }
    default void postTransfer(ItemStack upgrade, BlockEntity from, List<Path<BlockPos, BlockPosEdge>> other) {}
    Type getType();
    enum Type {
        UNIQUE,
        UNIVERSAL
    }
}
