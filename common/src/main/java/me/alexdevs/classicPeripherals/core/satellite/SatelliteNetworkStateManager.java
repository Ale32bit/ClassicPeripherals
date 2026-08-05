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

    private final Map<BlockPos, SatelliteState> data = new ConcurrentHashMap<>();

    private static String packPos(BlockPos pos) {
        return pos.getX() + ";" + pos.getZ();
    }

    private static BlockPos unpackPos(String packedPos) {
        var parts = packedPos.split(";");
        return new BlockPos(Integer.parseInt(parts[0]), 0, Integer.parseInt(parts[1]));
    }

    public static SatelliteNetworkStateManager loadData(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        var state = new SatelliteNetworkStateManager();

        var satelliteMap = nbt.getCompound("satelliteMap");
        for (var entry : satelliteMap.getAllKeys()) {
            var posKey = unpackPos(entry);
            var satelliteTag = satelliteMap.getCompound(entry);

            var uuid = satelliteTag.getUUID("uuid");

            var x = satelliteTag.getInt("x");
            var y = satelliteTag.getInt("y");
            var z = satelliteTag.getInt("z");

            var pos = new BlockPos(x, y, z);

            var runtimeType = Satellite.RuntimeType.valueOf(satelliteTag.getString("runtimeType"));
            var channel = satelliteTag.getInt("channel");

            state.data.put(posKey, new SatelliteState(uuid, pos, runtimeType, channel));
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
            var satelliteState = entry.getValue();
            var satelliteTag = new CompoundTag();

            var pos = satelliteState.pos;
            var posKey = packPos(pos);

            satelliteTag.putUUID("uuid", satelliteState.uuid);

            satelliteTag.putInt("x", pos.getX());
            satelliteTag.putInt("y", pos.getY());
            satelliteTag.putInt("z", pos.getZ());

            satelliteTag.putString("runtimeType", satelliteState.runtimeType.toString());
            satelliteTag.putInt("channel", satelliteState.channel);

            satelliteMap.put(posKey, satelliteTag);
        }

        nbt.put("satelliteMap", satelliteMap);

        return nbt;
    }

    public Map<BlockPos, SatelliteState> getData() {
        return data;
    }

    public record SatelliteState(UUID uuid, BlockPos pos, Satellite.RuntimeType runtimeType, int channel) {

    }
}
