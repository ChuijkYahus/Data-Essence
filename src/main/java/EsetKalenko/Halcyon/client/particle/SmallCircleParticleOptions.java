package EsetKalenko.Halcyon.client.particle;

import EsetKalenko.Halcyon.registry.ParticleRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class SmallCircleParticleOptions extends ConfigurableParticleOptions implements ParticleOptions {
    @Override
    public ParticleType<?> getType() {
        return ParticleRegistry.SMALL_CIRCLE.get();
    }
    public static final MapCodec<SmallCircleParticleOptions> CODEC = createCodec(SmallCircleParticleOptions::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, SmallCircleParticleOptions> STREAM_CODEC = createStreamCodec(SmallCircleParticleOptions::new);
}