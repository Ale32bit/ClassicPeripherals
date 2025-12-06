package me.alexdevs.classicPeripherals.item;

import me.alexdevs.classicPeripherals.ModComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface IDataItem {
    static Optional<String> getData(ItemStack stack) {
        tryMigrate(stack);

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

    static boolean tryMigrate(ItemStack stack) {
        if (stack.has(ModComponents.NFC_DATA)) {
            return false;
        }

        var legacyData = DataItemData.migrate(stack);
        if (legacyData.isEmpty()) {
            return false;
        }

        setData(stack, legacyData.get().data());
        setReadOnly(stack, legacyData.get().readonly());
        setColor(stack, legacyData.get().color());

        return true;
    }

    record DataItemData(String data, boolean readonly, int color) {
        public static Optional<DataItemData> migrate(ItemStack stack) {
            if (!(stack.getItem() instanceof IDataItem)) {
                return Optional.empty();
            }

            if (!stack.has(DataComponents.CUSTOM_DATA)) {
                return Optional.empty();
            }

            var customData = stack.get(DataComponents.CUSTOM_DATA);
            if (customData == null || customData.isEmpty()) {
                return Optional.empty();
            }

            String data;
            boolean readonly = false;
            int color = 0xFFFFFF;

            var tag = customData.copyTag();
            if (!tag.contains("data", Tag.TAG_STRING)) {
                return Optional.empty();
            }

            data = tag.getString("data");

            if (tag.contains("readOnly", Tag.TAG_BYTE)) {
                readonly = tag.getBoolean("readOnly");
            }

            if (tag.contains("color", Tag.TAG_INT)) {
                color = tag.getInt("color");
            }

            return Optional.of(new DataItemData(data, readonly, color));
        }
    }
}
