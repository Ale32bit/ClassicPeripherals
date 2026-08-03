package me.alexdevs.classicPeripherals.peripherals.satellite.launcher;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.ModRegistry;
import me.alexdevs.classicPeripherals.core.satellite.Satellite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SatelliteLauncherBlockEntity extends BlockEntity {
    public final static int MIN_HEIGHT = 3000;
    public final static int MAX_HEIGHT = 6000;

    public final static int MAX_ROCKET_POWER = 3;

    private final SatelliteLauncherPeripheral peripheral = new SatelliteLauncherPeripheral(this);

    private Satellite.RuntimeType runtimeType = Satellite.RuntimeType.relay;
    private int channel = 0;

    private ItemStack fuelStack = ItemStack.EMPTY;
    private ItemStack rocketStack = ItemStack.EMPTY;
    private ItemStack satelliteStack = ItemStack.EMPTY;

    public SatelliteLauncherBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModRegistry.TileEntities.SATELLITE_LAUNCHER.get(), pos, blockState);
    }

    public SatelliteLauncherPeripheral peripheral() {
        return peripheral;
    }

    private float getFuelPercentage() {
        return (float) fuelStack.getCount() / (float) fuelStack.getMaxStackSize();
    }

    private float getRocketPowerPercentage() {
        var firework = rocketStack.getComponents().getOrDefault(DataComponents.FIREWORKS, new Fireworks(1, List.of()));
        var flightDuration = (float) firework.flightDuration();

        return Math.clamp(flightDuration / MAX_ROCKET_POWER, 0f, 1f);
    }

    private int getMajorY() {
        var power = getRocketPowerPercentage();
        var range = MAX_HEIGHT - MIN_HEIGHT;
        return (int) (range * power + MIN_HEIGHT);
    }

    private int getMinorY() {
        var fuel = getFuelPercentage();
        var range = (MAX_HEIGHT - MIN_HEIGHT) / MAX_ROCKET_POWER;
        return (int) (range * fuel);
    }

    private int getTargetY() {
        var range = 100;
        var deviation = (int) (Math.random() * range) - (range / 2);
        return getMajorY() + getMinorY() + deviation;
    }

    public Optional<UUID> launch() {
        if (this.getLevel().isClientSide()) {
            return Optional.empty();
        }

        if (!isReady()) {
            return Optional.empty();
        }

        var x = this.getBlockPos().getX();
        var y = getTargetY();
        var z = this.getBlockPos().getZ();

        var uuid = UUID.randomUUID();

        var network = ClassicPeripherals.getSatelliteNetwork();
        var satellite = new Satellite(network, uuid, new BlockPos(x, y, z), (ServerLevel) this.getLevel(), runtimeType);
        satellite.setChannel(channel);
        network.addSatellite(satellite);

        return Optional.of(uuid);
    }

    public boolean isReady() {
        return true;
    }

    public void setMode(Satellite.RuntimeType type) {
        runtimeType = type;
    }

    public Satellite.RuntimeType getMode() {
        return runtimeType;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getChannel() {
        return channel;
    }
}
