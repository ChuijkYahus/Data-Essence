package EsetKalenko.Halcyon.api.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;

public class BlockPosEdge {
    public BlockPos source;
    public BlockPos target;

    public BlockPosEdge(BlockPos source, BlockPos target) {
        this.source = source;
        this.target = target;
    }

    public static final Codec<BlockPosEdge> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            BlockPos.CODEC.fieldOf("source").forGetter((edge) -> edge.source),
            BlockPos.CODEC.fieldOf("target").forGetter((edge) -> edge.target)
    ).apply(instance, BlockPosEdge::new));

    public double distSqr() {
        return this.source.distSqr(this.target);
    }
}
