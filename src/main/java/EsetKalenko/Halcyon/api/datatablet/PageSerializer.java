package EsetKalenko.Halcyon.api.datatablet;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public abstract class PageSerializer<T extends Page> {
    public abstract MapCodec<T> getCodec();
    public abstract StreamCodec<RegistryFriendlyByteBuf, T> getStreamCodec();
}
