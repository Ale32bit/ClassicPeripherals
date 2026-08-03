package me.alexdevs.classicPeripherals.datagen.generators;

import me.alexdevs.classicPeripherals.ModRegistry;
import me.alexdevs.classicPeripherals.platform.RegistrySupplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class TagGenerator extends TagsProvider<Block> {
    public TagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BLOCK, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(
                        key(ModRegistry.Blocks.TOWER_BASE), key(ModRegistry.Blocks.TOWER_SEGMENT),
                        key(ModRegistry.Blocks.TOWER_HEAD), key(ModRegistry.Blocks.ANTENNA),
                        key(ModRegistry.Blocks.NFC_READER), key(ModRegistry.Blocks.RFID_SCANNER),
                        key(ModRegistry.Blocks.CRYPTOGRAPHIC_ACCELERATOR), key(ModRegistry.Blocks.CRYPTOGRAPHIC_ACCELERATOR_SLIM),
                        key(ModRegistry.Blocks.SCANNER), key(ModRegistry.Blocks.SATELLITE_DISH),
                        key(ModRegistry.Blocks.SATELLITE_LAUNCHER)
                );
    }

    private static ResourceKey<Block> key(RegistrySupplier<? extends Block> supplier) {
        return ResourceKey.create(Registries.BLOCK, supplier.getId());
    }
}
