package EsetKalenko.Halcyon.api.misc;

import EsetKalenko.Halcyon.api.util.BlockPosEdge;
import com.jgalgo.alg.shortestpath.ShortestPathSingleSource;
import com.jgalgo.alg.shortestpath.ShortestPathSingleSourceDijkstra;
import com.jgalgo.graph.Graph;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;

public class BlockPosNetworks {
    public Graph<BlockPos, BlockPosEdge> graph;
    public static final Codec<BlockPosNetworks> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        BlockPosGraphSerialization.SerializationGraph.CODEC.fieldOf("graph").xmap(BlockPosGraphSerialization.SerializationGraph::toGraph, BlockPosGraphSerialization.SerializationGraph::fromGraph).forGetter((networks) -> networks.graph)
    ).apply(instance, BlockPosNetworks::new));

    public BlockPosNetworks(Graph<BlockPos, BlockPosEdge> graph) {
        this.graph = graph;
    }

    public ShortestPathSingleSource.Result<BlockPos, BlockPosEdge> getPaths(BlockPos vertex) {
        return new ShortestPathSingleSourceDijkstra().computeShortestPaths(this.graph, BlockPosEdge::distSqr, vertex);
    }
}
