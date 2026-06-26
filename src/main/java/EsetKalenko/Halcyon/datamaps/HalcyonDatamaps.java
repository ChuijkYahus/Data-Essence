package EsetKalenko.Halcyon.datamaps;

import EsetKalenko.Halcyon.Halcyon;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = Halcyon.MOD_ID)
public class HalcyonDatamaps {

    public static final DataMapType<Item, PlantSiphonEssenceMap> PLANT_SIPHON_ESSENCE = DataMapType.builder(
            Halcyon.locate("plant_siphon_essence"),
            Registries.ITEM,
            PlantSiphonEssenceMap.CODEC
    ).synced(PlantSiphonEssenceMap.CODEC, true).build();

    public static final DataMapType<Item, ShardSublimationMap> SHARD_SUBLIMATION = DataMapType.builder(
            Halcyon.locate("shard_sublimation"),
            Registries.ITEM,
            ShardSublimationMap.CODEC
    ).synced(ShardSublimationMap.CODEC, true).build();

    @SubscribeEvent
    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(PLANT_SIPHON_ESSENCE);
        event.register(SHARD_SUBLIMATION);
    }
}
