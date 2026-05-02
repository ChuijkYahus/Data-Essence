package EsetKalenko.Halcyon.networking.packet.s2c;

import EsetKalenko.Halcyon.DataNEssence;
import EsetKalenko.Halcyon.api.util.client.ClientRenderingUtil;
import EsetKalenko.Halcyon.moddata.ClientPlayerData;
import EsetKalenko.Halcyon.networking.Message;
import EsetKalenko.Halcyon.toasts.TierToast;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PlayerTierSync(int tier, boolean showIndicator) implements Message {

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static final Type<PlayerTierSync> TYPE = new Type<>(DataNEssence.locate("player_tier_sync"));

    public static void write(RegistryFriendlyByteBuf pBuffer, PlayerTierSync obj) {
        pBuffer.writeInt(obj.tier);
        pBuffer.writeBoolean(obj.showIndicator);
    }

    public static PlayerTierSync read(RegistryFriendlyByteBuf buf) {
        int tier = buf.readInt();
        boolean showIndicator = buf.readBoolean();
        return new PlayerTierSync(tier, showIndicator);
    }

    @Override
    public void handleClient(Minecraft minecraft, Player player, IPayloadContext ctx) {
        ClientPlayerData.setTier(tier);
        if (showIndicator) {
            ClientRenderingUtil.progressionShader();
            ClientHandler.addToast(tier);
        }
    }
    private static class ClientHandler {
        public static void addToast(int tier) {
            Minecraft.getInstance().getToasts().addToast(new TierToast(tier));
        }
    }
}