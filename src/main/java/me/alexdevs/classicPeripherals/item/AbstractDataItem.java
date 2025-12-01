package me.alexdevs.classicPeripherals.item;

import me.alexdevs.classicPeripherals.ModComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public abstract class AbstractDataItem extends Item {
    public AbstractDataItem(Properties properties) {
        super(properties);
    }

    public static Optional<String> getData(ItemStack stack) {
        var data = stack.getComponents().get(ModComponents.NFC_DATA.get());
        if (data == null || data.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(data);
    }

    public static void setData(ItemStack stack, String data) {
        stack.set(ModComponents.NFC_DATA, data);
    }

    public static boolean isReadOnly(ItemStack stack) {
        return stack.getComponents().getOrDefault(ModComponents.NFC_READONLY.get(), false);
    }

    public static void setReadOnly(ItemStack stack, boolean readOnly) {
        stack.set(ModComponents.NFC_READONLY, readOnly);
    }

    public static void setLabel(ItemStack stack, String title) {
        stack.set(DataComponents.CUSTOM_NAME, Component.nullToEmpty(title));
    }

    public static void clearLabel(ItemStack stack) {
        stack.remove(DataComponents.CUSTOM_NAME);
    }

    public static void setColor(ItemStack stack, int color) {
        stack.set(ModComponents.NFC_COLOR, color & 0x00_FFFFFF);
    }

    public static int getColor(ItemStack stack) {
        return 0xFF_000000 | stack.getComponents().getOrDefault(ModComponents.NFC_COLOR.get(), 0xFFFFFF);
    }
}
