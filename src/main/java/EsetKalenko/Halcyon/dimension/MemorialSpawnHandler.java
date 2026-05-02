package EsetKalenko.Halcyon.dimension;

import com.cmdpro.databank.megastructures.Megastructure;
import com.cmdpro.databank.megastructures.MegastructureManager;
import EsetKalenko.Halcyon.DataNEssence;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@EventBusSubscriber(modid = DataNEssence.MOD_ID)
public class MemorialSpawnHandler {
    public static final ResourceKey<Level> MEMORIAL = ResourceKey.create(Registries.DIMENSION, DataNEssence.locate("memorial"));
    public static boolean doesMemorialExist;
    @SubscribeEvent
    public static void onServerStart(ServerStartedEvent event) {
        ServerLevel level = event.getServer().getLevel(MEMORIAL);
        Megastructure megastructure = MegastructureManager.megastructures.get(DataNEssence.locate("memorial"));
        if (megastructure != null) {
            megastructure.placeIntoWorld(level, new BlockPos(0, 128, 0));
        }
        doesMemorialExist = megastructure != null;
    }
}
