package me.alexdevs.ccNetworks.core;

import me.alexdevs.ccNetworks.tiles.TowerBlockEntity;

import java.util.HashMap;
import java.util.Map;

public class TowerNetwork {
    private static final Map<Location, TowerBlockEntity> towers = new HashMap<>();

    public static final int MIN_FREQUENCY = 0;
    public static final int MAX_FREQUENCY = 63;
    public static final int STEP_FREQUENCY = 1;

    public static int getChannel(int frequency) {
        return (frequency - MIN_FREQUENCY) / STEP_FREQUENCY;
    }

    public static int getFrequency(int channel) {
        return channel * STEP_FREQUENCY + MIN_FREQUENCY;
    }

    public static void addTower(TowerBlockEntity tower) {
        var location = new Location(tower.getBlockPos(), tower.getLevel());
        towers.put(location, tower);
    }

    public static void removeTower(TowerBlockEntity tower) {
        var location = new Location(tower.getBlockPos(), tower.getLevel());
        towers.remove(location);
    }

    public static void broadcast(TowerBlockEntity sourceTower, String data) {
        var level = sourceTower.getLevel();
        var channel = sourceTower.getChannel();

        var receivers = towers.values().stream()
                .filter(x -> x.getLevel() == level && x.getChannel() == channel && x != sourceTower)
                .toList();

        for (var receiver : receivers) {
            if (receiver.inRange(sourceTower)) {
                var distance = receiver.getTopPos().distSqr(sourceTower.getTopPos());
                receiver.receive(data, Math.sqrt(distance), sourceTower);
            }
        }
    }
}
