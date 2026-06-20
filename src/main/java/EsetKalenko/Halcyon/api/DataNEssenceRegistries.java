package EsetKalenko.Halcyon.api;

import EsetKalenko.Halcyon.Halcyon;
import EsetKalenko.Halcyon.api.essence.EssenceType;
import EsetKalenko.Halcyon.api.computer.ComputerFileType;
import EsetKalenko.Halcyon.api.databank.MinigameSerializer;
import EsetKalenko.Halcyon.api.datatablet.PageSerializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber(modid = Halcyon.MOD_ID)
public class DataNEssenceRegistries {
    public static ResourceKey<Registry<PageSerializer>> PAGE_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(Halcyon.locate("page_types"));
    public static ResourceKey<Registry<MinigameSerializer>> MINIGAME_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(Halcyon.locate("minigames"));
    public static ResourceKey<Registry<ComputerFileType>> COMPUTER_FILE_TYPES_REGISTRY_KEY = ResourceKey.createRegistryKey(Halcyon.locate("computer_file_types"));
    public static ResourceKey<Registry<EssenceType>> ESSENCE_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(Halcyon.locate("essence_types"));
    public static Registry<PageSerializer> PAGE_TYPE_REGISTRY = new RegistryBuilder<>(PAGE_TYPE_REGISTRY_KEY).sync(true).create();
    public static Registry<MinigameSerializer> MINIGAME_TYPE_REGISTRY = new RegistryBuilder<>(MINIGAME_TYPE_REGISTRY_KEY).sync(true).create();
    public static Registry<ComputerFileType> COMPUTER_FILE_TYPES_REGISTRY = new RegistryBuilder<>(COMPUTER_FILE_TYPES_REGISTRY_KEY).sync(true).create();
    public static Registry<EssenceType> ESSENCE_TYPE_REGISTRY = new RegistryBuilder<>(ESSENCE_TYPE_REGISTRY_KEY).sync(true).create();
    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(PAGE_TYPE_REGISTRY);
        event.register(MINIGAME_TYPE_REGISTRY);
        event.register(COMPUTER_FILE_TYPES_REGISTRY);
        event.register(ESSENCE_TYPE_REGISTRY);
    }
}
