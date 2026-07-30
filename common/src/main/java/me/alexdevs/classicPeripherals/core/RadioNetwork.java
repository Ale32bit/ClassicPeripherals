package me.alexdevs.classicPeripherals.core;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.integrations.SableIntegration;
import me.alexdevs.classicPeripherals.peripherals.radio.AbstractRadioPeripheral;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RadioNetwork {

    public static final int MIN_FREQUENCY = 0;
    public static final int MAX_FREQUENCY = 0xFFFF;
    public static final int STEP_FREQUENCY = 1;

    private final Set<AbstractRadioPeripheral> receivers = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public static int getChannel(int frequency) {
        return (frequency - MIN_FREQUENCY) / STEP_FREQUENCY;
    }

    public static int getFrequency(int channel) {
        return channel * STEP_FREQUENCY + MIN_FREQUENCY;
    }

    public void addReceiver(AbstractRadioPeripheral receiver) {
        receivers.add(receiver);
    }

    public void removeReceiver(AbstractRadioPeripheral receiver) {
        receivers.remove(receiver);
    }

    public void broadcast(AbstractRadioPeripheral source, String data, double range) {
        if (!source.canBroadcast()) {
            return;
        }

        data = data.substring(0, Math.min(data.length(), ClassicPeripherals.CONFIG.radioTowerMaxMessageSize));

        for (var receiver : receivers) {
            tryBroadcast(source, receiver, data, range);
        }
    }

    private void tryBroadcast(AbstractRadioPeripheral sender, AbstractRadioPeripheral receiver, String data, double range) {
        if (sender == receiver) {
            return;
        }

        if (sender.getLevel() != receiver.getLevel()) {
            return;
        }

        if (sender.getChannel() != receiver.getChannel()) {
            return;
        }

        var receiveRange = Math.max(range, receiver.getRange());
        var distanceSquared = SableIntegration.getDistanceSquared(receiver.getLevel(), receiver.getPosition(), sender.getPosition());
        if (distanceSquared <= receiveRange * receiveRange) {
            receiver.receive(data, Math.sqrt(distanceSquared), receiveRange);
        }
    }
}
