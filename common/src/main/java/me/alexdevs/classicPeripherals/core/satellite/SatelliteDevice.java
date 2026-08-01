package me.alexdevs.classicPeripherals.core.satellite;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public abstract class SatelliteDevice {
    public enum SatelliteType {
        SATELLITE,
        DISH,
    }

    protected final SatelliteNetwork network;
    protected final BlockPos position;
    protected final ServerLevel level;
    protected final SatelliteType type;

    protected int channel = 0;

    public SatelliteDevice(SatelliteNetwork network, BlockPos position, ServerLevel level, SatelliteType type) {
        this.network = network;
        this.position = position;
        this.level = level;
        this.type = type;
    }

    public void broadcast(String data) {
        network.broadcast(data, this);
    }

    public abstract void onDataReceived(String data, SatelliteDevice source);

    public abstract int getRange();

    public BlockPos getPosition() {
        return position;
    }

    public ServerLevel getLevel() {
        return level;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public SatelliteType getType() {
        return type;
    }


    public void tick(ServerLevel level) {
    }
}
