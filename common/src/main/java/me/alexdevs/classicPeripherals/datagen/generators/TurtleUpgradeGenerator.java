package me.alexdevs.classicPeripherals.datagen.generators;

import dan200.computercraft.api.turtle.ITurtleUpgrade;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.ModRegistry;
import me.alexdevs.classicPeripherals.peripherals.crypto.upgrades.TurtleCrypto;
import me.alexdevs.classicPeripherals.peripherals.radio.upgrades.TurtleRadio;
import me.alexdevs.classicPeripherals.peripherals.rfid.upgrades.TurtleRfid;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.registries.RegistryPatchGenerator;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.CompletableFuture;

public class TurtleUpgradeGenerator {
    // Register our turtle upgrades.
    public static void addUpgrades(BootstrapContext<ITurtleUpgrade> upgrades) {
        upgrades.register(
                key(id("radio")),
                new TurtleRadio(new ItemStack(ModRegistry.Blocks.ANTENNA.get().asItem()))
        );

        upgrades.register(
                key(id("crypto")),
                new TurtleCrypto(new ItemStack(ModRegistry.Blocks.CRYPTOGRAPHIC_ACCELERATOR_SLIM.get().asItem()))
        );

        upgrades.register(
                key(id("rfid")),
                new TurtleRfid(new ItemStack(ModRegistry.Blocks.RFID_SCANNER.get().asItem()))
        );
    }

    // Set up the dynamic registries to contain our turtle upgrades.
    public static CompletableFuture<RegistrySetBuilder.PatchedRegistries> makeUpgradeRegistry(CompletableFuture<HolderLookup.Provider> registries) {
        return RegistryPatchGenerator.createLookup(registries, Util.make(new RegistrySetBuilder(), builder -> builder.add(ITurtleUpgrade.REGISTRY, TurtleUpgradeGenerator::addUpgrades)));
    }

    private static ResourceKey<ITurtleUpgrade> key(ResourceLocation id) {
        return ITurtleUpgrade.createKey(id);
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, path);
    }
}
