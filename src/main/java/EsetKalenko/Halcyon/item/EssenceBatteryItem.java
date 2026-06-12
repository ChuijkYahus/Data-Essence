package EsetKalenko.Halcyon.item;

import EsetKalenko.Halcyon.api.DataNEssenceRegistries;
import EsetKalenko.Halcyon.api.essence.EssenceType;
import EsetKalenko.Halcyon.api.item.ItemEssenceContainer;
import EsetKalenko.Halcyon.config.DataNEssenceConfig;
import EsetKalenko.Halcyon.registry.DataComponentRegistry;
import EsetKalenko.Halcyon.registry.EssenceTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class EssenceBatteryItem extends BlockItem {
    final ResourceLocation tide;

    public EssenceBatteryItem(Block block, Properties properties, ResourceLocation tide) {
        super(block, properties
                .component(DataComponentRegistry.ESSENCE_STORAGE.get(),
                        new ItemEssenceContainer(List.of(tide), 10000)));
        this.tide = tide;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

        @Override
    public int getBarWidth(ItemStack stack) {
        Float amount = ItemEssenceContainer.getEssence(stack, tide);
        return Math.round(13.0f * (amount / 10000));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return EssenceTypeRegistry.ESSENCE.get().getColor();
    }

//    @Override
//    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
//        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
//
//        var usedTide = DataNEssenceRegistries.ESSENCE_TYPE_REGISTRY.get(tide);
//        if (usedTide == null) return;
//
//        Float amount = ItemEssenceContainer.getEssence(stack, tide);
//        float max = DataNEssenceConfig.essenceBatteryMax;
//
//        tooltipComponents.add(Component.translatable(usedTide.tooltipKeyWithMax,
//                        (int)amount.floatValue(),
//                        (int)max)
//                .withStyle(Style.EMPTY.withColor(usedTide.getColor())));
//    }
}