package EsetKalenko.Halcyon.data.computers;

import EsetKalenko.Halcyon.Halcyon;
import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class ComputerTypeManager extends SimpleJsonResourceReloadListener {
    protected static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public static ComputerTypeManager instance;
    protected ComputerTypeManager() {
        super(GSON, "halcyon/computer_types");
    }
    public static ComputerTypeManager getOrCreateInstance() {
        if (instance == null) {
            instance = new ComputerTypeManager();
        }
        return instance;
    }
    public static Map<ResourceLocation, ComputerData> types;
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        types = new HashMap<>();
        Halcyon.LOGGER.info("[HALCYON] Adding Computer Types");
        for (Map.Entry<ResourceLocation, JsonElement> i : pObject.entrySet()) {
            ResourceLocation location = i.getKey();
            if (location.getPath().startsWith("_")) {
                continue;
            }

            try {
                JsonObject obj = i.getValue().getAsJsonObject();
                ComputerData data = serializer.read(i.getKey(), obj);
                types.put(i.getKey(), data);
                Halcyon.LOGGER.info("[HALCYON] Successfully added computer type {}", location);
            } catch (IllegalArgumentException | JsonParseException e) {
                Halcyon.LOGGER.error("[HALCYON] Parsing error loading computer type {}", location, e);
            }
        }
        Halcyon.LOGGER.info("[HALCYON] Loaded {} computer types", types.size());
    }
    public static ComputerTypeSerializer serializer = new ComputerTypeSerializer();
}
