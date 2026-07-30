package me.alexdevs.classicPeripherals.core.satellite;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayDeque;
import java.util.Queue;

public class Satellite extends SatelliteDevice {
    public static final int MAX_DATA_QUEUE_SIZE = 255;

    public enum RuntimeType {
        RELAY("relay", true),
        GPS("gps", false),
        CPU("cpu", true),
        ;

        public final String name;
        public final boolean canReceive;

        RuntimeType(String name, boolean canReceive) {
            this.name = name;
            this.canReceive = canReceive;
        }
    }

    private final Queue<String> dataQueue = new ArrayDeque<>(20);
    private final RuntimeType runtimeType;

    public Satellite(SatelliteNetwork network, BlockPos position, ServerLevel level, RuntimeType runtimeType) {
        super(network, position, level, SatelliteType.SATELLITE);

        this.runtimeType = runtimeType;
    }

    private String serializeBlockPos() {
        return String.format("%d;%d;%d", position.getX(), position.getY(), position.getZ());
    }

    @Override
    public void tick(MinecraftServer server) {
        switch (runtimeType) {
            case RELAY:
                if (!dataQueue.isEmpty()) {
                    var data = dataQueue.poll();
                    network.broadcast(data, this);
                }

                break;
            case GPS:
                var gameTime = level.getGameTime();
                // every 20 ticks (1 second) broadcast GPS coordinates of the satellite.
                if (gameTime % 20 == 0) {
                    network.broadcast(serializeBlockPos(), this);
                }
                break;
            case CPU:
                // todo

                break;
        }
    }

    @Override
    public void onDataReceived(String data, SatelliteDevice source) {
        if (runtimeType.canReceive) {
            if (dataQueue.size() < MAX_DATA_QUEUE_SIZE) {
                dataQueue.add(data);
            }
        }
    }

    @Override
    public int getRange() {
        return 1024 * 16;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Satellite other && position.equals(other.position) && level.equals(other.level);
    }
}
