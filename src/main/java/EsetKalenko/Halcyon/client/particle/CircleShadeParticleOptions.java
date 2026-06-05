package EsetKalenko.Halcyon.client.particle;

import EsetKalenko.Halcyon.registry.ParticleRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class CircleShadeParticleOptions extends ConfigurableParticleOptions {
    @Override
    public ParticleType<?> getType() {
        return ParticleRegistry.CIRCLE_SHADE.get();
    }

    public static final MapCodec<CircleShadeParticleOptions> CODEC = createCodec(CircleShadeParticleOptions::new);

    public static final StreamCodec<RegistryFriendlyByteBuf, CircleShadeParticleOptions> STREAM_CODEC
            = createStreamCodec(CircleShadeParticleOptions::new);
}
