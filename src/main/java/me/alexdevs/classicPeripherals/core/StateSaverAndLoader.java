package me.alexdevs.classicPeripherals.core;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StateSaverAndLoader extends SavedData {

    private Map<UUID, ItemDataHandler.ItemData> data = new ConcurrentHashMap<>();

    public static StateSaverAndLoader loadData(CompoundTag nbt) {
        var state = new StateSaverAndLoader();

        var cardDataMap = nbt.getCompound("cardDataMap");
        for (var entry : cardDataMap.getAllKeys()) {
            var cardDataItem = cardDataMap.getCompound(entry);
            var dataId = UUID.fromString(entry);

            var data = cardDataItem.getString("data");
            var privateKey = cardDataItem.getString("privateKey");

            if (privateKey.isEmpty()) {
                privateKey = null;
            }

            state.data.put(dataId, new ItemDataHandler.ItemData(data, privateKey));
        }

        return state;
    }

    public static StateSaverAndLoader getServerState(MinecraftServer server) {
        var stateManager = server.getLevel(Level.OVERWORLD).getDataStorage();

        var state = stateManager.computeIfAbsent(
                StateSaverAndLoader::loadData,
                StateSaverAndLoader::new,
                ClassicPeripherals.MOD_ID
        );

        state.setDirty(true);

        return state;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        var cardDataMap = new CompoundTag();
        for (var entry : data.entrySet()) {
            CompoundTag cardDataItem = new CompoundTag();
            cardDataItem.putString("data", entry.getValue().data);

            var privateKey = entry.getValue().privateKey;
            if (privateKey != null && !privateKey.isEmpty()) {
                cardDataItem.putString("privateKey", entry.getValue().privateKey);
            }

            cardDataMap.put(entry.getKey().toString(), cardDataItem);
        }

        nbt.put("cardDataMap", cardDataMap);

        return nbt;
    }

    public Optional<ItemDataHandler.ItemData> getData(UUID id) {
        return Optional.ofNullable(data.getOrDefault(id, null));
    }

    public void setData(UUID id, ItemDataHandler.ItemData data) {
        this.data.put(id, data);
        this.setDirty(true);
    }

    public void removeData(UUID id) {
        this.data.remove(id);
        this.setDirty(true);
    }
}
