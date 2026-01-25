package com.cmdpro.datanessence.block.auxiliary;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class TwiningLantern extends LanternBlock implements EntityBlock {
    protected static final VoxelShape HANGING_AABB = Shapes.or
            (Block.box
                    (5.0, 5.0, 5.0, 11.0, 12.0, 11.0),
                    Block.box(
                            6.0, 12.0, 6.0, 10.0, 14.0, 10.0));

    public TwiningLantern(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TwiningLanternBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof TwiningLanternBlockEntity ent) {
                TwiningLanternBlockEntity.tick(lvl, pos, st, ent);
            }
        };
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(HANGING) ? HANGING_AABB : AABB;
    }
}
