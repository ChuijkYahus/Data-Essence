package com.cmdpro.datanessence.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class BaseFilterLabel extends Item {

    public BaseFilterLabel(Properties properties) {
        super(properties);
    }

    public abstract boolean labelMatches(ItemStack tag, ItemStack compare);
}
