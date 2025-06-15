package com.cmdpro.datanessence.block.production;

import com.cmdpro.datanessence.api.essence.EssenceBlockEntity;
import com.cmdpro.datanessence.api.essence.EssenceStorage;
import com.cmdpro.datanessence.api.essence.container.SingleEssenceContainer;
import com.cmdpro.datanessence.registry.BlockEntityRegistry;
import com.cmdpro.datanessence.registry.EssenceTypeRegistry;
import com.cmdpro.datanessence.registry.SoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

public class FluidSpillerBlockEntity extends BlockEntity implements EssenceBlockEntity {
    public SingleEssenceContainer storage = new SingleEssenceContainer(EssenceTypeRegistry.ESSENCE.get(), 1000);
    @Override
    public EssenceStorage getStorage() {
        return storage;
    }
    private final FluidTank fluidHandler = new FluidTank(4000);
    public FluidSpillerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.FLUID_SPILLER.get(), pos, state);
    }
    public IFluidHandler getFluidHandler() {
        return fluidHandler;
    }
    
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider pRegistries) {
        tag.put("EssenceStorage", storage.toNbt());
        tag.put("fluid", fluidHandler.writeToNBT(pRegistries, new CompoundTag()));
        tag.putInt("cooldown", cooldown);
        super.saveAdditional(tag, pRegistries);
    }
    public int cooldown;
    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        super.loadAdditional(nbt, pRegistries);
        fluidHandler.readFromNBT(pRegistries, nbt.getCompound("fluid"));
        storage.fromNbt(nbt.getCompound("EssenceStorage"));
        cooldown = nbt.getInt("cooldown");
    }
    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, FluidSpillerBlockEntity pBlockEntity) {
        if (!pLevel.isClientSide()) {
            if (pBlockEntity.cooldown <= 0) {
                if (pBlockEntity.getStorage().getEssence(EssenceTypeRegistry.ESSENCE.get()) >= 25) {
                    BlockPos pos = pPos.relative(pState.getValue(FluidCollector.FACING));
                    FluidState state = pLevel.getFluidState(pos);
                    BlockState state2 = pLevel.getBlockState(pos);
                    FluidStack stack = new FluidStack(pBlockEntity.fluidHandler.getFluid().getFluid(), 1000);
                    if ((state.isEmpty() || !state.isSource()) && state2.canBeReplaced(stack.getFluid())) {
                        if (pBlockEntity.fluidHandler.getFluidAmount() >= 1000) {
                            pBlockEntity.fluidHandler.drain(stack, IFluidHandler.FluidAction.EXECUTE);
                            pLevel.setBlockAndUpdate(pos, stack.getFluid().defaultFluidState().createLegacyBlock());
                            pLevel.playSound(null, pPos, SoundRegistry.FLUID_SPILLER_EMPTY.value(), SoundSource.BLOCKS, 2, 1);
                            pBlockEntity.getStorage().removeEssence(EssenceTypeRegistry.ESSENCE.get(), 25);
                        }
                    }
                }
            } else {
                pBlockEntity.cooldown -= 1;
            }
        }
    }
}
