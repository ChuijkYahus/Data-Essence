package com.cmdpro.datanessence.block.decoration;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MakutuinRunner extends Block {
    public static final EnumProperty<Direction.Axis> FACING = BlockStateProperties.HORIZONTAL_AXIS;
    public static final VoxelShape SHAPE_NS = Block.box(0, 0, 1, 16, 1, 15);
    public static final VoxelShape SHAPE_EW = Block.box(1, 0, 0, 15, 1, 16);

    public MakutuinRunner(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.Axis.X));
    }

    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return (state.getValue(FACING) == Direction.Axis.X)
                ? SHAPE_NS
                : SHAPE_EW;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getAxis());
    }
}
