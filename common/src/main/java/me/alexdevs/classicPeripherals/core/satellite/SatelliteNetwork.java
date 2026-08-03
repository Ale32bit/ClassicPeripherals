package me.alexdevs.classicPeripherals.core.satellite;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SatelliteNetwork {
    public static final int MAX_CHANNEL = 65535;
    public static final int MIN_CHANNEL = 0;

    private final ConcurrentHashMap<ServerLevel, Set<SatelliteDevice>> levels = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<ServerLevel, SatelliteNetworkStateManager> stateManagers = new ConcurrentHashMap<>();

    public void load(MinecraftServer server) {
        for (var level : server.getAllLevels()) {
            var state = getStateManager(level);
            var levelSatellites = getSatellites(level);

            var stateMap = state.getData();

            stateMap.forEach((uuid, satelliteState) -> {
                var satellite = new Satellite(this, satelliteState.uuid(), satelliteState.pos(), level, satelliteState.runtimeType());
                satellite.setChannel(satelliteState.channel());
                levelSatellites.add(satellite);
            });
        }
    }

    public void addSatellite(SatelliteDevice satellite) {
        var satellites = getSatellites(satellite.getLevel());
        satellites.add(satellite);

        if (satellite instanceof Satellite sat) {
            var state = getStateManager(satellite.getLevel());
            state.getData().put(sat.getUUID(), toSatelliteState(sat));
            state.setDirty();
        }
    }

    public void removeSatellite(SatelliteDevice satellite) {
        var satellites = getSatellites(satellite.getLevel());
        satellites.remove(satellite);

        if (satellite instanceof Satellite sat) {
            var state = getStateManager(satellite.getLevel());
            state.getData().remove(sat.getUUID());
            state.setDirty();
        }
    }

    public void tick(ServerLevel level) {
        var satellites = getSatellites(level);
        satellites.forEach(satellite -> satellite.tick(level));
    }

    public void broadcast(String data, SatelliteDevice source) {
        var channel = source.getChannel();
        var level = source.getLevel();
        var sourcePosition = source.getPosition();
        var sourceRange = source.getRange();
        var satellites = getSatellites(level);

        var flatSource = flatPosition(sourcePosition);

        var receivers = satellites.stream()
                .filter(receiver
                        -> receiver.getChannel() == channel // same channel
                        && !receiver.equals(source) // not source
                        && !receiver.getDeviceType().equals(source.getDeviceType()) // different type: sats can talk to dishes and dishes can talk to sats.
                )
                .toList();


        for (var receiver : receivers) {
            var range = Math.max(sourceRange, receiver.getRange());
            if (flatSource.distanceToSqr(flatPosition(receiver.getPosition())) <= range * range) {
                receiver.onDataReceived(data, source);
            }
        }
    }

    private Set<SatelliteDevice> getSatellites(@NotNull ServerLevel level) {
        return levels.computeIfAbsent(level, l -> Collections.newSetFromMap(new ConcurrentHashMap<>()));
    }

    private SatelliteNetworkStateManager getStateManager(ServerLevel level) {
        return stateManagers.computeIfAbsent(level, l -> SatelliteNetworkStateManager.getLevelState(level));
    }

    private SatelliteNetworkStateManager.SatelliteState toSatelliteState(Satellite satellite) {
        return new SatelliteNetworkStateManager.SatelliteState(
                satellite.getUUID(),
                toBlockPos(satellite.getPosition()),
                satellite.getRuntimeType(),
                satellite.getChannel()
        );
    }

    private static BlockPos toBlockPos(Vec3 vec3) {
        return new BlockPos(
                (int) vec3.x,
                (int) vec3.y,
                (int) vec3.z
        );
    }

    private static Vec2 flatPosition(Vec3 vec3) {
        return new Vec2((float) vec3.x(), (float) vec3.z());
    }
}
