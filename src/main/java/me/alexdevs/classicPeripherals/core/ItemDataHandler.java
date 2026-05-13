package me.alexdevs.classicPeripherals.core;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.item.IDataItem;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class ItemDataHandler {
    private static ItemData getData(UUID id) {
        var state = ClassicPeripherals.getState();
        if (state == null) {
            return new ItemData();
        }

        return state.getData(id).orElse(new ItemData());
    }

    private static void setData(UUID id, ItemData data) {
        var state = ClassicPeripherals.getState();
        if (state == null) {
            return;
        }

        state.setData(id, data);
    }

    public static Optional<UUID> getId(ItemStack stack) {
        var tag = stack.getOrCreateTag();

        if (tag.hasUUID("uuid")) {
            return Optional.of(tag.getUUID("uuid"));
        }

        return Optional.empty();
    }

    public static UUID getOrCreateId(ItemStack stack) {
        var tag = stack.getOrCreateTag();
        if (!tag.hasUUID("uuid")) {
            tag.putUUID("uuid", UUID.randomUUID());
        }

        return tag.getUUID("uuid");
    }

    public static Optional<String> getData(ItemStack stack) {
        var tag = stack.getOrCreateTag();
        var uuid = getId(stack);

        if (uuid.isEmpty()) {
            return Optional.empty();
        }

        String dataContent;
        var data = getData(uuid.get());
        if (tag.contains("data", Tag.TAG_STRING)) {
            dataContent = tag.getString("data");
            data.data = dataContent;
            setData(uuid.get(), data);
            tag.remove("data");
        }

        dataContent = data.data;

        if (dataContent.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(data.data);
    }

    public static void setData(ItemStack stack, String data) {
        var uuid = getOrCreateId(stack);
        var dataItem = getData(uuid);
        dataItem.data = data;
        setData(uuid, dataItem);
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

    public static void setColor(ItemStack stack, int color) {
        var tag = stack.getOrCreateTag();
        tag.putInt("color", color);
    }

    public static int getColor(ItemStack stack) {
        var tag = stack.getOrCreateTag();
        if (tag.contains("color", Tag.TAG_INT)) {
            return tag.getInt("color");
        }

        return 0xFFFFFF;
    }

    public static Optional<String> getPrivateKey(ItemStack stack) {
        var id = getId(stack);
        if (id.isEmpty()) {
            return Optional.empty();
        }

        if (stack.getItem() instanceof IDataItem dataItem && dataItem.supportsPrivateKey()) {
            var data = getData(id.get());

            if (data.privateKey == null || data.privateKey.isEmpty()) {
                var key = Crypto.generatePrivateKey();
                data.privateKey = key;
                setData(id.get(), data);
                return Optional.of(key);
            }

            return Optional.of(data.privateKey);
        }

        return Optional.empty();
    }

    public static void setPrivateKey(ItemStack stack, String privateKey) {
        if (stack.getItem() instanceof IDataItem dataItem && dataItem.supportsPrivateKey()) {
            var id = getOrCreateId(stack);
            var data = getData(id);
            data.privateKey = privateKey;
            setData(id, data);
        }
    }

    public static class ItemData {
        public String data;
        public @Nullable String privateKey;

        public ItemData(String data, @Nullable String privateKey) {
            this.data = data;
            this.privateKey = privateKey;
        }

        public ItemData(String data) {
            this.data = data;
        }

        public ItemData() {
            data = "";
        }
    }
}
