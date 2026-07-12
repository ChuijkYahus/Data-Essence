package EsetKalenko.Halcyon.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record ShardSublimationMap(Map<ResourceLocation, Float> energyContained) {
    public static final Codec<ShardSublimationMap> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                Codec.unboundedMap(ResourceLocation.CODEC, Codec.FLOAT)
                        .fieldOf("energy_contained")
                        .forGetter(ShardSublimationMap::energyContained)
            ).apply(instance, ShardSublimationMap::new));
}
