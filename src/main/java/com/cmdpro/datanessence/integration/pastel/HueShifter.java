package com.cmdpro.datanessence.integration.pastel;

import com.cmdpro.datanessence.api.block.RedirectorInteractable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class HueShifter extends Block implements EntityBlock, RedirectorInteractable {
    // Able to shift Ink (in a Flask) or Pigment between two adjacent colors on the color wheel, according to
    // temperature. Which output is active will be represented by a flower in bloom, with its output item floating
    // above it.
    // Megablock; 2 blocks tall. Will require a rose bush to craft.

    public HueShifter(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onRedirectorUse(UseOnContext context) {
        return false; // change output state. maybe goes in the BE instead
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return EntityBlock.super.getTicker(level, state, blockEntityType);
    }
}
