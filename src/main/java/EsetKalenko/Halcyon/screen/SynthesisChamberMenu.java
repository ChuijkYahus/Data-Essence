package EsetKalenko.Halcyon.screen;

import EsetKalenko.Halcyon.util.IDataNEssenceMenuHelper;
import EsetKalenko.Halcyon.block.processing.SynthesisChamberBlockEntity;
import EsetKalenko.Halcyon.registry.BlockRegistry;
import EsetKalenko.Halcyon.registry.MenuRegistry;
import EsetKalenko.Halcyon.screen.slot.DataDriveSlot;
import EsetKalenko.Halcyon.screen.slot.ModResultSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

public class SynthesisChamberMenu extends AbstractContainerMenu implements IDataNEssenceMenuHelper {
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return guiHelperQuickMoveStack(player, index, 3, this);
    }
    @Override
    public boolean guiHelperMoveItemStackTo(ItemStack pStack, int pStartIndex, int pEndIndex, boolean pReverseDirection) {
        return moveItemStackTo(pStack, pStartIndex, pEndIndex, pReverseDirection);
    }
    public final SynthesisChamberBlockEntity blockEntity;
    private final Level level;
    public SynthesisChamberMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }
    public SynthesisChamberMenu(int pContainerId, Inventory inv, BlockEntity entity) {
        super(MenuRegistry.SYNTHESIS_CHAMBER_MENU.get(), pContainerId);
        blockEntity = ((SynthesisChamberBlockEntity) entity);
        this.level = inv.player.level();
        addPlayerInventory(inv);
        addPlayerHotbar(inv);
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 0, 62, 22));
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 1, 62, 45));
        this.addSlot(new ModResultSlot(blockEntity.getOutputHandler(), 0, 116, 34));
        this.addSlot(new DataDriveSlot(blockEntity.getDataDriveHandler(), 0, 152, 8));
    }

    @Override
    public void slotsChanged(Container pContainer) {
        super.slotsChanged(pContainer);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, BlockRegistry.SYNTHESIS_CHAMBER.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

}
