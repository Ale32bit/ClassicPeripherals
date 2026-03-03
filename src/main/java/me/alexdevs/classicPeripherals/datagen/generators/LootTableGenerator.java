package me.alexdevs.classicPeripherals.datagen.generators;

import me.alexdevs.classicPeripherals.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class LootTableGenerator extends FabricBlockLootTableProvider {
    public LootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        this.dropSelf(ModBlocks.TOWER_BASE);
        this.dropSelf(ModBlocks.TOWER_SEGMENT);
        this.dropSelf(ModBlocks.TOWER_HEAD);
        this.dropSelf(ModBlocks.ANTENNA);

        this.dropSelf(ModBlocks.NFC_READER);
        this.dropSelf(ModBlocks.RFID_SCANNER);
        this.dropSelf(ModBlocks.CRYPTOGRAPHIC_ACCELERATOR);
    }
}
