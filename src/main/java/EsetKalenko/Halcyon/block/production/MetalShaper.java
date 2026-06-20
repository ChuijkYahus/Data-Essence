package EsetKalenko.Halcyon.block.production;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class MetalShaper extends Block implements EntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public MetalShaper(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    // I LOVE VOXELSHAPES I LOVE VOXELSHAPES
    private static final VoxelShape OBELISK_N = box(10.0, 0.0, 1.0,
            17.0, 14.0, 15.0);
    private static final VoxelShape OBELISK_S = box(-1.0, 0.0, 1.0,
            6.0, 14.0, 15.0);
    private static final VoxelShape OBELISK_E = box(1.0, 0.0, 10.0,
            15.0, 14.0, 17.0);
    private static final VoxelShape OBELISK_W = box(1.0, 0.0, -1.0,
            15.0, 14.0, 6.0);
    private static final VoxelShape BASE_N = Shapes.or(OBELISK_N, box(0, 0, 0,
            10, 2, 10));
    private static final VoxelShape BASE_S = Shapes.or(OBELISK_S, box(6, 0, 6,
            16, 2, 16));
    private static final VoxelShape BASE_E = Shapes.or(OBELISK_E, box(6, 0, 0,
            16, 2, 10));
    private static final VoxelShape BASE_W = Shapes.or(OBELISK_W, box(0, 0, 6,
            10, 2, 16));
    private static final VoxelShape BACK_N = Shapes.or(BASE_N, box(1, 0, 10,
            9, 16, 14));
    private static final VoxelShape BACK_S = Shapes.or(BASE_S, box(7, 0, 2,
            15, 16, 6));
    private static final VoxelShape BACK_E = Shapes.or(BASE_E, box(2, 0, 1,
            6, 16, 9));
    private static final VoxelShape BACK_W = Shapes.or(BASE_W, box(10, 0, 7,
            14, 16, 15));
    private static final VoxelShape SHAPE_N = Shapes.or(BACK_N, box(1, 14, 1,
            9, 17, 9));
    private static final VoxelShape SHAPE_S = Shapes.or(BACK_S, box(7, 14, 7,
            15, 17, 15));
    private static final VoxelShape SHAPE_E = Shapes.or(BACK_E, box(7, 14, 1,
            15, 17, 9));
    private static final VoxelShape SHAPE_W = Shapes.or(BACK_W, box(1, 14, 7,
            9, 17, 15));

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (pState.getValue(FACING)) {
            case NORTH -> SHAPE_N;
            case SOUTH -> SHAPE_S;
            case EAST -> SHAPE_E;
            case WEST -> SHAPE_W;
            default -> Shapes.block();
        };
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        MetalShaperBlockEntity ent = new MetalShaperBlockEntity(pPos, pState);
        return ent;
    }
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof MetalShaperBlockEntity) {
                ((MetalShaperBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof MetalShaperBlockEntity ent) {
                pPlayer.openMenu(ent, pPos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof MetalShaperBlockEntity ent) {
                MetalShaperBlockEntity.tick(lvl, pos, st, ent);
            }
        };
    }

    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }
}
