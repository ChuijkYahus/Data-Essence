package EsetKalenko.Halcyon.client.shaders;

import EsetKalenko.Halcyon.Halcyon;
import com.cmdpro.databank.shaders.PostShaderInstance;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

public class PingShader extends PostShaderInstance {
    @Override
    public ResourceLocation getShaderLocation() {
        return Halcyon.locate("shaders/post/ping.json");
    }
    public Vector3f pingPosition;
    public PingShader(Vector3f pingPosition) {
        this.pingPosition = pingPosition;
    }

    @Override
    public void setUniforms(PostPass instance) {
        super.setUniforms(instance);
        instance.getEffect().safeGetUniform("PingPosition").set(pingPosition);
    }

    @Override
    public void beforeProcess() {
        super.beforeProcess();
        if (time >= 5) {
            queueRemoval();
        }
    }
}
