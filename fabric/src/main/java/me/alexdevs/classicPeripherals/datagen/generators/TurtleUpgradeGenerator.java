package me.alexdevs.classicPeripherals.datagen.generators;

import dan200.computercraft.api.turtle.ITurtleUpgrade;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.upgrades.crypto.TurtleCrypto;
import me.alexdevs.classicPeripherals.upgrades.radio.TurtleRadio;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.registries.RegistryPatchGenerator;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.CompletableFuture;

public class TurtleUpgradeGenerator {
    // Register our turtle upgrades.
    public static void addUpgrades(BootstrapContext<ITurtleUpgrade> upgrades) {
        upgrades.register(
                ITurtleUpgrade.createKey(id("radio")),
                new TurtleRadio(new ItemStack(ModBlocks.ANTENNA.get().asItem()))
        );

        upgrades.register(
                ITurtleUpgrade.createKey(id("crypto")),
                new TurtleCrypto(new ItemStack(ModBlocks.CRYPTOGRAPHIC_ACCELERATOR.get().asItem()))
        );
    }

    // Set up the dynamic registries to contain our turtle upgrades.
    public static CompletableFuture<RegistrySetBuilder.PatchedRegistries> makeUpgradeRegistry(CompletableFuture<HolderLookup.Provider> registries) {
        return RegistryPatchGenerator.createLookup(registries, Util.make(new RegistrySetBuilder(), builder -> {
            builder.add(ITurtleUpgrade.REGISTRY, TurtleUpgradeGenerator::addUpgrades);
        }));
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, path);
    }
}
