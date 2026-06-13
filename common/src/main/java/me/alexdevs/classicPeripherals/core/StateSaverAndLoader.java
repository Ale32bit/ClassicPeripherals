package me.alexdevs.classicPeripherals.core;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.core.dataHolder.DataHolderHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StateSaverAndLoader extends SavedData {

    private static SavedData.Factory<StateSaverAndLoader> type = new SavedData.Factory<>(
            StateSaverAndLoader::createNew,
            StateSaverAndLoader::loadData,
            null
    );

    private Map<UUID, DataHolderHandler.ItemData> data = new ConcurrentHashMap<>();

    public static StateSaverAndLoader loadData(CompoundTag nbt, HolderLookup.Provider registryLookup) {
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

            state.data.put(dataId, new DataHolderHandler.ItemData(data, privateKey));
        }

        return state;
    }

    public static StateSaverAndLoader createNew() {
        return new StateSaverAndLoader();
    }

    public static StateSaverAndLoader getServerState(MinecraftServer server) {
        var stateManager = server.getLevel(Level.OVERWORLD).getDataStorage();

        var state = stateManager.computeIfAbsent(
                type,
                ClassicPeripherals.MOD_ID
        );

        state.setDirty(true);

        return state;
    }

    @Override
    public @NonNull CompoundTag save(@NonNull CompoundTag nbt, HolderLookup.@NonNull Provider registries) {
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
    public Optional<DataHolderHandler.ItemData> getData(UUID id) {
        return Optional.ofNullable(data.getOrDefault(id, null));
    }

    public void setData(UUID id, DataHolderHandler.ItemData data) {
        this.data.put(id, data);
        this.setDirty(true);
    }
}
