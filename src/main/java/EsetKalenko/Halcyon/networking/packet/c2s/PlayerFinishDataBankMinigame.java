package EsetKalenko.Halcyon.networking.packet.c2s;

import EsetKalenko.Halcyon.DataNEssence;
import EsetKalenko.Halcyon.api.util.DataTabletUtil;
import EsetKalenko.Halcyon.networking.Message;
import EsetKalenko.Halcyon.registry.AttachmentTypeRegistry;
import EsetKalenko.Halcyon.data.databank.DataBankEntries;
import EsetKalenko.Halcyon.data.databank.DataBankEntry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PlayerFinishDataBankMinigame(ResourceLocation entry) implements Message {

    public static PlayerFinishDataBankMinigame read(RegistryFriendlyByteBuf buf) {
        ResourceLocation entry = buf.readResourceLocation();
        return new PlayerFinishDataBankMinigame(entry);
    }

    @Override
    public void handleServer(MinecraftServer server, ServerPlayer player, IPayloadContext ctx) {
        DataBankEntry entry2 = DataBankEntries.entries.get(entry);
        if (entry2 != null) {
            if (entry2.tier <= player.getData(AttachmentTypeRegistry.TIER)) {
                DataTabletUtil.unlockEntry(player, entry2.entry, 0);
            }
        }
    }

    public static void write(RegistryFriendlyByteBuf buf, PlayerFinishDataBankMinigame obj) {
        buf.writeResourceLocation(obj.entry);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static final Type<PlayerFinishDataBankMinigame> TYPE = new Type<>(DataNEssence.locate("player_finish_data_bank_minigame"));
}