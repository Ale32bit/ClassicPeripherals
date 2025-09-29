package me.alexdevs.ccNetworks.core;

import me.alexdevs.ccNetworks.tiles.AbstractRadioBlockEntity;

import java.util.HashMap;
import java.util.Map;

public class TowerNetwork {
    private static final Map<Location, AbstractRadioBlockEntity> towers = new HashMap<>();

    public static final int MIN_FREQUENCY = 0;
    public static final int MAX_FREQUENCY = 0xFFFF;
    public static final int STEP_FREQUENCY = 1;
    public static final int MAX_MESSAGE_SIZE = 8 * 1024 * 1024; // 8 MiB

    public static int getChannel(int frequency) {
        return (frequency - MIN_FREQUENCY) / STEP_FREQUENCY;
    }

    public static int getFrequency(int channel) {
        return channel * STEP_FREQUENCY + MIN_FREQUENCY;
    }

    public static void addTower(AbstractRadioBlockEntity tower) {
        var location = new Location(tower.getBlockPos(), tower.getLevel());
        towers.put(location, tower);
    }

    public static void removeTower(AbstractRadioBlockEntity tower) {
        var location = new Location(tower.getBlockPos(), tower.getLevel());
        towers.remove(location);
    }

    public static void broadcast(AbstractRadioBlockEntity sourceTower, String data) {
        if(!sourceTower.canBroadcast()) {
            throw new IllegalStateException(sourceTower + " cannot broadcast.");
        }

        var level = sourceTower.getLevel();
        var channel = sourceTower.getChannel();
        data = data.substring(0, Math.min(data.length(), MAX_MESSAGE_SIZE));

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
