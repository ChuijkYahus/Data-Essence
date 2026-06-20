package EsetKalenko.Halcyon.networking.packet.s2c;


import EsetKalenko.Halcyon.Halcyon;
import EsetKalenko.Halcyon.networking.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.Map;

public record AddScannedOre(HashMap<BlockPos, Integer> ores) implements Message {

    public static HashMap<BlockPos, Integer> scanned = new HashMap<>();

    public static final Type<AddScannedOre> TYPE = new Type<>(Halcyon.locate("add_scanned_ore"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void write(RegistryFriendlyByteBuf buf, AddScannedOre obj) {
        buf.writeMap(obj.ores, RegistryFriendlyByteBuf::writeBlockPos, FriendlyByteBuf::writeInt);
    }

    public static AddScannedOre read(RegistryFriendlyByteBuf buf) {
        HashMap<BlockPos, Integer> ores = new HashMap<>(buf.readMap(RegistryFriendlyByteBuf::readBlockPos, FriendlyByteBuf::readInt));
        return new AddScannedOre(ores);
    }

    @Override
    public void handleClient(Minecraft minecraft, Player player, IPayloadContext ctx) {
        for (Map.Entry<BlockPos, Integer> i : ores.entrySet()) {
            if (!scanned.containsKey(i.getKey()) || scanned.get(i.getKey()) > i.getValue()) {
                scanned.put(i.getKey(), i.getValue());
            }
        }
    }
}