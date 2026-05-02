package EsetKalenko.Halcyon.registry;

import EsetKalenko.Halcyon.DataNEssence;
import EsetKalenko.Halcyon.api.DataNEssenceRegistries;
import EsetKalenko.Halcyon.api.datatablet.PageSerializer;
import EsetKalenko.Halcyon.data.datatablet.pages.serializers.CraftingPageSerializer;
import EsetKalenko.Halcyon.data.datatablet.pages.serializers.ItemPageSerializer;
import EsetKalenko.Halcyon.data.datatablet.pages.serializers.MultiblockPageSerializer;
import EsetKalenko.Halcyon.data.datatablet.pages.serializers.TextPageSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PageTypeRegistry {
    public static final DeferredRegister<PageSerializer> PAGE_TYPES = DeferredRegister.create(DataNEssenceRegistries.PAGE_TYPE_REGISTRY_KEY, DataNEssence.MOD_ID);

    public static final Supplier<CraftingPageSerializer> CRAFTINGPAGE = register("crafting", () -> CraftingPageSerializer.INSTANCE);
    public static final Supplier<TextPageSerializer> TEXTPAGE = register("text", () -> TextPageSerializer.INSTANCE);
    public static final Supplier<ItemPageSerializer> ITEMPAGE = register("item", () -> ItemPageSerializer.INSTANCE);
    public static final Supplier<MultiblockPageSerializer> MULTIBLOCK_PAGE = register("multiblock", () -> MultiblockPageSerializer.INSTANCE);
    private static <T extends PageSerializer> Supplier<T> register(final String name, final Supplier<T> item) {
        return PAGE_TYPES.register(name, item);
    }
}
