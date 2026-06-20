package EsetKalenko.Halcyon.data.pinging;

import EsetKalenko.Halcyon.Halcyon;
import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class PingableStructureManager extends SimpleJsonResourceReloadListener {
    protected static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    public static PingableStructureManager instance;

    protected PingableStructureManager() {
        super(GSON, "halcyon/pingable_structures");
    }

    public static PingableStructureManager getOrCreateInstance() {
        if (instance == null) {
            instance = new PingableStructureManager();
        }
        return instance;
    }

    public static Map<ResourceLocation, PingableStructure> types = new HashMap<>();

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        types = new HashMap<>();
        Halcyon.LOGGER.info("[HALCYON] Adding Pingable Structures");
        for (Map.Entry<ResourceLocation, JsonElement> i : pObject.entrySet()) {
            ResourceLocation location = i.getKey();
            if (location.getPath().startsWith("_")) {
                continue;
            }

            try {
                JsonObject obj = i.getValue().getAsJsonObject();
                PingableStructure data = serializer.read(i.getKey(), obj);
                types.put(i.getKey(), data);
                Halcyon.LOGGER.info("[HALCYON] Successfully added pingable structure {}", location);
            } catch (IllegalArgumentException | JsonParseException e) {
                Halcyon.LOGGER.error("[HALCYON ERROR] Parsing error loading pingable structure {}", location, e);
            }
        }
        Halcyon.LOGGER.info("[HALCYON] Loaded {} pingable structures", types.size());
    }

    public static PingableStructureSerializer serializer = new PingableStructureSerializer();
}
