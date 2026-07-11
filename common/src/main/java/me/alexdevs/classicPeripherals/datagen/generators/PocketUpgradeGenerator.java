package me.alexdevs.classicPeripherals.datagen.generators;

import dan200.computercraft.api.pocket.IPocketUpgrade;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.ModRegistry;
import me.alexdevs.classicPeripherals.peripherals.crypto.upgrades.PocketCrypto;
import me.alexdevs.classicPeripherals.peripherals.radio.upgrades.PocketRadio;
import me.alexdevs.classicPeripherals.peripherals.rfid.upgrades.PocketRfid;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.registries.RegistryPatchGenerator;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.CompletableFuture;

public class PocketUpgradeGenerator {
    public static void addUpgrades(BootstrapContext<IPocketUpgrade> upgrades) {
        upgrades.register(
                key(id("radio")),
                new PocketRadio(new ItemStack(ModRegistry.Blocks.ANTENNA.get().asItem()))
        );

        upgrades.register(
                key(id("crypto")),
                new PocketCrypto(new ItemStack(ModRegistry.Blocks.CRYPTOGRAPHIC_ACCELERATOR_SLIM.get().asItem()))
        );

        upgrades.register(
                key(id("rfid")),
                new PocketRfid(new ItemStack(ModRegistry.Blocks.RFID_SCANNER.get().asItem()))
        );
    }

    public static CompletableFuture<RegistrySetBuilder.PatchedRegistries> makeUpgradeRegistry(CompletableFuture<HolderLookup.Provider> registries) {
        return RegistryPatchGenerator.createLookup(registries, Util.make(new RegistrySetBuilder(), builder -> builder.add(IPocketUpgrade.REGISTRY, PocketUpgradeGenerator::addUpgrades)));
    }

    public static ResourceKey<IPocketUpgrade> key(ResourceLocation id) {
        return ResourceKey.create(IPocketUpgrade.REGISTRY, id);
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, path);
    }
}
