package EsetKalenko.Halcyon.datagen;

import EsetKalenko.Halcyon.DataNEssence;
import EsetKalenko.Halcyon.datagen.loot.HalcyonLootTables;
import EsetKalenko.Halcyon.datagen.model.HalcyonBlockModels;
import EsetKalenko.Halcyon.datagen.model.HalcyonItemModels;
import EsetKalenko.Halcyon.datagen.recipe.HalcyonRecipeProvider;
import EsetKalenko.Halcyon.datagen.datamap.PlantSiphonEssenceProvider;
import EsetKalenko.Halcyon.datagen.tag.HalcyonBlockTags;
import EsetKalenko.Halcyon.datagen.tag.HalcyonEntityTags;
import EsetKalenko.Halcyon.datagen.tag.HalcyonItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = DataNEssence.MOD_ID)
public class HalcyonDataGeneration {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), HalcyonLootTables.create(packOutput, event.getLookupProvider()));

        generator.addProvider(event.includeClient(), new HalcyonBlockModels(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new HalcyonItemModels(packOutput, existingFileHelper));

        generator.addProvider(event.includeServer(), new PlantSiphonEssenceProvider(packOutput, lookupProvider));

        HalcyonBlockTags blockTagGenerator = generator.addProvider(event.includeServer(),
                new HalcyonBlockTags(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new HalcyonItemTags(packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new HalcyonEntityTags(packOutput, lookupProvider, existingFileHelper));

        // Recipes
        generator.addProvider(event.includeServer(), new HalcyonRecipeProvider(packOutput, lookupProvider));
    }
}