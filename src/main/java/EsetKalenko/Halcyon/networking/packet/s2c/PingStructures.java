package EsetKalenko.Halcyon.networking.packet.s2c;

import EsetKalenko.Halcyon.DataNEssence;
import EsetKalenko.Halcyon.client.gui.PingsGuiLayer;
import EsetKalenko.Halcyon.data.pinging.StructurePing;
import EsetKalenko.Halcyon.networking.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record PingStructures(List<StructurePing> structures) implements Message {
    public static PingStructures read(RegistryFriendlyByteBuf buf) {
        List<StructurePing> structures = buf.readList((pBuffer) -> StructurePing.STREAM_CODEC.decode((RegistryFriendlyByteBuf)pBuffer));
        return  new PingStructures(structures);
    }

    @Override
    public void handleClient(Minecraft minecraft, Player player, IPayloadContext ctx) {
        ClientHandler.addPings(structures);
    }

    public static void write(RegistryFriendlyByteBuf buf, PingStructures obj) {
        buf.writeCollection(obj.structures, (pBuffer, pValue) -> StructurePing.STREAM_CODEC.encode((RegistryFriendlyByteBuf)pBuffer, pValue));
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static final Type<PingStructures> TYPE = new Type<>(DataNEssence.locate("ping_structures"));
    private static class ClientHandler {
        public static void addPings(List<StructurePing> pings) {
            for (StructurePing i : pings) {
                PingsGuiLayer.pings.put(i, 200);
            }
        }
    }
}