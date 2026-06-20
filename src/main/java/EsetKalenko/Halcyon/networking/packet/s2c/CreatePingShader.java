package EsetKalenko.Halcyon.networking.packet.s2c;

import EsetKalenko.Halcyon.Halcyon;
import EsetKalenko.Halcyon.client.shaders.PingShader;
import com.cmdpro.databank.shaders.PostShaderInstance;
import com.cmdpro.databank.shaders.PostShaderManager;
import EsetKalenko.Halcyon.config.DataNEssenceClientConfig;
import EsetKalenko.Halcyon.networking.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CreatePingShader(Vec3 position) implements Message {
    public static CreatePingShader read(RegistryFriendlyByteBuf buf) {
        Vec3 position = buf.readVec3();
        return new CreatePingShader(position);
    }

    @Override
    public void handleClient(Minecraft minecraft, Player player, IPayloadContext ctx) {
        ClientHandler.startShader(position);
    }

    public static void write(RegistryFriendlyByteBuf buf, CreatePingShader obj) {
        buf.writeVec3(obj.position);
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static final Type<CreatePingShader> TYPE = new Type<>(Halcyon.locate("create_ping_shader"));
    private static class ClientHandler {
        public static void startShader(Vec3 position) {
            if (DataNEssenceClientConfig.pingShader) {
                PostShaderInstance shader = new PingShader(position.toVector3f());
                shader.setActive(true);
                PostShaderManager.addShader(shader);
            }
        }
    }
}