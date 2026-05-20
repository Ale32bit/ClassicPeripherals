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
        var uuid = stack.getOrDefault(ModComponents.DATAHOLDER_UUID, null);
        return Optional.ofNullable(uuid);

    }

    public static UUID getOrCreateId(ItemStack stack) {
        var uuid = getId(stack).orElseGet(() -> {
            var id = UUID.randomUUID();
            stack.set(ModComponents.DATAHOLDER_UUID, id);
            return id;
        });

        return uuid;
    }

    public static Optional<String> getData(ItemStack stack) {
        tryMigrate(stack);

        var id = getId(stack);
        if (id.isEmpty()) {
            return Optional.empty();
        }

        var data = getData(id.get());
        if (data.data == null || data.data.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(data.data);
    }

    public static void setData(ItemStack stack, String data) {
        var id = getOrCreateId(stack);
        var dataItem = getData(id);
        dataItem.data = data;
        setData(id, dataItem);
    }

    public static boolean isReadOnly(ItemStack stack) {
        return stack.getComponents().getOrDefault(ModComponents.DATAHOLDER_READONLY.get(), false);
    }

    public static void setReadOnly(ItemStack stack, boolean readOnly) {
        stack.set(ModComponents.DATAHOLDER_READONLY, readOnly);
    }

    public static void setLabel(ItemStack stack, String title) {
        stack.set(DataComponents.CUSTOM_NAME, Component.nullToEmpty(title));
    }

    public static void clearLabel(ItemStack stack) {
        stack.remove(DataComponents.CUSTOM_NAME);
    }

    public static void setColor(ItemStack stack, int color) {
        stack.set(ModComponents.DATAHOLDER_COLOR, color & 0x00_FFFFFF);
    }

    public static int getColor(ItemStack stack) {
        return 0xFF_000000 | stack.getComponents().getOrDefault(ModComponents.DATAHOLDER_COLOR.get(), 0xFFFFFF);
    }

    public static Optional<String> getPrivateKey(ItemStack stack) {
        if (stack.getItem() instanceof IDataItem dataItem) {
            if (!dataItem.supportsPrivateKey()) {
                return Optional.empty();
            }

            var id = getId(stack);
            if (id.isEmpty()) {
                return Optional.empty();
            }

            var data = getData(id.get());
            if (data.privateKey != null) {
                return Optional.of(data.privateKey);
            } else {
                setPrivateKey(stack, Crypto.generatePrivateKey());
                return Optional.ofNullable(data.privateKey);
            }
        }

        return Optional.empty();
    }

    public static void setPrivateKey(ItemStack stack, String privateKey) {
        if (stack.getItem() instanceof IDataItem dataItem) {
            if (!dataItem.supportsPrivateKey()) {
                return;
            }

            var id = getOrCreateId(stack);
            var data = getData(id);
            data.privateKey = privateKey;
            setData(id, data);
        }
    }

    static boolean tryMigrate(ItemStack stack) {
        if (stack.has(ModComponents.DATAHOLDER_UUID)) {
            return false;
        }

        var legacyData = DataItemData.migrate(stack);
        if (legacyData.isEmpty()) {
            return false;
        }

        var data = getData(legacyData.get().uuid());
        data.data = legacyData.get().data();
        data.privateKey = legacyData.get().privateKey();

        setData(legacyData.get().uuid(), data);

        stack.set(ModComponents.DATAHOLDER_UUID, legacyData.get().uuid());

        ItemDataHandler.setReadOnly(stack, legacyData.get().readonly());
        ItemDataHandler.setColor(stack, legacyData.get().color());

        return true;
    }

    record DataItemData(UUID uuid, String data, boolean readonly, int color, @Nullable String privateKey) {
        public static Optional<DataItemData> migrate(ItemStack stack) {
            if (!(stack.getItem() instanceof IDataItem)) {
                return Optional.empty();
            }

            if (stack.has(ModComponents.DATAHOLDER_UUID)) {
                return Optional.empty();
            }

            var customData = stack.get(DataComponents.CUSTOM_DATA);
            if ((customData == null || customData.isEmpty()) && !stack.has(ModComponents.DATAHOLDER_DATA)) {
                return Optional.empty();
            }

            UUID uuid;
            String data;
            boolean readonly = false;
            int color = 0xFFFFFF;
            String privateKey = null;


            if (customData != null) {
                var tag = customData.copyTag();

                if (!tag.contains("data", Tag.TAG_STRING) && !tag.hasUUID("uuid")) {
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
                    uuid = tag.getUUID("uuid");
                    var state = ClassicPeripherals.getState();
                    var legacyData = state.getData(uuid);
                    if (legacyData.isPresent()) {
                        data = legacyData.get().data;
                        privateKey = legacyData.get().privateKey;

                        tag.remove("uuid");
                    }
                } else {
                    uuid = UUID.randomUUID();
                }
            } else {
                uuid = UUID.randomUUID();
                if (stack.has(ModComponents.DATAHOLDER_DATA)) {
                    data = stack.get(ModComponents.DATAHOLDER_DATA);
                    stack.remove(ModComponents.DATAHOLDER_DATA);
                } else {
                    return Optional.empty();
                }
            }

            return Optional.of(new DataItemData(uuid, data, readonly, color, privateKey));
        }
    }

    public static class ItemData {
        public String data;
        public @Nullable String privateKey;

        public ItemData(String data, @Nullable String privateKey) {
            this.data = data;
            this.privateKey = privateKey;
        }

        public ItemData() {
            this("", null);
        }
    }
}
