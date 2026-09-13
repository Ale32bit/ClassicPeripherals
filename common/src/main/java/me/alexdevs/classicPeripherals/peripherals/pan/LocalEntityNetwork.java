package me.alexdevs.classicPeripherals.peripherals.pan;

import dan200.computercraft.api.network.Packet;
import dan200.computercraft.api.network.PacketNetwork;
import dan200.computercraft.api.network.PacketReceiver;

import java.util.HashSet;

public class LocalEntityNetwork implements PacketNetwork {
    private final HashSet<PacketReceiver> receivers = new HashSet<>();

    @Override
    public void addReceiver(PacketReceiver receiver) {
        receivers.add(receiver);
    }

    @Override
    public void removeReceiver(PacketReceiver receiver) {
        receivers.remove(receiver);
    }

    @Override
    public boolean isWireless() {
        return true;
    }

    @Override
    public void transmitSameDimension(Packet packet, double range) {
        for (PacketReceiver receiver : receivers) {
            if (packet.sender() != receiver) {
                receiver.receiveSameDimension(packet, range);
            }
        }
    }

    @Override
    public void transmitInterdimensional(Packet packet) {
        // do nothing
    }
}
