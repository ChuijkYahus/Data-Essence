package EsetKalenko.Halcyon.client.particle;

import EsetKalenko.Halcyon.registry.ParticleRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class RhombusParticleOptions extends ConfigurableParticleOptions implements ParticleOptions {
    @Override
    public ParticleType<?> getType() {
        return ParticleRegistry.RHOMBUS.get();
    }
    public static final MapCodec<RhombusParticleOptions> CODEC = createCodec(RhombusParticleOptions::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, RhombusParticleOptions> STREAM_CODEC = createStreamCodec(RhombusParticleOptions::new);
}