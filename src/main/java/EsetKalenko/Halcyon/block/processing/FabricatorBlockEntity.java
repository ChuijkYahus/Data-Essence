package EsetKalenko.Halcyon.block.processing;

import com.cmdpro.databank.model.animation.DatabankAnimationReference;
import com.cmdpro.databank.model.animation.DatabankAnimationState;
import EsetKalenko.Halcyon.api.block.BaseFabricatorBlockEntity;
import EsetKalenko.Halcyon.api.essence.EssenceType;
import EsetKalenko.Halcyon.client.FactorySong;
import EsetKalenko.Halcyon.registry.BlockEntityRegistry;
import EsetKalenko.Halcyon.registry.EssenceTypeRegistry;
import EsetKalenko.Halcyon.registry.SoundRegistry;
import EsetKalenko.Halcyon.screen.FabricatorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FabricatorBlockEntity extends BaseFabricatorBlockEntity implements MenuProvider {
    public DatabankAnimationState animState = new DatabankAnimationState("idle")
            .addAnim(new DatabankAnimationReference("idle", (state, anim) -> {}, (state, anim) -> {}))
            .addAnim(new DatabankAnimationReference("ready", (state, anim) -> {}, (state, anim) -> {}));

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        animState.setLevel(level);
    }
    public FabricatorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.FABRICATOR.get(), pos, state);
    }
    @Override
    public Component getDisplayName() {
        return Component.empty();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new FabricatorMenu(pContainerId, pInventory, this);
    }

    public boolean isCrafting() {
        return time >= 0;
    }

    @Override
    public List<EssenceType> getSupportedEssenceTypes() {
        return List.of(EssenceTypeRegistry.ESSENCE.get());
    }

    @Override
    public float getMaxEssence() {
        return 1000;
    }

    @Override
    public void tick(Level world, BlockPos pos, BlockState state, BaseFabricatorBlockEntity fabricator) {
        super.tick(world, pos, state, fabricator);
        if (world.isClientSide()) {
            if (fabricator.time >= 0 && fabricator.essenceCost != null) {
                ClientHandler.markFactorySong(pos);
            }
        }
    }

    private static class ClientHandler {
        static FactorySong.FactoryLoop industrialSound = FactorySong.getLoop(SoundRegistry.FABRICATOR_LOOP.value());

        public static void markFactorySong(BlockPos pos) {
            industrialSound.addSource(pos);
        }
    }
}
