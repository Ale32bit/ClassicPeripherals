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
        this.dropSelf(ModBlocks.TOWER_BASE.get());
        this.dropSelf(ModBlocks.TOWER_SEGMENT.get());
        this.dropSelf(ModBlocks.TOWER_HEAD.get());
        this.dropSelf(ModBlocks.ANTENNA.get());

        this.dropSelf(ModBlocks.NFC_READER.get());
        this.dropSelf(ModBlocks.RFID_SCANNER.get());
        this.dropSelf(ModBlocks.CRYPTOGRAPHIC_ACCELERATOR.get());
        this.dropSelf(ModBlocks.SCANNER.get());
    }
}
