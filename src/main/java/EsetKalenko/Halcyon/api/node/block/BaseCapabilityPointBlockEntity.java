package EsetKalenko.Halcyon.api.node.block;

import EsetKalenko.Halcyon.api.util.BlockPosEdge;
import com.cmdpro.databank.model.animation.DatabankAnimationReference;
import com.cmdpro.databank.model.animation.DatabankAnimationState;
import EsetKalenko.Halcyon.DataNEssence;
import EsetKalenko.Halcyon.api.misc.BlockPosNetworks;
import EsetKalenko.Halcyon.api.node.item.INodeUpgrade;
import EsetKalenko.Halcyon.client.particle.CircleParticleOptions;
import EsetKalenko.Halcyon.registry.AttachmentTypeRegistry;
import com.jgalgo.alg.common.Path;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseCapabilityPointBlockEntity extends BlockEntity {
    private int backoff;
    private int delay;

    public DatabankAnimationState animState = new DatabankAnimationState("idle")
            .addAnim(new DatabankAnimationReference("idle", (state, anim) -> {}, (state, anim) -> {}));

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        animState.setLevel(level);
    }

    public List<BlockPos> link;
    public boolean isRelay;
    boolean wasRelay;

    public final ItemStackHandler universalUpgrade = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    public final ItemStackHandler uniqueUpgrade = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    @SuppressWarnings("unchecked")
    public <T> T getValue(ResourceLocation id, T defaultValue) {
        T value = defaultValue;
        if (universalUpgrade.getStackInSlot(0).getItem() instanceof INodeUpgrade upgrade) {
            Object modified = upgrade.getValue(universalUpgrade.getStackInSlot(0), id, value, this);
            if (modified != null) {
                value = (T)modified;
            }
        }
        if (uniqueUpgrade.getStackInSlot(0).getItem() instanceof INodeUpgrade upgrade) {
            Object modified = upgrade.getValue(uniqueUpgrade.getStackInSlot(0), id, value, this);
            if (modified != null) {
                value = (T)modified;
            }
        }
        return value;
    }
    public float getFinalSpeed(float value) {
        return value*getValue(DataNEssence.locate("speed_multiplier"), 1f);
    }
    public int getFinalSpeed(int value) {
        return (int)(value*getValue(DataNEssence.locate("speed_multiplier"), 1f));
    }

    public BaseCapabilityPointBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null) {
            if (!level.isClientSide) {
                BlockPosNetworks networks = level.getData(AttachmentTypeRegistry.CAPABILITY_NODE_NETWORKS);
                if (!networks.graph.vertices().contains(getBlockPos())) {
                    networks.graph.addVertex(getBlockPos());
                }
            }
        }
    }
    public abstract Color[] linkColor();

    private List<Path<BlockPos, BlockPosEdge>> ends;
    private long cachedVersion = -1;

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, BaseCapabilityPointBlockEntity pBlockEntity) {
        if (!pLevel.isClientSide()) {
            if (pBlockEntity.link == null) {
                pBlockEntity.updateLinks();
            }
            BlockPosNetworks networks = pLevel.getData(AttachmentTypeRegistry.CAPABILITY_NODE_NETWORKS);
            var shouldTransfer = networks.graph.inEdges(pPos).isEmpty() || networks.graph.outEdges(pPos).isEmpty();

            if (shouldTransfer) {
                if (pBlockEntity.delay > 0) {
                    pBlockEntity.delay--;
                } else {
                    if (pBlockEntity.ends == null || pBlockEntity.cachedVersion != networks.graph.getVersion()) {
                        if (pBlockEntity.ends == null) {
                            pBlockEntity.ends = new ArrayList<>();
                        }
                        pBlockEntity.ends.clear();
                        var paths = networks.graph.getPaths(pPos);
                        for (var vertex : networks.graph.vertices()) {
                            if (pPos.equals(vertex)) {
                                continue;
                            }

                            if (!networks.graph.outEdges(vertex).isEmpty()) {
                                continue;
                            }

                            if (!pLevel.isLoaded(vertex)) {
                                continue;
                            }

                            var path = paths.getPath(vertex);
                            if (path != null) {
                                pBlockEntity.ends.add(path);
                            }
                        }
                    }
                    
                    pBlockEntity.preTransferHooks(pBlockEntity, pBlockEntity.ends);
                    if (pBlockEntity.transfer(pBlockEntity, pBlockEntity.ends)) {
                        pBlockEntity.backoff = Math.max(0, pBlockEntity.backoff >> 1);
                    } else {
                        pBlockEntity.backoff = Math.min(pBlockEntity.maxBackoff(), pBlockEntity.backoff == 0 ? 1 : pBlockEntity.backoff << 1);
                    }
                    pBlockEntity.delay = pBlockEntity.backoff;
                    pBlockEntity.postTransferHooks(pBlockEntity, pBlockEntity.ends);
                }
            }

            // check if this node is a relay
            var incoming = networks.graph.inEdges(pPos);
            var outgoing = networks.graph.outEdges(pPos);
            pBlockEntity.isRelay = (!incoming.isEmpty() && !outgoing.isEmpty());

        } else {
            if (pBlockEntity.link == null) {
                pBlockEntity.link = new ArrayList<>();
            }
            if (pBlockEntity.wasRelay != pBlockEntity.isRelay) {
                Color color = pBlockEntity.linkColor()[0];
                for (int i = 0; i < 32; i++) {
                    CircleParticleOptions options = new CircleParticleOptions();
                    options.setColor(color);
                    Vec3 center = pPos.getCenter();
                    float angle = (360f/32f)*(float)i;
                    Vector3f spd = new Vector3f((float)Math.sin(Math.toRadians(angle)), 0, (float)Math.cos(Math.toRadians(angle))).mul(0.2f);
                    AttachFace attachFace = pState.getValue(BaseCapabilityPoint.FACE);
                    Direction direction = pState.getValue(BaseCapabilityPoint.FACING);
                    if (attachFace.equals(AttachFace.WALL)) {
                        spd.rotate(direction.getRotation());
                    }
                    pLevel.addParticle(options, center.x, center.y, center.z, spd.x, spd.y, spd.z);
                }
            }
            pBlockEntity.wasRelay = pBlockEntity.isRelay;
        }
    }
    public void updateLinks() {
        if (link == null) {
            link = new ArrayList<>();
        }
        link.clear();
        ends = null;
        BlockPosNetworks networks = level.getData(AttachmentTypeRegistry.CAPABILITY_NODE_NETWORKS);
        if (networks.graph.vertices().contains(getBlockPos())) {
            for (var i : networks.graph.outEdges(getBlockPos())) {
                link.add(i.target());
            }
        }
    }
    public boolean preTransferHooks(BlockEntity from, List<Path<BlockPos, BlockPosEdge>> other) {
        boolean cancel = false;
        if (universalUpgrade.getStackInSlot(0).getItem() instanceof INodeUpgrade upgrade) {
            if (upgrade.preTransfer(universalUpgrade.getStackInSlot(0), from, other, cancel)) {
                cancel = true;
            }
        }
        if (uniqueUpgrade.getStackInSlot(0).getItem() instanceof INodeUpgrade upgrade) {
            if (upgrade.preTransfer(uniqueUpgrade.getStackInSlot(0), from, other, cancel)) {
                cancel = true;
            }
        }
        return cancel;
    }
    public void postTransferHooks(BlockEntity from, List<Path<BlockPos, BlockPosEdge>> other) {
        if (universalUpgrade.getStackInSlot(0).getItem() instanceof INodeUpgrade upgrade) {
            upgrade.postTransfer(universalUpgrade.getStackInSlot(0), from, other);
        }
        if (uniqueUpgrade.getStackInSlot(0).getItem() instanceof INodeUpgrade upgrade) {
            upgrade.postTransfer(uniqueUpgrade.getStackInSlot(0), from, other);
        }
    }

    /**
     * @param from The origin point
     * @param other The other points in the graph
     * @return Whether any work was done.
     */
    public abstract boolean transfer(BaseCapabilityPointBlockEntity from, List<Path<BlockPos, BlockPosEdge>> other);

    /**
     * @return The maximum number of ticks to exponentially backoff to.
     */
    public int maxBackoff() {
        return 0;
    }

    public Direction getDirection() {
        if (getBlockState().getValue(BaseCapabilityPoint.FACE).equals(AttachFace.CEILING)) {
            return Direction.DOWN;
        }
        if (getBlockState().getValue(BaseCapabilityPoint.FACE).equals(AttachFace.WALL)) {
            return getBlockState().getValue(BaseCapabilityPoint.FACING);
        }
        return Direction.UP;
    }
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider pRegistries) {
        CompoundTag tag = pkt.getTag();
        ListTag list = (ListTag)tag.get("link");
        if (link == null) {
            link = new ArrayList<>();
        }
        link.clear();
        for (Tag i : list) {
            CompoundTag blockpos = (CompoundTag)i;
            link.add(new BlockPos(blockpos.getInt("linkX"), blockpos.getInt("linkY"), blockpos.getInt("linkZ")));
        }
        uniqueUpgrade.deserializeNBT(pRegistries, tag.getCompound("uniqueUpgrade"));
        universalUpgrade.deserializeNBT(pRegistries, tag.getCompound("universalUpgrade"));
        isRelay = tag.getBoolean("Relay");
    }
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        if (link != null) {
            for (BlockPos target : link) {
                CompoundTag blockpos = new CompoundTag();
                blockpos.putInt("linkX", target.getX());
                blockpos.putInt("linkY", target.getY());
                blockpos.putInt("linkZ", target.getZ());
                list.add(blockpos);
            }
        }
        tag.put("link", list);
        tag.put("uniqueUpgrade", uniqueUpgrade.serializeNBT(pRegistries));
        tag.put("universalUpgrade", universalUpgrade.serializeNBT(pRegistries));
        tag.putBoolean("Relay", isRelay);
        return tag;
    }
    public void updateBlock() {
        updateLinks();
        BlockPosNetworks networks = level.getData(AttachmentTypeRegistry.CAPABILITY_NODE_NETWORKS);
        var incoming = networks.graph.inEdges(getBlockPos());
        var outgoing = networks.graph.outEdges(getBlockPos());
        isRelay = (!incoming.isEmpty() && !outgoing.isEmpty());
        BlockState blockState = level.getBlockState(this.getBlockPos());
        this.level.sendBlockUpdated(this.getBlockPos(), blockState, blockState, 3);
        this.setChanged();
    }
    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(tag, pRegistries);
        ListTag list = (ListTag)tag.get("link");
        if (link == null) {
            link = new ArrayList<>();
        }
        link.clear();
        for (Tag i : list) {
            CompoundTag blockpos = (CompoundTag)i;
            link.add(new BlockPos(blockpos.getInt("linkX"), blockpos.getInt("linkY"), blockpos.getInt("linkZ")));
        }
        uniqueUpgrade.deserializeNBT(pRegistries, tag.getCompound("uniqueUpgrade"));
        universalUpgrade.deserializeNBT(pRegistries, tag.getCompound("universalUpgrade"));
        isRelay = tag.getBoolean("Relay");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(tag, pRegistries);
        tag.put("uniqueUpgrade", uniqueUpgrade.serializeNBT(pRegistries));
        tag.put("universalUpgrade", universalUpgrade.serializeNBT(pRegistries));
        ListTag list = new ListTag();
        if (link != null) {
            for (BlockPos target : link) {
                CompoundTag blockpos = new CompoundTag();
                blockpos.putInt("linkX", target.getX());
                blockpos.putInt("linkY", target.getY());
                blockpos.putInt("linkZ", target.getZ());
                list.add(blockpos);
            }
        }
        tag.put("link", list);
        tag.putBoolean("Relay", isRelay);
    }
}
