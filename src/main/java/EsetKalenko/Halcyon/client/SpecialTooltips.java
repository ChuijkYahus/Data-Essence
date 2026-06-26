package EsetKalenko.Halcyon.client;

import EsetKalenko.Halcyon.Halcyon;
import EsetKalenko.Halcyon.api.DataNEssenceRegistries;
import EsetKalenko.Halcyon.api.essence.EssenceType;
import EsetKalenko.Halcyon.datamaps.HalcyonDatamaps;
import EsetKalenko.Halcyon.datamaps.PlantSiphonEssenceMap;
import EsetKalenko.Halcyon.registry.EssenceTypeRegistry;
import EsetKalenko.Halcyon.registry.HalcyonTags;
import EsetKalenko.Halcyon.screen.EssenceBurnerScreen;
import EsetKalenko.Halcyon.screen.IndustrialPlantSiphonScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@EventBusSubscriber(value = Dist.CLIENT, modid = Halcyon.MOD_ID)
public class SpecialTooltips {
    private static final HashMap<Function<ItemTooltipEvent, Boolean>, Consumer<ItemTooltipEvent>> TOOLTIPS = new HashMap<>();

    public static final Function<ItemTooltipEvent, Boolean> PLANT_SIPHON_CONDITION = (event) ->
            event.getItemStack().getItemHolder().getData(HalcyonDatamaps.PLANT_SIPHON_ESSENCE) != null
            && Minecraft.getInstance().screen instanceof IndustrialPlantSiphonScreen;

    public static final Function<ItemTooltipEvent, Boolean> SHARD_BURNER_CONDITION = (event) ->
            event.getItemStack().getItemHolder().getData(HalcyonDatamaps.SHARD_SUBLIMATION) != null
                    && Minecraft.getInstance().screen instanceof EssenceBurnerScreen;

    public static final Function<ItemTooltipEvent, Boolean> WIP_CONDITION = (event) ->
            event.getItemStack().is(HalcyonTags.Items.WIP);

    static {
        TOOLTIPS.put(PLANT_SIPHON_CONDITION, (event) -> {
            PlantSiphonEssenceMap map = event.getItemStack().getItemHolder().getData(HalcyonDatamaps.PLANT_SIPHON_ESSENCE);
            if (map != null) {
                EssenceType essence = EssenceTypeRegistry.ESSENCE.get();
                Color color = new Color(essence.color);
                Color essenceColor = new Color((int)(color.getRed()), (int)(color.getGreen()), (int)(color.getBlue()), color.getAlpha());
                Color numberColor = new Color((int)(color.getRed()), (int)(color.getGreen()), (int)(color.getBlue()), color.getAlpha());
                Color textColor = new Color(0xFFFF96B5); // 0xFFF25EA1
                DecimalFormat format = new DecimalFormat("#.##");
                event.getToolTip().add(Component.translatable("tooltip.halcyon.plant_siphon_display.text").withStyle(ChatFormatting.ITALIC).withColor(0xFFF25EA1));
                event.getToolTip().add(Component.translatable("tooltip.halcyon.plant_siphon_display.values", Component.literal(format.format(map.amountPerTick())).withColor(numberColor.getRGB()), Component.literal(String.valueOf(map.ticks())).withColor(numberColor.getRGB()), essence.name.copy().withColor(essenceColor.getRGB())).withColor(textColor.getRGB()));
            }
        });

        TOOLTIPS.put(SHARD_BURNER_CONDITION, (event) -> {
            var sublimationData = event.getItemStack().getItemHolder().getData(HalcyonDatamaps.SHARD_SUBLIMATION);
            if (sublimationData != null) {
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                Color textColor = new Color(0xFFFF96B5);

                event.getToolTip().add(
                        Component.translatable("tooltip.halcyon.shard_burner_display.text")
                                .withStyle(ChatFormatting.ITALIC).withColor(0xFFF25EA1));

                for (Map.Entry<ResourceLocation, Float> energy : sublimationData.energyContained().entrySet()) {
                    var tideKey = DataNEssenceRegistries.ESSENCE_TYPE_REGISTRY.get(energy.getKey());
                    if ( tideKey == null )
                        return;

                    event.getToolTip().add(
                            Component.translatable("tooltip.halcyon.shard_burner_display.values",
                                    Component.literal(decimalFormat.format(energy.getValue()))
                                            .withColor(tideKey.color),
                                    tideKey.name.copy()
                                            .withColor(tideKey.color))
                                            .withColor(textColor.getRGB()));
                }
            }
        });

        TOOLTIPS.put( WIP_CONDITION, (event) -> {
            event.getToolTip().add(
                    Component.translatable("tooltip.halcyon.wip_1")
                            .withColor(0xE83A27) // maybe make this flash between red and white every 2 seconds
            );
            event.getToolTip().add(
                    Component.translatable("tooltip.halcyon.wip_2")
                            .withStyle(ChatFormatting.ITALIC)
                            .withColor(0xFFEA99)
            );
        });
    }



    @SubscribeEvent
    public static void onTooltipEvent(ItemTooltipEvent event) {
        for (var i : TOOLTIPS.entrySet()) {
            if (i.getKey().apply(event)) {
                i.getValue().accept(event);
            }
        }
    }
}
