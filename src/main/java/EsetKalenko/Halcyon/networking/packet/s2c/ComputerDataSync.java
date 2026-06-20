package EsetKalenko.Halcyon.networking.packet.s2c;

import EsetKalenko.Halcyon.Halcyon;
import EsetKalenko.Halcyon.data.computers.ClientComputerData;
import EsetKalenko.Halcyon.data.computers.ComputerData;
import EsetKalenko.Halcyon.data.computers.ComputerTypeSerializer;
import EsetKalenko.Halcyon.networking.Message;
import EsetKalenko.Halcyon.screen.ComputerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ComputerDataSync(ComputerData data) implements Message {
    public static ComputerDataSync read(RegistryFriendlyByteBuf buf) {
        ComputerData data = ComputerTypeSerializer.STREAM_CODEC.decode(buf);
        return new ComputerDataSync(data);
    }

    @Override
    public void handleClient(Minecraft minecraft, Player player, IPayloadContext ctx) {
        ClientComputerData.clientComputerData = data;
        ClientHandler.openScreen();
    }

    public static void write(RegistryFriendlyByteBuf buf, ComputerDataSync obj) {
        ComputerTypeSerializer.STREAM_CODEC.encode(buf, obj.data);
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static final Type<ComputerDataSync> TYPE = new Type<>(Halcyon.locate("computer_data_sync"));
    private static class ClientHandler {
        public static void openScreen() {
            Minecraft.getInstance().setScreen(new ComputerScreen(Component.empty()));
        }
    }
}