package EsetKalenko.Halcyon.api.block;

import EsetKalenko.Halcyon.api.util.DataBankUtil;
import EsetKalenko.Halcyon.item.DataTablet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class BaseDataBankBlock extends Block {
    public BaseDataBankBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide) {
            if (isOkayToOpen(pState, pLevel, pPos, pPlayer, pHitResult)) {
                if (canUse(pState, pLevel, pPos, pPlayer, pHitResult)) {
                    DataBankUtil.sendDataBankEntries(pPlayer, getEntries(pState, pLevel, pPos, pPlayer, pHitResult));
                }
                else {
                    pPlayer.displayClientMessage(Component.translatable("block.halcyon.data_bank.cannot_use"), true);
                }
            }
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }
    public boolean canUse(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        return pPlayer.getInventory().contains((stack) -> stack.getItem() instanceof DataTablet);
    }
    public boolean isOkayToOpen(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        return true;
    }
    public abstract ResourceLocation[] getEntries(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult);
}
