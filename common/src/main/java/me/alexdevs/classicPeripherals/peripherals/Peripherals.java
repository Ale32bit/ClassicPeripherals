package me.alexdevs.classicPeripherals.peripherals;

import me.alexdevs.classicPeripherals.platform.PeripheralRegistrar;
import me.alexdevs.classicPeripherals.tiles.ModBlockTiles;
import me.alexdevs.classicPeripherals.tiles.RfidScannerBlockEntity;
import net.minecraft.core.Direction;

public class Peripherals {
    public static void register(PeripheralRegistrar registrar) {
        registrar.register(ModBlockTiles.TOWER_BASE.get(), (block, dir) -> dir == Direction.DOWN ? block.peripheral() : null);
        registrar.register(ModBlockTiles.ANTENNA.get(), (block, dir) -> dir == Direction.DOWN ? block.peripheral() : null);
        registrar.register(ModBlockTiles.NFC_READER.get(), (block, dir) -> block.peripheral());
        registrar.register(ModBlockTiles.RFID_SCANNER.get(), RfidScannerBlockEntity::peripheral);
        registrar.register(ModBlockTiles.CRYPTOGRAPHIC_ACCELERATOR.get(), (block, dir) -> block.peripheral());
        registrar.register(ModBlockTiles.SCANNER.get(), (block, dir) -> block.peripheral());
    }
}
