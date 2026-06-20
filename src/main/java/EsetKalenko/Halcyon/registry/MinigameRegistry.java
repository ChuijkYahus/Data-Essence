package EsetKalenko.Halcyon.registry;

import EsetKalenko.Halcyon.Halcyon;
import EsetKalenko.Halcyon.api.DataNEssenceRegistries;
import EsetKalenko.Halcyon.data.minigames.*;
import EsetKalenko.Halcyon.api.databank.MinigameSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MinigameRegistry {
    public static final DeferredRegister<MinigameSerializer> MINIGAME_TYPES = DeferredRegister.create(DataNEssenceRegistries.MINIGAME_TYPE_REGISTRY_KEY, Halcyon.MOD_ID);

    public static final Supplier<MinigameSerializer> MINESWEEPER = register("minesweeper", () -> new MinesweeperMinigameCreator.MinesweeperMinigameSerializer());
    public static final Supplier<MinigameSerializer> WIRE = register("wire", () -> new WireMinigameCreator.WireMinigameSerializer());
    public static final Supplier<MinigameSerializer> TRACES = register("traces", () -> new TracesMinigameCreator.TracesMinigameSerializer());
    public static final Supplier<MinigameSerializer> COLOR_MIXING = register("color_mixing", () -> new ColorMixingMinigameCreator.ColorMixingMinigameSerializer());
    public static final Supplier<MinigameSerializer> LASER = register("laser", () -> new LaserMinigameCreator.LaserMinigameSerializer());
    private static <T extends MinigameSerializer> Supplier<T> register(final String name, final Supplier<T> item) {
        return MINIGAME_TYPES.register(name, item);
    }
}
