package me.alexdevs.classicPeripherals.core.satellite;

import me.alexdevs.classicPeripherals.utils.Point2i;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SatelliteNetwork {
    public static final int MAX_CHANNEL = 65535;
    public static final int MIN_CHANNEL = 0;

    private final ConcurrentHashMap<ServerLevel, Set<SatelliteDevice>> levels = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<ServerLevel, SatelliteNetworkStateManager> stateManagers = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<ServerLevel, Map<Point2i, Satellite>> satelliteMap = new ConcurrentHashMap<>();

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
            var satelliteState = toSatelliteState(sat);
            getMap(satellite.getLevel()).put(Point2i.of(satelliteState.pos()), sat);
            var stateManager = getStateManager(satellite.getLevel());
            stateManager.getData().put(satelliteState.pos(), satelliteState);
            stateManager.setDirty();
        }
    }

    public void removeSatellite(SatelliteDevice satellite) {
        var satellites = getSatellites(satellite.getLevel());
        satellites.remove(satellite);

        if (satellite instanceof Satellite sat) {
            var satelliteState = toSatelliteState(sat);
            getMap(satellite.getLevel()).remove(Point2i.of(satelliteState.pos()));
            var stateManager = getStateManager(satellite.getLevel());
            stateManager.getData().remove(satelliteState.pos());
            stateManager.setDirty();
        }
    }

    public Optional<Satellite> getSatellite(ServerLevel level, Point2i pos) {
        var sat = getMap(level).get(pos);
        return Optional.ofNullable(sat);
    }

    public boolean hasSatellite(ServerLevel level, Point2i pos) {
        return getSatellite(level, pos).isPresent();
    }

    public int countChunkSatellites(ServerLevel level, ChunkPos pos) {
        var minX = pos.getMinBlockX();
        var minZ = pos.getMinBlockZ();
        var maxX = pos.getMaxBlockX();
        var maxZ = pos.getMaxBlockZ();

        var count = 0;

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                if (hasSatellite(level, new Point2i(x, z))) {
                    count++;
                }
            }
        }

        return count;
    }

    public List<Satellite> getSatellitesInRange(ServerLevel level, Point2i pos, int range) {
        var list = new ArrayList<Satellite>();
        for (int x = pos.x() - range; x <= pos.x() + range; x++) {
            for (int z = pos.z() - range; z <= pos.z() + range; z++) {
                var satellite = getSatellite(level, new Point2i(x, z));
                satellite.ifPresent(list::add);
            }
        }

        return list;
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

    private Map<Point2i, Satellite> getMap(ServerLevel level) {
        return satelliteMap.computeIfAbsent(level, l -> new ConcurrentHashMap<>());
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
