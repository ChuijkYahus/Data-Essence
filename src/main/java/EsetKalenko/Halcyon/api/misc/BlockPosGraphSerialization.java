package EsetKalenko.Halcyon.api.misc;

import EsetKalenko.Halcyon.api.util.BlockPosEdge;
import com.jgalgo.graph.Graph;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class BlockPosGraphSerialization {
    public static class SerializationGraph {
        public List<BlockPos> vertices;
        public List<BlockPosEdge> edges;

        public static final Codec<SerializationGraph> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                BlockPos.CODEC.listOf().fieldOf("vertices").forGetter((graph) -> graph.vertices),
                BlockPosEdge.CODEC.listOf().fieldOf("edges").forGetter((graph) -> graph.edges)
        ).apply(instance, SerializationGraph::new));

        public SerializationGraph(List<BlockPos> vertices, List<BlockPosEdge> edges) {
            this.vertices = vertices;
            this.edges = edges;
        }

        public static Graph<BlockPos, BlockPosEdge> toGraph(SerializationGraph graphSerialization) {
            Graph<BlockPos, BlockPosEdge> graph = Graph.newDirected();
            graph.addVertices(graphSerialization.vertices);
            for (BlockPosEdge i : graphSerialization.edges) {
                graph.addEdge(i.source, i.target, i);
            }
            return graph;
        }
        public static SerializationGraph fromGraph(Graph<BlockPos, BlockPosEdge> graph) {
            List<BlockPos> vertices = new ArrayList<>(graph.vertices());
            List<BlockPosEdge> edges = new ArrayList<>(graph.edges());
            return new SerializationGraph(vertices, edges);
        }
    }
}
