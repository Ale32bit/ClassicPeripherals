package me.alexdevs.classicPeripherals.screen;

import dan200.computercraft.shared.media.items.PrintoutItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PrintoutSlot extends Slot {
        public PrintoutSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        public boolean mayPlace(ItemStack stack) {
            return isPrintout(stack);
        }

        public static boolean isPrintout(ItemStack stack) {
            return stack.getItem() instanceof PrintoutItem;
        }
}
