package me.alexdevs.ccNetworks.peripherals;

import dan200.computercraft.api.peripheral.PeripheralLookup;
import me.alexdevs.ccNetworks.tiles.ModBlockTiles;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class Peripherals {
    public static void register() {
        //ComputerCraftAPI.registerGenericSource(new)
        PeripheralLookup.get().registerForBlockEntity((f, s) -> new RadioTowerPeripheral(f), ModBlockTiles.TOWER_BASE);
    }
}
