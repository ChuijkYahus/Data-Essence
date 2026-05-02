package EsetKalenko.Halcyon.client.particle;

import EsetKalenko.Halcyon.registry.ParticleRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class CircleParticleOptions extends ConfigurableParticleOptions implements ParticleOptions {
    @Override
    public ParticleType<?> getType() {
        return ParticleRegistry.CIRCLE.get();
    }
    public static final MapCodec<CircleParticleOptions> CODEC = createCodec(CircleParticleOptions::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, CircleParticleOptions> STREAM_CODEC = createStreamCodec(CircleParticleOptions::new);
}