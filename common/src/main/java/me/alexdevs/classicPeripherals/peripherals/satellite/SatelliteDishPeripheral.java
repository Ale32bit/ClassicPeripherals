package me.alexdevs.classicPeripherals.peripherals.satellite;

import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.classicPeripherals.core.satellite.SatelliteDevice;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class SatelliteDishPeripheral extends AbstractSatelliteDishPeripheral {
    private final SatelliteDishBlockEntity dish;

    public SatelliteDishPeripheral(SatelliteDishBlockEntity dish) {
        this.dish = dish;
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof SatelliteDishPeripheral o && dish == o.dish;
    }

    @Override
    public Vec3 getPosition() {
        return Vec3.atLowerCornerOf(dish.getBlockPos().relative(dish.getDirection()));
    }

    @Override
    public ServerLevel getLevel() {
        return (ServerLevel) dish.getLevel();
    }

    @Override
    public void onDataReceived(String data, SatelliteDevice source) {

    }

    @Override
    public int getRange() {
        return 0;
    }
}
