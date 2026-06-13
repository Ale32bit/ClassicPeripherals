package me.alexdevs.classicPeripherals.registry.peripherals;

import me.alexdevs.classicPeripherals.platform.PeripheralRegistrar;
import me.alexdevs.classicPeripherals.registry.ModRegistry;
import me.alexdevs.classicPeripherals.registry.tiles.RfidScannerBlockEntity;
import net.minecraft.core.Direction;

public class Peripherals {
    public static void register(PeripheralRegistrar registrar) {
        registrar.register(ModRegistry.TileEntities.TOWER_BASE.get(), (block, dir) -> dir == Direction.DOWN ? block.peripheral() : null);
        registrar.register(ModRegistry.TileEntities.ANTENNA.get(), (block, dir) -> dir == Direction.DOWN ? block.peripheral() : null);
        registrar.register(ModRegistry.TileEntities.NFC_READER.get(), (block, dir) -> block.peripheral());
        registrar.register(ModRegistry.TileEntities.RFID_SCANNER.get(), RfidScannerBlockEntity::peripheral);
        registrar.register(ModRegistry.TileEntities.CRYPTOGRAPHIC_ACCELERATOR.get(), (block, dir) -> block.peripheral());
        registrar.register(ModRegistry.TileEntities.SCANNER.get(), (block, dir) -> block.peripheral());
    }
}
