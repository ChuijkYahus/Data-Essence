package EsetKalenko.Halcyon.block.transmission;

import EsetKalenko.Halcyon.Halcyon;
import EsetKalenko.Halcyon.api.node.ICustomItemPointBehaviour;
import EsetKalenko.Halcyon.api.node.block.BaseCapabilityPointBlockEntity;
import EsetKalenko.Halcyon.api.util.BlockPosEdge;
import EsetKalenko.Halcyon.config.DataNEssenceConfig;
import EsetKalenko.Halcyon.item.BaseFilterLabel;
import EsetKalenko.Halcyon.registry.BlockEntityRegistry;
import com.jgalgo.alg.common.Path;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;

import java.awt.*;
import java.util.List;

public class ItemPointBlockEntity extends BaseCapabilityPointBlockEntity {
    public static final ResourceLocation ALLOWED_ITEMSTACKS = Halcyon.locate("allowed_itemstacks");

    public ItemPointBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ITEM_POINT.get(), pos, state);
    }

    @Override
    public Color[] linkColor() {
        return new Color[] {
                new Color(0xef4d3d),
                new Color(0xbb163d)
        };
    }

    @Override
    public boolean transfer(BaseCapabilityPointBlockEntity sourceNode, List<Path<BlockPos, BlockPosEdge>> other) {
        if (other.isEmpty()) {
            return false;
        }

        int transferAmount = (int)Math.floor((float)getFinalSpeed(DataNEssenceConfig.itemPointTransfer)/(float)other.size());

        var sourceDirection = sourceNode.getDirection();
        var sourceTile = sourceNode.getBlockPos().relative(sourceDirection.getOpposite());
        if (!(sourceNode.getAttachedCapability(IItemHandler.class) instanceof IItemHandler sourceHandler)) {
            return false;
        }

        var didWork = false;

        for (Path<BlockPos, BlockPosEdge> i : other) {
            if (level.getBlockEntity(i.target()) instanceof BaseCapabilityPointBlockEntity destinationNode) {
                var destinationDirection = destinationNode.getDirection();
                var destTile = destinationNode.getBlockPos().relative(destinationDirection.getOpposite());
                if (!(destinationNode.getAttachedCapability(IItemHandler.class) instanceof IItemHandler destHandler)) {
                    return false;
                }

                List<ItemStack> allowedItemstacks = null;
                for (BlockPos j : i.vertices()) {
                    if (level.getBlockEntity(j) instanceof BaseCapabilityPointBlockEntity ent2) {
                        List<ItemStack> value = ent2.getValue(ALLOWED_ITEMSTACKS, null);
                        if (allowedItemstacks == null) {
                            allowedItemstacks = value;
                        } else if (value != null) {
                            var iter = allowedItemstacks.listIterator();
                            while (iter.hasNext()) {
                                var next = iter.next();
                                for (var stack : value) {
                                    if (!ItemStack.isSameItem(next, stack)) {
                                        iter.remove();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                if (level.getBlockEntity(sourceTile) instanceof ICustomItemPointBehaviour behaviour) {
                    if (!behaviour.canExtractItem(destHandler, sourceHandler)) {
                        continue;
                    }
                }

                if (level.getBlockEntity(destTile) instanceof ICustomItemPointBehaviour behaviour) {
                    if (!behaviour.canInsertItem(destHandler, sourceHandler)) {
                        continue;
                    }
                }

                if (moveItems(sourceHandler, destHandler, transferAmount, allowedItemstacks)) {
                    didWork = true;
                }
            }
        }

        return didWork;
    }

    @Override
    public int maxBackoff() {
        return 16;
    }

    public boolean moveItems(IItemHandler sourceHandler, IItemHandler destHandler, int transferAmount, List<ItemStack> allowedItemstacks) {
        boolean movedAnything = false;

        for (int o = 0; o < sourceHandler.getSlots(); o++) {
            ItemStack candidate = sourceHandler.extractItem(o, transferAmount, true).copy();

            if (allowedItemstacks != null) {
                boolean shouldSkip = true;

                for ( ItemStack allowedStack : allowedItemstacks ) {
                    var item = allowedStack.getItem();
                    if ( item instanceof BaseFilterLabel && ((BaseFilterLabel) item).labelMatches(allowedStack, candidate) ) {
                        shouldSkip = false;
                        break;
                    }

                    if ( !(item instanceof BaseFilterLabel) && ItemStack.isSameItem(allowedStack, candidate) ) {
                        shouldSkip = false;
                        break;
                    }

                }

                if (shouldSkip) {
                    continue;
                }
            }

            if (!candidate.isEmpty()) {
                ItemStack copy2 = candidate.copy();
                int slot = 0;
                while (slot < destHandler.getSlots()) {
                    ItemStack copyCopy = candidate.copy();
                    int remaining = destHandler.insertItem(slot, copyCopy, false).getCount();
                    candidate.setCount(remaining);
                    if (copy2.getCount() - candidate.getCount() > 0) {
                        movedAnything = true;
                    }
                    if (remaining <= 0) {
                        break;
                    }
                    slot++;
                }
                if (movedAnything) {
                    sourceHandler.extractItem(o, copy2.getCount() - candidate.getCount(), false);
                    break;
                }
            }
        }

        return movedAnything;
    }

    private BlockCapabilityCache<IItemHandler, Direction> capCache = null;

    @Override
    public @Nullable Object getAttachedCapability(Class<?> capabilityClass) {
        if (capabilityClass != IItemHandler.class) {
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
