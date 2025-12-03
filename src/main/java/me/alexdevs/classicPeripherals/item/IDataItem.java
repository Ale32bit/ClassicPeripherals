package me.alexdevs.classicPeripherals.item;

import me.alexdevs.classicPeripherals.ModComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface IDataItem {
    static Optional<String> getData(ItemStack stack) {
        var data = stack.getComponents().get(ModComponents.NFC_DATA);
        if (data == null || data.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(data);
    }

    static void setData(ItemStack stack, String data) {
        stack.set(ModComponents.NFC_DATA, data);
    }

    static boolean isReadOnly(ItemStack stack) {
        return stack.getComponents().getOrDefault(ModComponents.NFC_READONLY, false);
    }

    static void setReadOnly(ItemStack stack, boolean readOnly) {
        stack.set(ModComponents.NFC_READONLY, readOnly);
    }

    static void setLabel(ItemStack stack, String title) {
        stack.set(DataComponents.CUSTOM_NAME, Component.nullToEmpty(title));
    }

    static void clearLabel(ItemStack stack) {
        stack.remove(DataComponents.CUSTOM_NAME);
    }

    static void setColor(ItemStack stack, int color) {
        stack.set(ModComponents.NFC_COLOR, color & 0x00_FFFFFF);
    }

    static int getColor(ItemStack stack) {
        return 0xFF_000000 | stack.getComponents().getOrDefault(ModComponents.NFC_COLOR, 0xFFFFFF);
    }
}
