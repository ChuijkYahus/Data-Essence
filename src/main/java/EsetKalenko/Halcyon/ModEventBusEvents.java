package EsetKalenko.Halcyon;

import EsetKalenko.Halcyon.config.DataNEssenceClientConfig;
import EsetKalenko.Halcyon.config.DataNEssenceConfig;
import EsetKalenko.Halcyon.entity.AncientSentinel;
import EsetKalenko.Halcyon.registry.EntityRegistry;
import EsetKalenko.Halcyon.registry.ItemRegistry;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber(modid = DataNEssence.MOD_ID)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(EntityRegistry.ANCIENT_SENTINEL.get(), AncientSentinel.setAttributes());
    }
    @SubscribeEvent
    public static void onModConfigEvent(ModConfigEvent event) {
        ModConfig config = event.getConfig();
        if (config.getSpec() == DataNEssenceConfig.COMMON_SPEC) {
            DataNEssenceConfig.bake(config);
        }
        if (config.getSpec() == DataNEssenceClientConfig.CLIENT_SPEC) {
            DataNEssenceClientConfig.bake(config);
        }
    }
    @SubscribeEvent
    public static void entitySpawnRestriction(RegisterSpawnPlacementsEvent event) {

    }
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DispenserBlock.registerProjectileBehavior(ItemRegistry.ESSENCE_BOMB.get());
            DispenserBlock.registerProjectileBehavior(ItemRegistry.CRYSTALLIZATION_CHARGE.get());
            DispenserBlock.registerProjectileBehavior(ItemRegistry.NATURAL_ESSENCE_BOMB.get());
            DispenserBlock.registerProjectileBehavior(ItemRegistry.EXOTIC_ESSENCE_BOMB.get());
        });
    }
}
