package me.alexdevs.classicPeripherals.datagen.generators;

import me.alexdevs.classicPeripherals.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class TagGenerator extends FabricTagProvider<Block> {
    public TagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BLOCK, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.TOWER_BASE.get(), ModBlocks.TOWER_SEGMENT.get(), ModBlocks.TOWER_HEAD.get(), ModBlocks.ANTENNA.get())
                .add(ModBlocks.NFC_READER.get(), ModBlocks.RFID_SCANNER.get(), ModBlocks.CRYPTOGRAPHIC_ACCELERATOR.get(), ModBlocks.SCANNER.get());
    }
}
