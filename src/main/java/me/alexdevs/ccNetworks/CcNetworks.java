package me.alexdevs.ccNetworks;

import me.alexdevs.ccNetworks.block.ModBlocks;
import me.alexdevs.ccNetworks.peripherals.Peripherals;
import me.alexdevs.ccNetworks.tiles.ModBlockTiles;
import net.fabricmc.api.ModInitializer;

public class CcNetworks implements ModInitializer {
    public static final String MOD_ID = "ccnetworks";

    @Override
    public void onInitialize() {
        ModBlocks.initialize();
        ModBlockTiles.initialize();
        Peripherals.register();
    }
}
