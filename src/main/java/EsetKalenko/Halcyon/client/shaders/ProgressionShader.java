package EsetKalenko.Halcyon.client.shaders;

import EsetKalenko.Halcyon.Halcyon;
import com.cmdpro.databank.shaders.PostShaderInstance;
import net.minecraft.resources.ResourceLocation;

public class ProgressionShader extends PostShaderInstance {
    @Override
    public ResourceLocation getShaderLocation() {
        return Halcyon.locate("shaders/post/progression.json");
    }

    @Override
    public void afterProcess() {
        if (time >= 15f) {
            setActive(false);
        }
    }
}
