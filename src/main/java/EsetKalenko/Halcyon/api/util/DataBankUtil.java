package EsetKalenko.Halcyon.api.util;

import EsetKalenko.Halcyon.networking.ModMessages;
import EsetKalenko.Halcyon.networking.packet.s2c.DataBankEntrySync;
import EsetKalenko.Halcyon.data.databank.DataBankEntries;
import EsetKalenko.Halcyon.data.databank.DataBankEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class DataBankUtil {
    public static void sendDataBankEntries(ServerPlayer player, ResourceLocation[] ids) {
        Map<ResourceLocation, DataBankEntry> entries = new HashMap<>();
        for (ResourceLocation i : ids) {
            DataBankEntry entry = DataBankEntries.entries.get(i);
            if (entry != null) {
                entries.put(i, entry);
            }
        }
        ModMessages.sendToPlayer(new DataBankEntrySync(entries), (player));
    }

    public static void sendDataBankEntries(Player player, ResourceLocation[] ids) {
        DataBankUtil.sendDataBankEntries((ServerPlayer) player, ids);
    }
}
