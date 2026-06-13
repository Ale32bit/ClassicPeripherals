package me.alexdevs.classicPeripherals.peripherals.scanner.screen;

import me.alexdevs.classicPeripherals.ModRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class ScannerMenu extends AbstractContainerMenu {
    private final Container inventory;

    public ScannerMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(1));
    }

    public ScannerMenu(int syncId, Inventory playerInventory, Container inventory) {
        super(ModRegistry.Screens.SCANNER.get(), syncId);
        this.inventory = inventory;

        checkContainerSize(inventory, 1);

        // printout slot
        this.addSlot(new PrintoutSlot(inventory, 0, 80, 35));

        // player inventory
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        // player hotbar
        for (int y = 0; y < 9; y++) {
            this.addSlot(new Slot(playerInventory, y, 8 + y * 18, 142));
        }
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();

            if (index == 0) {
                if (!this.moveItemStackTo(originalStack, 1, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {

                if (!this.moveItemStackTo(originalStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return newStack;
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return this.inventory.stillValid(player);
    }
}
