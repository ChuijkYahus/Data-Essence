package EsetKalenko.Halcyon.screen.slot;

import EsetKalenko.Halcyon.registry.DataComponentRegistry;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class MoldSlot extends SlotItemHandler {
    public MoldSlot(IItemHandler itemHandler, int index, int x, int y) {
        super(itemHandler, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.has(DataComponentRegistry.MOLD);
    }
}