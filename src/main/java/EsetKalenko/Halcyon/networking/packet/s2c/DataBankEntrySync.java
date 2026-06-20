package EsetKalenko.Halcyon.networking.packet.s2c;

import EsetKalenko.Halcyon.Halcyon;
import EsetKalenko.Halcyon.networking.Message;
import EsetKalenko.Halcyon.screen.DataBankScreen;
import EsetKalenko.Halcyon.data.databank.DataBankEntries;
import EsetKalenko.Halcyon.data.databank.DataBankEntry;
import EsetKalenko.Halcyon.data.databank.DataBankEntrySerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Map;

public record DataBankEntrySync(Map<ResourceLocation, DataBankEntry> entries) implements Message {
    public static DataBankEntrySync read(RegistryFriendlyByteBuf buf) {
        Map<ResourceLocation, DataBankEntry> entries = buf.readMap(FriendlyByteBuf::readResourceLocation, (pBuffer) -> DataBankEntrySerializer.STREAM_CODEC.decode((RegistryFriendlyByteBuf)pBuffer));
        return  new DataBankEntrySync(entries);
    }

    @Override
    public void handleClient(Minecraft minecraft, Player player, IPayloadContext ctx) {
        DataBankEntries.clientEntries.clear();
        DataBankEntries.clientEntries.putAll(entries);
        ClientHandler.openScreen();
    }

    public static void write(RegistryFriendlyByteBuf buf, DataBankEntrySync obj) {
        buf.writeMap(obj.entries, ResourceLocation.STREAM_CODEC, (pBuffer, pValue) -> DataBankEntrySerializer.STREAM_CODEC.encode((RegistryFriendlyByteBuf)pBuffer, pValue));
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static final Type<DataBankEntrySync> TYPE = new Type<>(Halcyon.locate("data_bank_entry_sync"));
    private static class ClientHandler {
        public static void openScreen() {
            Minecraft.getInstance().setScreen(new DataBankScreen(Component.empty()));
        }
    }
}