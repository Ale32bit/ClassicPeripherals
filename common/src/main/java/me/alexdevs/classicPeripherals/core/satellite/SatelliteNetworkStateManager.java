package me.alexdevs.classicPeripherals.core.satellite;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.core.dataHolder.DataHolderHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SatelliteNetworkStateManager extends SavedData {
    private static final String STATE_NAME = ClassicPeripherals.MOD_ID + "_satellite_network";

    private static final Factory<SatelliteNetworkStateManager> type = new Factory<>(
            SatelliteNetworkStateManager::createNew,
            SatelliteNetworkStateManager::loadData,
            null
    );

    private final Map<UUID, SatelliteState> data = new ConcurrentHashMap<>();

    public static SatelliteNetworkStateManager loadData(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        var state = new SatelliteNetworkStateManager();

        var satelliteMap = nbt.getCompound("satelliteMap");
        for (var entry : satelliteMap.getAllKeys()) {
            var uuid = UUID.fromString(entry);
            var satelliteTag = satelliteMap.getCompound(entry);

            var x = satelliteTag.getInt("x");
            var y = satelliteTag.getInt("y");
            var z = satelliteTag.getInt("z");

            var pos = new BlockPos(x, y, z);

            var runtimeType = Satellite.RuntimeType.valueOf(satelliteTag.getString("runtimeType"));
            var channel = satelliteTag.getInt("channel");

            state.data.put(uuid, new SatelliteState(uuid, pos, runtimeType, channel));
        }

        return state;
    }

    private static SatelliteNetworkStateManager createNew() {
        return new SatelliteNetworkStateManager();
    }

    public static SatelliteNetworkStateManager getLevelState(ServerLevel level) {
        var stateManager = level.getDataStorage();

        var state = stateManager.computeIfAbsent(
                type,
                STATE_NAME
        );

        state.setDirty();

        return state;
    }

    @Override
    public @NonNull CompoundTag save(@NonNull CompoundTag nbt, HolderLookup.@NonNull Provider registries) {
        var satelliteMap = new CompoundTag();
        for (var entry : data.entrySet()) {
            var uuid = entry.getKey();
            var satelliteState = entry.getValue();
            var satelliteTag = new CompoundTag();

            satelliteTag.putInt("x", satelliteState.pos.getX());
            satelliteTag.putInt("y", satelliteState.pos.getY());
            satelliteTag.putInt("z", satelliteState.pos.getZ());

            satelliteTag.putString("runtimeType", satelliteState.runtimeType.toString());
            satelliteTag.putInt("channel", satelliteState.channel);

            satelliteMap.put(uuid.toString(), satelliteTag);
        }

        nbt.put("satelliteMap", satelliteMap);

        return nbt;
    }

    public Map<UUID, SatelliteState> getData() {
        return data;
    }

    public record SatelliteState(UUID uuid, BlockPos pos, Satellite.RuntimeType runtimeType, int channel) {

    }
}
