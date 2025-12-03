package me.alexdevs.classicPeripherals.item;

import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface IDataItem {
    static Optional<String> getData(ItemStack stack) {
        var tag = stack.getOrCreateTag();
        var data = tag.getString("data");
        if (data.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(data);
    }

    static void setData(ItemStack stack, String data) {
        var tag = stack.getOrCreateTag();
        tag.putString("data", data);
    }

    static boolean isReadOnly(ItemStack stack) {
        var tag = stack.getOrCreateTag();
        return tag.getBoolean("readOnly");
    }

    static void setReadOnly(ItemStack stack, boolean readOnly) {
        var tag = stack.getOrCreateTag();
        tag.putBoolean("readOnly", readOnly);
    }

    static void setLabel(ItemStack stack, String title) {
        stack.setHoverName(Component.nullToEmpty(title));
    }

    static void clearLabel(ItemStack stack) {
        stack.resetHoverName();
    }

    static void setColor(ItemStack stack, int color) {
        var tag = stack.getOrCreateTag();
        tag.putInt("color", color);
    }

    static int getColor(ItemStack stack) {
        var tag = stack.getOrCreateTag();
        if (tag.contains("color", Tag.TAG_INT)) {
            return tag.getInt("color");
        }

        return 0xFFFFFF;
    }
}
