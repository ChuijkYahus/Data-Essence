package EsetKalenko.Halcyon;

import EsetKalenko.Halcyon.commands.DataNEssenceCommands;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = DataNEssence.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ForgeModEvents {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        DataNEssenceCommands.register(event.getDispatcher());
    }
}
