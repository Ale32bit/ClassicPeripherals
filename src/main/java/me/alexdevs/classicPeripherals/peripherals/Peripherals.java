package me.alexdevs.classicPeripherals.peripherals;

import dan200.computercraft.api.peripheral.PeripheralCapability;
import me.alexdevs.classicPeripherals.tiles.ModBlockTiles;
import me.alexdevs.classicPeripherals.tiles.RfidScannerBlockEntity;
import net.minecraft.core.Direction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class Peripherals {
    public static void register(IEventBus modBus) {
        modBus.addListener((RegisterCapabilitiesEvent event) -> {

            event.registerBlockEntity(PeripheralCapability.get(), ModBlockTiles.TOWER_BASE.get(),
                    (block, dir) -> dir == Direction.DOWN ? block.peripheral() : null);

            event.registerBlockEntity(PeripheralCapability.get(), ModBlockTiles.ANTENNA.get(),
                    (block, dir) -> dir == Direction.DOWN ? block.peripheral() : null);

            event.registerBlockEntity(PeripheralCapability.get(), ModBlockTiles.NFC_READER.get(),
                    (block, dir) -> block.peripheral());

            event.registerBlockEntity(PeripheralCapability.get(), ModBlockTiles.RFID_SCANNER.get(), RfidScannerBlockEntity::peripheral);
        });
    }
}
