package EsetKalenko.Halcyon.block.transmission;

import EsetKalenko.Halcyon.api.LockableItemHandler;
import EsetKalenko.Halcyon.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.joml.Math;

public class LimitedItemBufferBlockEntity extends ItemBufferBlockEntity {
    public LimitedItemBufferBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.LIMITED_ITEM_BUFFER.get(), pPos, pBlockState);
    }

    @Override
    public boolean transfer(IItemHandler handler) {
        IItemHandler resolved = getItemHandler();
        boolean movedAnything = false;
        for (int o = 0; o < resolved.getSlots(); o++) {
            ItemStack copy = resolved.getStackInSlot(o).copy();
            if (!copy.isEmpty()) {
                copy.setCount(Math.clamp(0, 16, copy.getCount()));
                ItemStack copy2 = copy.copy();
                int p = 0;
                while (p < handler.getSlots()) {
                    ItemStack copyCopy = copy.copy();
                    int limit = 2-handler.getStackInSlot(p).getCount();
                    if (limit <= 0) {
                        p++;
                        continue;
                    }
                    copyCopy.setCount(Math.clamp(0, limit, copyCopy.getCount()));
                    boolean canInsert = true;
                    if (handler instanceof LockableItemHandler lockable) {
                        canInsert = lockable.canInsertFromBuffer(p, copyCopy);
                    }
                    if (canInsert) {
                        int remove = handler.insertItem(p, copyCopy, false).getCount()+(copy.getCount()-copyCopy.getCount());
                        if (remove < copy.getCount()) {
                            movedAnything = true;
                        }
                        copy.setCount(remove);
                        if (remove <= 0) {
                            break;
                        }
                    }
                    p++;
                }
                if (movedAnything) {
                    resolved.extractItem(o, copy2.getCount() - copy.getCount(), false);
                    break;
                }
            }
        }
        return movedAnything;
    }
}
