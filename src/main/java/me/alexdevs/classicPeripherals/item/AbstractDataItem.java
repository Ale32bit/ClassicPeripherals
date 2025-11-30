package me.alexdevs.classicPeripherals.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public abstract class AbstractDataItem extends Item {
    public AbstractDataItem(Properties properties) {
        super(properties);
    }

    public static Optional<String> getData(ItemStack stack) {
        var tag = stack.getOrCreateTag();
        var data = tag.getString("data");
        if (data.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(data);
    }

    public static void setData(ItemStack stack, String data) {
        var tag = stack.getOrCreateTag();
        tag.putString("data", data);
    }

    public static boolean isReadOnly(ItemStack stack) {
        var tag = stack.getOrCreateTag();
        return tag.getBoolean("readOnly");
    }

    public static void setReadOnly(ItemStack stack, boolean readOnly) {
        var tag = stack.getOrCreateTag();
        tag.putBoolean("readOnly", readOnly);
    }

    public static void setLabel(ItemStack stack, String title) {
        stack.setHoverName(Component.nullToEmpty(title));
    }

    public static void clearLabel(ItemStack stack) {
        stack.resetHoverName();
    }
}
