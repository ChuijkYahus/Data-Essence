package EsetKalenko.Halcyon.block.transportation;

import EsetKalenko.Halcyon.api.util.BlockPosEdge;
import com.cmdpro.databank.model.animation.DatabankAnimationReference;
import com.cmdpro.databank.model.animation.DatabankAnimationState;
import EsetKalenko.Halcyon.api.misc.BlockPosNetworks;
import EsetKalenko.Halcyon.api.pearlnetwork.PearlNetworkBlockEntity;
import EsetKalenko.Halcyon.networking.ModMessages;
import EsetKalenko.Halcyon.networking.packet.s2c.vfx.PlayEnderPearlRedirectionEffect;
import EsetKalenko.Halcyon.registry.AttachmentTypeRegistry;
import EsetKalenko.Halcyon.registry.BlockEntityRegistry;
import com.jgalgo.alg.common.Path;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EnderPearlCaptureBlockEntity extends PearlNetworkBlockEntity {
    public DatabankAnimationState animState = new DatabankAnimationState("idle")
            .addAnim(new DatabankAnimationReference("idle", (state, anim) -> {}, (state, anim) -> {}));

    public EnderPearlCaptureBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.ENDER_PEARL_CAPTURE.get(), pos, blockState);
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        animState.setLevel(level);
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, EnderPearlCaptureBlockEntity pBlockEntity) {
        if (!pLevel.isClientSide) {
            List<ThrownEnderpearl> pearls = pLevel.getEntitiesOfClass(ThrownEnderpearl.class, AABB.ofSize(pPos.getCenter(), 5, 5, 5));
            if (!pearls.isEmpty()) {
                BlockPosNetworks networks = pLevel.getData(AttachmentTypeRegistry.ENDER_PEARL_NETWORKS);
                var paths = networks.getPaths(pPos);
                List<Path<BlockPos, BlockPosEdge>> ends = networks.graph.vertices().stream()
                        .filter((vertex) -> networks.graph.outEdges(vertex).isEmpty() && paths.isReachable(vertex))
                        .map(paths::getPath)
                        .filter((i) -> pLevel.getBlockEntity(i.target()) instanceof EnderPearlDestinationBlockEntity).toList();
                if (!ends.isEmpty()) {
                    for (ThrownEnderpearl i : pearls) {
                        if (i.getOwner() != null) {
                            Entity owner = i.getOwner();
                            Path<BlockPos, BlockPosEdge> end = ends.get(owner.getRandom().nextInt(0, ends.size()));
                            Vec3 pos = end.target().getCenter();
                            owner.teleportTo(pos.x, pos.y, pos.z);
                            List<BlockPos> vertexes = end.vertices();
                            for (ServerPlayer j : ((ServerLevel) pLevel).players()) {
                                List<BlockPos> nodes = vertexes.stream().filter((block) -> block.getCenter().distanceTo(j.position()) <= 64).toList();
                                PlayEnderPearlRedirectionEffect message = new PlayEnderPearlRedirectionEffect(nodes);
                                boolean canSend = !nodes.isEmpty();
                                if (canSend) {
                                    ModMessages.sendToPlayer(message, j);
                                }
                            }
                            i.remove(Entity.RemovalReason.DISCARDED);
                        }
                    }
                }
            }
        }
    }
}
