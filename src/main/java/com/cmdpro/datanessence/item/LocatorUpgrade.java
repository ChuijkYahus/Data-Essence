package com.cmdpro.datanessence.item;

import com.cmdpro.datanessence.registry.DataComponentRegistry;
import com.cmdpro.datanessence.registry.EssenceTypeRegistry;
import com.cmdpro.datanessence.registry.ItemRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class LocatorUpgrade extends Item {

    public LocatorUpgrade(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {

        if ( !world.isClientSide ) {

            var offhandItem = player.getItemInHand(InteractionHand.OFF_HAND);

            if ( ItemStack.isSameItem(offhandItem, new ItemStack(ItemRegistry.LOCATOR.get())) ) {
                offhandItem.set(DataComponentRegistry.SIGNAL_TRACKER_UPGRADED, true);
                player.getItemInHand(hand).shrink(1);
                player.sendSystemMessage( Component.translatable("item.datanessence.signal_tracker_resonance_receptor.install")
                        .withStyle(Style.EMPTY
                                .withItalic(true)
                                .withColor(0x7904c7)));
            }

        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        tooltipComponents.add(Component.translatable("item.datanessence.signal_tracker_resonance_receptor.tooltip")
                .withStyle(Style.EMPTY.withColor(EssenceTypeRegistry.ESSENCE.get().getColor())));
    }
}
