package me.alexdevs.classicPeripherals.core.satellite;

import net.minecraft.server.MinecraftServer;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SatelliteNetwork {
    private final Set<SatelliteDevice> satellites = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public void addSatellite(SatelliteDevice satellite) {
        satellites.add(satellite);
    }

    public void removeSatellite(SatelliteDevice satellite) {
        satellites.remove(satellite);
    }

    public void tick(MinecraftServer server) {
        satellites.forEach(satellite -> satellite.tick(server));
    }

    public void broadcast(String data, SatelliteDevice source) {
        var channel = source.getChannel();
        var level = source.getLevel();
        var sourcePosition = source.getPosition();
        var sourceRange = source.getRange();

        var receivers = satellites.stream()
                .filter(receiver
                        -> receiver.getChannel() == channel // same channel
                        && receiver.getLevel() == level
                        && !receiver.equals(source) // not source
                        && !receiver.getType().equals(source.getType()) // different type: sats can talk to dishes and dishes can talk to sats.
                )
                .toList();

        for (var receiver : receivers) {
            var range = Math.max(sourceRange, receiver.getRange());
            if (range * range <= sourcePosition.distSqr(receiver.getPosition())) {
                receiver.onDataReceived(data, source);
            }
        }
    }
}
