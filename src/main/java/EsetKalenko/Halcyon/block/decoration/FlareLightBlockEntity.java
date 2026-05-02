package EsetKalenko.Halcyon.block.decoration;

import EsetKalenko.Halcyon.client.particle.CircleParticleOptions;
import EsetKalenko.Halcyon.registry.BlockEntityRegistry;
import EsetKalenko.Halcyon.registry.EssenceTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class FlareLightBlockEntity extends BlockEntity {

    public FlareLightBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.FLARE_LIGHT.get(), pos, blockState);
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, FlareLightBlockEntity pBlockEntity) {
        if (pLevel.isClientSide) {
            ClientHandler.spawnParticles(pLevel.random, pPos.getCenter());
        }
    }

    private static class ClientHandler {
        public static void spawnParticles(RandomSource random, Vec3 pos) {
            Vec3 dir = new Vec3(Mth.nextFloat(random, -1f, 1f), Mth.nextFloat(random, -1f, 1f), Mth.nextFloat(random, -1f, 1f)).normalize().multiply(0.01f, 0.1f, 0.01f);
            Vec3 offset = new Vec3(Mth.nextFloat(random, -1f, 1f), Mth.nextFloat(random, -1f, 1f), Mth.nextFloat(random, -1f, 1f)).normalize().multiply(0.01f, 0.1f, 0.01f);
            Minecraft.getInstance().particleEngine.createParticle(new CircleParticleOptions().setColor(new Color(EssenceTypeRegistry.ESSENCE.get().getColor())).setAdditive(true), pos.x + offset.x, pos.y + offset.y, pos.z + offset.z, dir.x, dir.y, dir.z);
        }
    }
}
