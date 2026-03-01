package com.cmdpro.datanessence.block.decoration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MakutuinRunnerCross extends MakutuinRunner {
    public static final VoxelShape SHAPE = Shapes.join(SHAPE_EW, SHAPE_NS, BooleanOp.OR);

    public MakutuinRunnerCross(Properties properties) {
        super(properties);
    }

    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
