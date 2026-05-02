package EsetKalenko.Halcyon.networking.packet.s2c;

import EsetKalenko.Halcyon.DataNEssence;
import EsetKalenko.Halcyon.networking.Message;
import EsetKalenko.Halcyon.registry.AttachmentTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record DragonPartsSync(int id, boolean horns, boolean tail, boolean wings) implements Message {
    @Override
    public void handleClient(Minecraft minecraft, Player player, IPayloadContext ctx) {
        if (player.level().getEntity(id) instanceof Player target) {
            target.setData(AttachmentTypeRegistry.HAS_HORNS, horns);
            target.setData(AttachmentTypeRegistry.HAS_TAIL, tail);
            target.setData(AttachmentTypeRegistry.HAS_WINGS, wings);
        }
    }

    public static void write(RegistryFriendlyByteBuf pBuffer, DragonPartsSync obj) {
        pBuffer.writeInt(obj.id);
        pBuffer.writeBoolean(obj.horns);
        pBuffer.writeBoolean(obj.tail);
        pBuffer.writeBoolean(obj.wings);
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static final Type<DragonPartsSync> TYPE = new Type<>(DataNEssence.locate("dragon_parts_sync"));

    public static DragonPartsSync read(RegistryFriendlyByteBuf buf) {
        int id = buf.readInt();
        boolean horns = buf.readBoolean();
        boolean tail = buf.readBoolean();
        boolean wings = buf.readBoolean();
        return new DragonPartsSync(id, horns, tail, wings);
    }

}