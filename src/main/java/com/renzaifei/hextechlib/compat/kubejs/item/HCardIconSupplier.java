package com.renzaifei.hextechlib.compat.kubejs.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Supplier;

@FunctionalInterface
public interface HCardIconSupplier {
    HCardIconSupplier DEFAULT = () -> ItemStack.EMPTY;

    record Wrapper(HCardIconSupplier supplier) implements Supplier<ItemStack> {
        @Override
        public ItemStack get() {
            try {
                var i = supplier.getIcon();
                return i.isEmpty() ? Items.PAPER.getDefaultInstance() : i;
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return Items.PAPER.getDefaultInstance();
        }
    }

    ItemStack getIcon();
}
