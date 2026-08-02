package me.alexdevs.classicPeripherals.core.satellite;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public abstract class SatelliteDevice {
    public enum SatelliteType {
        SATELLITE,
        DISH,
    }

    protected final SatelliteNetwork network;
    protected final SatelliteType type;

    protected int channel = 0;

    public SatelliteDevice(SatelliteNetwork network, SatelliteType type) {
        this.network = network;
        this.type = type;
    }

    public void broadcast(String data) {
        network.broadcast(data, this);
    }

    public abstract void onDataReceived(String data, SatelliteDevice source);

    public abstract int getRange();

    public abstract Vec3 getPosition();

    public abstract ServerLevel getLevel();

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public SatelliteType getDeviceType() {
        return type;
    }


    public void tick(ServerLevel level) {
    }
}
