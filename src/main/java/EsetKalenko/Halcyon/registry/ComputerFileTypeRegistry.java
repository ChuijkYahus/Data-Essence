package EsetKalenko.Halcyon.registry;

import EsetKalenko.Halcyon.DataNEssence;
import EsetKalenko.Halcyon.api.DataNEssenceRegistries;
import EsetKalenko.Halcyon.api.computer.ComputerFileType;
import EsetKalenko.Halcyon.data.computers.files.types.TextFileType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ComputerFileTypeRegistry {
    public static final DeferredRegister<ComputerFileType> COMPUTER_FILE_TYPES = DeferredRegister.create(DataNEssenceRegistries.COMPUTER_FILE_TYPES_REGISTRY_KEY, DataNEssence.MOD_ID);

    public static final Supplier<ComputerFileType> TEXT = register("text", () -> new TextFileType());
    private static <T extends ComputerFileType> Supplier<T> register(final String name, final Supplier<T> item) {
        return COMPUTER_FILE_TYPES.register(name, item);
    }
}
