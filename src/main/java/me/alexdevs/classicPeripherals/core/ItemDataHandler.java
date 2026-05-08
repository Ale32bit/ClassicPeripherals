package me.alexdevs.classicPeripherals.core;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.ModComponents;
import me.alexdevs.classicPeripherals.item.IDataItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ItemDataHandler {
    public static Optional<String> getData(ItemStack stack) {
        tryMigrate(stack);

        var data = stack.getComponents().get(ModComponents.NFC_DATA);
        if (data == null || data.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(data);
    }

    public static void setData(ItemStack stack, String data) {
        stack.set(ModComponents.NFC_DATA, data);
    }

    public static boolean isReadOnly(ItemStack stack) {
        return stack.getComponents().getOrDefault(ModComponents.NFC_READONLY, false);
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
        return 0xFF_000000 | stack.getComponents().getOrDefault(ModComponents.NFC_COLOR, 0xFFFFFF);
    }

    public static Optional<String> getPrivateKey(ItemStack stack) {
        if (stack.getItem() instanceof IDataItem dataItem) {
            if (!dataItem.supportsPrivateKey()) {
                return Optional.empty();
            }

            return Optional.of(stack.getComponents().getOrDefault(ModComponents.NFC_PRIVATEKEY, Crypto.generatePrivateKey()));
        }

        return Optional.empty();
    }

    public static void setPrivateKey(ItemStack stack, String privateKey) {
        if (stack.getItem() instanceof IDataItem dataItem) {
            if (!dataItem.supportsPrivateKey()) {
                return;
            }

            stack.set(ModComponents.NFC_PRIVATEKEY, privateKey);
        }
    }

    static boolean tryMigrate(ItemStack stack) {
        if (stack.has(ModComponents.NFC_DATA)) {
            return false;
        }

        var legacyData = DataItemData.migrate(stack);
        if (legacyData.isEmpty()) {
            return false;
        }

        ItemDataHandler.setData(stack, legacyData.get().data());
        ItemDataHandler.setReadOnly(stack, legacyData.get().readonly());
        ItemDataHandler.setColor(stack, legacyData.get().color());

        return true;
    }

    record DataItemData(String data, boolean readonly, int color, @Nullable String privateKey) {
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
            String privateKey = null;

            var tag = customData.copyTag();
            if (!tag.contains("data", Tag.TAG_STRING)) {
                return Optional.empty();
            }

            data = tag.getString("data");
            tag.remove("data");

            if (tag.contains("readOnly", Tag.TAG_BYTE)) {
                readonly = tag.getBoolean("readOnly");
                tag.remove("readOnly");
            }

            if (tag.contains("color", Tag.TAG_INT)) {
                color = tag.getInt("color");
                tag.remove("color");
            }

            if (tag.hasUUID("uuid")) {
                var uuid = tag.getUUID("uuid");
                var state = ClassicPeripherals.getState();
                var legacyData = state.getData(uuid);
                if (legacyData.isPresent()) {
                    data = legacyData.get().data;
                    privateKey = legacyData.get().privateKey;

                    state.removeData(uuid);
                    tag.remove("uuid");
                }
            }

            return Optional.of(new DataItemData(data, readonly, color, privateKey));
        }
    }

    public static class ItemData {
        public String data;
        public @Nullable String privateKey;

        public ItemData(String data, @Nullable String privateKey) {
            this.data = data;
            this.privateKey = privateKey;
        }
    }
}
