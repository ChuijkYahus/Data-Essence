package EsetKalenko.Halcyon.client.particle;

import EsetKalenko.Halcyon.registry.ParticleRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class MoteParticleOptions extends ConfigurableParticleOptions implements ParticleOptions {
    @Override
    public ParticleType<?> getType() {
        return ParticleRegistry.MOTE.get();
    }
    public static final MapCodec<MoteParticleOptions> CODEC = createCodec(MoteParticleOptions::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, MoteParticleOptions> STREAM_CODEC = createStreamCodec(MoteParticleOptions::new);
}