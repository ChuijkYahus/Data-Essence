package EsetKalenko.Halcyon.block.processing;

import EsetKalenko.Halcyon.Halcyon;
import EsetKalenko.Halcyon.client.particle.CircleParticleOptions;
import EsetKalenko.Halcyon.client.particle.CircleShadeParticleOptions;
import EsetKalenko.Halcyon.client.particle.MoteParticleOptions;
import com.cmdpro.databank.model.animation.DatabankAnimationReference;
import com.cmdpro.databank.model.animation.DatabankAnimationState;
import EsetKalenko.Halcyon.api.DataNEssenceRegistries;
import EsetKalenko.Halcyon.api.block.BaseFabricatorBlockEntity;
import EsetKalenko.Halcyon.api.essence.EssenceType;
import EsetKalenko.Halcyon.client.FactorySong;
import EsetKalenko.Halcyon.registry.BlockEntityRegistry;
import EsetKalenko.Halcyon.registry.EssenceTypeRegistry;
import EsetKalenko.Halcyon.registry.SoundRegistry;
import EsetKalenko.Halcyon.screen.LunariumMenu;
import com.cmdpro.databank.multiblock.Multiblock;
import com.cmdpro.databank.multiblock.MultiblockManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

public class LunariumBlockEntity extends BaseFabricatorBlockEntity implements MenuProvider {
    public DatabankAnimationState animState = new DatabankAnimationState("pose")
            .addAnim(new DatabankAnimationReference("pose", (state, anim) -> {}, (state, anim) -> {}));

    final Multiblock structure = MultiblockManager.multiblocks.get(Halcyon.locate("machines/lunarium"));

    static final BlockPos[] vfxWispOffsets = new BlockPos[] {
            new BlockPos(-4, 4, -4),
            new BlockPos(-4, 4, 4),
            new BlockPos(4, 4, -4),
            new BlockPos(4, 4, 4)
    };

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        animState.setLevel(level);
    }

    public LunariumBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.LUNARIUM.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.empty();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new LunariumMenu(pContainerId, pInventory, this);
    }

    @Override
    public List<EssenceType> getSupportedEssenceTypes() {
        return List.of(EssenceTypeRegistry.ESSENCE.get(), EssenceTypeRegistry.LUNAR_ESSENCE.get());
    }

    @Override
    public void tick(Level world, BlockPos pos, BlockState state, BaseFabricatorBlockEntity baseFabricator) {
        super.tick(world, pos, state, baseFabricator);

        if (baseFabricator instanceof LunariumBlockEntity lunarium && world.isClientSide()) {

            if (lunarium.time >= 0) {
                // Vfx; little lunar wisps on the ecliptrum blocks
                for (int i = 0; i < 4; i++) {
                    var color = world.random.nextInt() % 5 == 0 ? 0x2473B4 : 0xFFE3AA;
                    var flame = new CircleParticleOptions()
                            .setColor(new Color(color))
                            .setAdditive(true)
                            .setFriction(0f);

                    var cloud = new CircleShadeParticleOptions()
                            .setColor(new Color(color))
                            .setAdditive(true)
                            .setFriction(0f)
                            .setLifetime(50);

                    var mote = new MoteParticleOptions()
                            .setColor(new Color(color))
                            .setAdditive(true)
                            .setFriction(0f)
                            .setGravity(0.3f);

                    var origin = pos.getCenter().add(
                            vfxWispOffsets[i].getX(),
                            vfxWispOffsets[i].getY(),
                            vfxWispOffsets[i].getZ()
                    );

                    world.addParticle(
                            flame,
                            origin.x,
                            origin.y,
                            origin.z,
                            Mth.nextDouble(world.random, -0.05, 0.05),
                            Mth.nextDouble(world.random, 0.01, 0.2),
                            Mth.nextDouble(world.random, -0.05, 0.05)
                    );

                    world.addParticle(
                            cloud,
                            origin.x,
                            origin.y,
                            origin.z,
                            Mth.nextDouble(world.random, -0.05, 0.05),
                            Mth.nextDouble(world.random, -0.01, -0.2),
                            Mth.nextDouble(world.random, -0.05, 0.05)
                    );

                    if (world.random.nextInt() % 15 == 0)
                        world.addParticle(
                                mote,
                                origin.x,
                                origin.y,
                                origin.z,
                                Mth.nextDouble(world.random, -0.4, 0.4),
                                Mth.nextDouble(world.random, 0.01, 0.2),
                                Mth.nextDouble(world.random, -0.4, 0.4)
                        );

                    // TODO Vfx; crafting finalization rays from ecliptrum capstones to machine
                }

                // Song
                if (lunarium.essenceCost != null) {
                    if (lunarium.essenceCost.containsKey(DataNEssenceRegistries.ESSENCE_TYPE_REGISTRY.getKey(EssenceTypeRegistry.ESSENCE.get()))) {
                        ClientHandler.markIndustrialFactorySong(pos);
                    }
                    if (lunarium.essenceCost.containsKey(DataNEssenceRegistries.ESSENCE_TYPE_REGISTRY.getKey(EssenceTypeRegistry.LUNAR_ESSENCE.get()))) {
                        ClientHandler.markLunarFactorySong(pos);
                    }
                }
            }
        }
    }

    @Override
    public float getMeterSideLength(Direction direction) {
        if (direction.equals(Direction.UP)) {
            return super.getMeterSideLength(direction)*2.8f;
        }
        return super.getMeterSideLength(direction);
    }

    @Override
    public float getMaxEssence() {
        return 2000;
    }

    @Override
    public boolean canCraft() {
        return structure.checkMultiblockAll(level, worldPosition);
    }

    private static class ClientHandler {
        static FactorySong.FactoryLoop industrialSound = FactorySong.getLoop(SoundRegistry.FABRICATOR_LOOP.value());
        static FactorySong.FactoryLoop lunarSound = FactorySong.getLoop(SoundRegistry.LUNARIUM_LOOP.value());

        public static void markIndustrialFactorySong(BlockPos pos) {
            industrialSound.addSource(pos);
        }
        public static void markLunarFactorySong(BlockPos pos) {
            lunarSound.addSource(pos);
        }
    }
}
