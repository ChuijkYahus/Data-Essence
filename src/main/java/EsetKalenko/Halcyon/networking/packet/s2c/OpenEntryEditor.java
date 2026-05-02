package EsetKalenko.Halcyon.networking.packet.s2c;

import EsetKalenko.Halcyon.DataNEssence;
import EsetKalenko.Halcyon.networking.Message;
import EsetKalenko.Halcyon.screen.dev.DataTabletEditorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OpenEntryEditor() implements Message {
    public static OpenEntryEditor read(FriendlyByteBuf buf) {
        return new OpenEntryEditor();
    }

    public static void write(RegistryFriendlyByteBuf buf, OpenEntryEditor obj) {

    }
    @Override
    public void handleClient(Minecraft minecraft, Player player, IPayloadContext ctx) {
        ClientHandler.handle(this);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static final Type<OpenEntryEditor> TYPE = new Type<>(DataNEssence.locate("open_entry_editor"));

    private static class ClientHandler {
        public static void handle(OpenEntryEditor packet) {
            Minecraft.getInstance().setScreen(new DataTabletEditorScreen(Component.empty()));
        }
    }
}