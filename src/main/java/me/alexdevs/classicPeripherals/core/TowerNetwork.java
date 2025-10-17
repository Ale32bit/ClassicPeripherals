package me.alexdevs.classicPeripherals.core;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.peripherals.RadioPeripheral;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TowerNetwork {
    private static final Set<RadioPeripheral> receivers = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public static final int MIN_FREQUENCY = 0;
    public static final int MAX_FREQUENCY = 0xFFFF;
    public static final int STEP_FREQUENCY = 1;

    public static int getChannel(int frequency) {
        return (frequency - MIN_FREQUENCY) / STEP_FREQUENCY;
    }

    public static int getFrequency(int channel) {
        return channel * STEP_FREQUENCY + MIN_FREQUENCY;
    }

    public static void addReceiver(RadioPeripheral receiver) {
        Objects.requireNonNull(receiver);
        receivers.add(receiver);
    }

    public static void removeReceiver(RadioPeripheral receiver) {
        Objects.requireNonNull(receiver);
        receivers.remove(receiver);
    }

    public static void broadcast(RadioPeripheral source, String data, double range) {
        if (!source.canBroadcast()) {
            return;
        }

        data = data.substring(0, Math.min(data.length(), ClassicPeripherals.CONFIG.radioTowerMaxMessageSize));

        for (var receiver : receivers) {
            tryBroadcast(source, receiver, data, range);
        }
    }

    private static void tryBroadcast(RadioPeripheral sender, RadioPeripheral receiver, String data, double range) {
        if (sender == receiver) {
            return;
        }

        if (sender.getLevel() != receiver.getLevel()) {
            return;
        }

        var receiveRange = Math.max(range, receiver.getRange());
        var distanceSquared = receiver.getPosition().distanceToSqr(sender.getPosition());
        if (distanceSquared <= receiveRange * receiveRange) {
            receiver.receive(data, Math.sqrt(distanceSquared), receiveRange);
        }
    }
}
