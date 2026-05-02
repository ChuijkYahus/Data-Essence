package EsetKalenko.Halcyon.api.util.client;

import EsetKalenko.Halcyon.client.ClientModEvents;
import EsetKalenko.Halcyon.config.DataNEssenceClientConfig;
import EsetKalenko.Halcyon.data.datatablet.Entry;
import EsetKalenko.Halcyon.toasts.CriticalDataToast;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;

public class ClientProgressionUtil {
    public static void unlockedCriticalData(Entry entry) {
        Minecraft.getInstance().getToasts().addToast(new CriticalDataToast(entry));
        ClientProgressionUtil.progressionShader();
    }

    public static void progressionShader() {
        if (DataNEssenceClientConfig.progressionShader) {
            ClientModEvents.progressionShader.time = 0;
            ClientModEvents.progressionShader.setActive(true);
        }
    }

    public static void updateWorld() {
        for (SectionRenderDispatcher.RenderSection i : Minecraft.getInstance().levelRenderer.viewArea.sections) {
            i.setDirty(false);
        }
    }
}
