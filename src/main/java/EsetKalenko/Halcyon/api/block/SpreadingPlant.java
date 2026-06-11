package EsetKalenko.Halcyon.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;

public class SpreadingPlant extends Block implements GenderfluidGrowable {
    public int spreadChance; // how often this plant grows by spreading, like mushrooms

    public SpreadingPlant(Properties properties, int spreadChance) {
        super(properties);
        this.spreadChance = spreadChance;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (random.nextInt() % spreadChance == 0)
            grow(state, world, pos, random);
    }

    @Override
    public void grow(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        BlockPos candidate = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);

        for (int k = 0; k < 4; k++) {
            if (world.isEmptyBlock(candidate) && state.canSurvive(world, candidate)) {
                pos = candidate;
            }
            candidate = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
        }

        if (world.isEmptyBlock(candidate) && state.canSurvive(world, candidate)) {
            world.setBlock(candidate, state, 2);
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState pState) {
        return true;
    }
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(BlockTags.DIRT) || pState.getBlock() instanceof net.minecraft.world.level.block.FarmBlock;
    }
    @Override
    protected BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return !pState.canSurvive(pLevel, pCurrentPos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    protected boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockPos blockpos = pPos.below();
        BlockState belowBlockState = pLevel.getBlockState(blockpos);
        net.neoforged.neoforge.common.util.TriState soilDecision = belowBlockState.canSustainPlant(pLevel, blockpos, Direction.UP, pState);
        if (!soilDecision.isDefault()) return soilDecision.isTrue();
        return this.mayPlaceOn(belowBlockState, pLevel, blockpos);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return pState.getFluidState().isEmpty();
    }

    @Override
    protected boolean isPathfindable(BlockState pState, PathComputationType pPathComputationType) {
        return pPathComputationType == PathComputationType.AIR && !this.hasCollision || super.isPathfindable(pState, pPathComputationType);
    }
}
