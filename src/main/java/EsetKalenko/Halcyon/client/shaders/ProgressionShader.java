package EsetKalenko.Halcyon.client.shaders;

import com.cmdpro.databank.shaders.PostShaderInstance;
import EsetKalenko.Halcyon.DataNEssence;
import net.minecraft.resources.ResourceLocation;

public class ProgressionShader extends PostShaderInstance {
    @Override
    public ResourceLocation getShaderLocation() {
        return DataNEssence.locate("shaders/post/progression.json");
    }

    @Override
    public void afterProcess() {
        if (time >= 15f) {
            setActive(false);
        }
    }
}
