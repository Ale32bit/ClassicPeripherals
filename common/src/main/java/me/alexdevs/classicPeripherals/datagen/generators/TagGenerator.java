package me.alexdevs.classicPeripherals.datagen.generators;

import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.platform.RegistrySupplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class TagGenerator extends TagsProvider<Block> {
    public TagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BLOCK, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(key(ModBlocks.TOWER_BASE), key(ModBlocks.TOWER_SEGMENT), key(ModBlocks.TOWER_HEAD), key(ModBlocks.ANTENNA))
                .add(key(ModBlocks.NFC_READER), key(ModBlocks.RFID_SCANNER), key(ModBlocks.CRYPTOGRAPHIC_ACCELERATOR), key(ModBlocks.SCANNER));
    }

    private static ResourceKey<Block> key(RegistrySupplier<? extends Block> supplier) {
        return ResourceKey.create(Registries.BLOCK, supplier.getId());
    }
}
