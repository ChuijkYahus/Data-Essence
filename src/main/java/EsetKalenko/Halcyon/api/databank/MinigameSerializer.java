package EsetKalenko.Halcyon.api.databank;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public abstract class MinigameSerializer<T extends MinigameCreator> {
    public abstract MapCodec<T> getCodec();
    public abstract StreamCodec<RegistryFriendlyByteBuf, T> getStreamCodec();
}
