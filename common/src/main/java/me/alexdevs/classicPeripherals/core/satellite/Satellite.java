package me.alexdevs.classicPeripherals.core.satellite;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.UUID;

public class Satellite extends SatelliteDevice {
    public static final int MAX_DATA_QUEUE_SIZE = 255;

    public enum RuntimeType {
        relay("relay", true),
        gps("gps", false),
        cpu("cpu", true),
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
    private final UUID uuid;
    private BlockPos position;
    private final ServerLevel level;

    public Satellite(SatelliteNetwork network, UUID uuid, BlockPos position, ServerLevel level, RuntimeType runtimeType) {
        super(network, SatelliteType.SATELLITE);

        this.runtimeType = runtimeType;
        this.uuid = uuid;
        this.position = position;
        this.level = level;
    }

    private String serializeBlockPos() {
        return String.format("%d;%d;%d", position.getX(), position.getY(), position.getZ());
    }

    public UUID getUUID() {
        return uuid;
    }

    public RuntimeType getRuntimeType() {
        return runtimeType;
    }

    @Override
    public void tick(ServerLevel level) {
        switch (runtimeType) {
            case relay:
                while (!dataQueue.isEmpty()) {
                    var data = dataQueue.poll();
                    broadcast(data);
                }

                break;
            case gps:
                var gameTime = level.getGameTime();
                // every 20 ticks (1 second) broadcast GPS coordinates of the satellite.
                if (gameTime % 20 == 0) {
                    broadcast(serializeBlockPos());
                }

                break;
            case cpu:
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
        return 1024 * 16; // todo: placeholder value
    }

    @Override
    public Vec3 getPosition() {
        return position.getCenter();
    }

    public void setPosition(BlockPos pos) {
        position = pos;
    }

    @Override
    public ServerLevel getLevel() {
        return level;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Satellite other && position.equals(other.position) && level.equals(other.level) && uuid.equals(other.uuid);
    }
}
