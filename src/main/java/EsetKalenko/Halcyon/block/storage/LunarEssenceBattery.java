package EsetKalenko.Halcyon.block.storage;

import EsetKalenko.Halcyon.api.DataNEssenceRegistries;
import EsetKalenko.Halcyon.api.item.ItemEssenceContainer;
import EsetKalenko.Halcyon.registry.DataComponentRegistry;
import EsetKalenko.Halcyon.registry.EssenceTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LunarEssenceBattery extends Block implements EntityBlock {
    private static final VoxelShape SHAPE =  Block.box(0, 0, 0, 16, 16, 16);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
    public LunarEssenceBattery(Properties pProperties) {
        super(pProperties);
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new LunarEssenceBatteryBlockEntity(pPos, pState);
    }
    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pLevel.getBlockEntity(pPos) instanceof LunarEssenceBatteryBlockEntity ent) {
            if (!pLevel.isClientSide) {
                pPlayer.displayClientMessage(Component.translatable("block.datanessence.lunar_essence_battery.amount", ent.storage.getEssence(EssenceTypeRegistry.LUNAR_ESSENCE.get()), ent.storage.getMaxEssence()), true);
            }
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        }
        return super.useWithoutItem(pState, pLevel, pPos, pPlayer, pHitResult);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> drops = super.getDrops(state, params);
        BlockEntity be = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);

        if (be instanceof LunarEssenceBatteryBlockEntity battery) {
            for (ItemStack drop : drops) {

                float amount = battery.storage.getEssence(EssenceTypeRegistry.LUNAR_ESSENCE.get());
                ItemEssenceContainer.addEssence(drop,
                        DataNEssenceRegistries.ESSENCE_TYPE_REGISTRY.getKey(EssenceTypeRegistry.ESSENCE.get()),
                        amount);
            }
        }

        return drops;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (level.getBlockEntity(pos) instanceof LunarEssenceBatteryBlockEntity battery) {
            Float storedEssence = ItemEssenceContainer.getEssence(stack,
                    DataNEssenceRegistries.ESSENCE_TYPE_REGISTRY.getKey(EssenceTypeRegistry.ESSENCE.get()));

            if (storedEssence != null && storedEssence > 0) {
                battery.storage.addEssence(EssenceTypeRegistry.LUNAR_ESSENCE.get(), storedEssence);
                battery.setChanged();
            }
        }
    }
}
