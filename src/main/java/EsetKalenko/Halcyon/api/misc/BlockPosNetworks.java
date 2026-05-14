package EsetKalenko.Halcyon.api.misc;

import EsetKalenko.Halcyon.api.util.BlockPosGraph;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class BlockPosNetworks {
    public BlockPosGraph graph;
    public static final Codec<BlockPosNetworks> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        BlockPosGraphSerialization.SerializationGraph.CODEC.fieldOf("graph").xmap(BlockPosGraphSerialization.SerializationGraph::toGraph, BlockPosGraphSerialization.SerializationGraph::fromGraph).forGetter((networks) -> networks.graph)
    ).apply(instance, BlockPosNetworks::new));

    public BlockPosNetworks(BlockPosGraph graph) {
        this.graph = graph;
    }
}
