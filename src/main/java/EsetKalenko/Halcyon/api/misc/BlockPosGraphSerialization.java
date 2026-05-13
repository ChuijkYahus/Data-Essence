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
        public static final Codec<SerializationGraph> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                BlockPos.CODEC.listOf().fieldOf("vertices").forGetter((graph) -> graph.vertices),
                BlockPosEdge.CODEC.listOf().fieldOf("edges").forGetter((graph) -> graph.edges)
        ).apply(instance, SerializationGraph::new));
        public SerializationGraph(List<BlockPos> vertices, List<BlockPosEdge> edges) {
            this.vertices = vertices;
            this.edges = edges;
        }
        public List<BlockPos> vertices;
        public List<BlockPosEdge> edges;
        public static Graph<BlockPos, BlockPosEdge> toGraph(SerializationGraph graphSerialization) {
            Graph<BlockPos, BlockPosEdge> graph = Graph.newDirected();
            for (BlockPos i : graphSerialization.vertices) {
                graph.addVertex(i);
            }
            for (BlockPosEdge i : graphSerialization.edges) {
                graph.addEdge(i.source, i.target, i);
            }
            return graph;
        }
        public static SerializationGraph fromGraph(Graph<BlockPos, BlockPosEdge> graph) {
            List<BlockPos> vertices = new ArrayList<>(graph.vertices());
            List<BlockPosEdge> edges = graph.edges().stream().map((edge) -> new BlockPosEdge(graph.edgeSource(edge), graph.edgeTarget(edge))).toList();
            return new SerializationGraph(vertices, edges);
        }
    }
}
