package EsetKalenko.Halcyon.registry;

import com.cmdpro.databank.DatabankRegistries;
import com.cmdpro.databank.hidden.HiddenCondition;
import EsetKalenko.Halcyon.Halcyon;
import EsetKalenko.Halcyon.data.hidden.EntryCondition;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class HiddenConditionRegistry {
    public static final DeferredRegister<HiddenCondition.Serializer<?>> HIDDEN_CONDITIONS = DeferredRegister.create(DatabankRegistries.HIDDEN_CONDITION_REGISTRY_KEY, Halcyon.MOD_ID);

    public static final Supplier<HiddenCondition.Serializer<?>> ENTRY = register("entry", () -> EntryCondition.EntryConditionSerializer.INSTANCE);
    private static <T extends HiddenCondition.Serializer<?>> Supplier<T> register(final String name, final Supplier<T> item) {
        return HIDDEN_CONDITIONS.register(name, item);
    }
}
