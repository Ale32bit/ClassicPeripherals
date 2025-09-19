package me.alexdevs.ccNetworks.core;

import java.util.HashMap;
import java.util.Map;

public class TowerNetwork {
    private static final Map<Location, Tower> towers = new HashMap<>();

    public static final int MIN_FREQUENCY = 100;
    public static final int MAX_FREQUENCY = 1000;
    public static final int STEP_FREQUENCY = 5;

    public static int getChannel(int frequency) {
        return (frequency - MIN_FREQUENCY) / STEP_FREQUENCY;
    }

    public static void addTower(Tower tower) {
        var location = new Location(tower.getBasePos(), tower.getLevel());
        towers.put(location, tower);
    }

    public static void removeTower(Tower tower) {
        var location = new Location(tower.getBasePos(), tower.getLevel());
        towers.remove(location);
    }

    public static void broadcast(Tower sourceTower, String data) {
        var level = sourceTower.getLevel();

        var receivers = towers.values().stream()
                .filter(x -> x.getLevel() == level)
                .toList();

        for (var receiver : receivers) {
            if (receiver.inRange(sourceTower)) {
                var distance = receiver.getTopPos().distSqr(sourceTower.getTopPos());
                receiver.receive(data, Math.sqrt(distance));
            }
        }
    }
}
