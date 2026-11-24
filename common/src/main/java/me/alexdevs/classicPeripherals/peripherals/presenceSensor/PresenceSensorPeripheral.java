package me.alexdevs.classicPeripherals.peripherals.presenceSensor;

import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class PresenceSensorPeripheral extends AbstractPresenceSensorPeripheral {
    private final PresenceSensorBlockEntity presenceSensor;

    public PresenceSensorPeripheral(PresenceSensorBlockEntity presenceSensor) {
        this.presenceSensor = presenceSensor;
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof PresenceSensorPeripheral o && presenceSensor == o.presenceSensor;
    }

    @Override
    public Vec3 getPosition() {
        return presenceSensor.getBlockPos().getCenter();
    }

    @Override
    public Level getLevel() {
        return presenceSensor.getLevel();
    }
}
