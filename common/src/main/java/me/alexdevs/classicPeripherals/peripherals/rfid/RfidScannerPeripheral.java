package me.alexdevs.classicPeripherals.peripherals.rfid;

import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class RfidScannerPeripheral extends AbstractRfidScannerPeripheral {
    private final RfidScannerBlockEntity rfidScanner;

    public RfidScannerPeripheral(RfidScannerBlockEntity rfidScanner) {
        this.rfidScanner = rfidScanner;
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof RfidScannerPeripheral o && rfidScanner == o.rfidScanner;
    }

    @Override
    public Vec3 getPosition() {
        return Vec3.atLowerCornerOf(rfidScanner.getBlockPos().relative(rfidScanner.getDirection()));
    }

    @Override
    public Level getLevel() {
        return rfidScanner.getLevel();
    }

    @Override
    protected void updateState(boolean active) {
        var state = rfidScanner.getBlockState();
        var pos = rfidScanner.getBlockPos();
        rfidScanner.getLevel().setBlockAndUpdate(pos, state.setValue(RfidScannerBlock.ACTIVE, active));
    }
}
