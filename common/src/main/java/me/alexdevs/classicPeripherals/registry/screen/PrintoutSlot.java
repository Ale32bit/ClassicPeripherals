package me.alexdevs.classicPeripherals.registry.screen;

import dan200.computercraft.shared.media.items.PrintoutItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class PrintoutSlot extends Slot {
        public PrintoutSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        public boolean mayPlace(@NonNull ItemStack stack) {
            return isPrintout(stack);
        }

        public static boolean isPrintout(ItemStack stack) {
            return stack.getItem() instanceof PrintoutItem;
        }
}
