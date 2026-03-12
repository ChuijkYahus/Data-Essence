package com.cmdpro.datanessence.item;

import com.cmdpro.datanessence.registry.DataComponentRegistry;
import com.cmdpro.datanessence.registry.EssenceTypeRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class TagFilterLabel extends BaseFilterLabel {

    public TagFilterLabel(Properties properties) {
        super(properties);
    }

    @Override
    public boolean labelMatches(ItemStack filter, ItemStack compare) {
        String tag = filter.get(DataComponentRegistry.FILTER_STRING);

        if (tag != null && !tag.isEmpty()) {
            for (TagKey<Item> tagKey : compare.getTags().toList()) {
                if ( tag.equals(tagKey.location().toString()) )
                    return true;
            }
        }

        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {

        ItemStack filterItem = player.getItemInHand(hand);

        if ( player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() || player.getItemInHand(InteractionHand.OFF_HAND).isEmpty() ) {
            if (world.isClientSide)
                player.displayClientMessage(
                        Component.translatable("item.datanessence.tag_filter.instructions"), false);
            return InteractionResultHolder.fail(filterItem);
        }

        ItemStack candidateItem = player.getItemInHand(hand == InteractionHand.MAIN_HAND
                ? InteractionHand.OFF_HAND
                : InteractionHand.MAIN_HAND
        );

        List<String> tags = new ArrayList<>();
        for (TagKey<Item> tagKey : candidateItem.getTags().toList()) {
            tags.add(tagKey.location().toString());
        }

        // to cycle backwards whilst sneaking
        if (player.isShiftKeyDown())
            tags = tags.reversed();

        if (tags.isEmpty()) {
            if (world.isClientSide)
                player.displayClientMessage(
                        Component.translatable("item.datanessence.tag_filter.no_tags"), true);
            return InteractionResultHolder.fail(filterItem);
        }

        String currentSelection = filterItem.get(DataComponentRegistry.FILTER_STRING);
        if (currentSelection == null || !tags.contains(currentSelection)) {
            filterItem.set(DataComponentRegistry.FILTER_STRING, tags.getFirst());
            if (world.isClientSide)
                player.displayClientMessage(
                        Component.translatable("item.datanessence.tag_filter.selected", tags.getFirst()), true);
        } else {
            for ( int i = 0; i < tags.size(); i++ ) {
                if (tags.get(i).equals(currentSelection) ) {
                    String tag = tags.get( (i + 1) % tags.size() );
                    filterItem.set(DataComponentRegistry.FILTER_STRING, tag);
                    if (world.isClientSide)
                        player.displayClientMessage(
                                Component.translatable("item.datanessence.tag_filter.selected", tag), true);
                    break;
                }
            }
        }

        // TODO custom sound event
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundSource.PLAYERS);

        return InteractionResultHolder.success(filterItem);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        String tag = stack.get(DataComponentRegistry.FILTER_STRING);

        if ( tag == null || tag.isEmpty() ) {
            tooltip.add(
                    Component.translatable("item.datanessence.tag_filter.tooltip_empty")
                            .withStyle(Style.EMPTY).withColor(0xFFFF96B5)
            );
        } else {
            tooltip.add(
                    Component.translatable("item.datanessence.tag_filter.tooltip_set",
                            Component.literal(tag)
                                    .withStyle(Style.EMPTY).withColor(EssenceTypeRegistry.ESSENCE.get().getColor()))
                            .withStyle(Style.EMPTY).withColor(0xFFFF96B5)
            );
        }
    }
}
