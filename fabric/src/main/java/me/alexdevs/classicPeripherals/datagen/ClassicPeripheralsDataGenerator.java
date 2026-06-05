package me.alexdevs.classicPeripherals.datagen;

import me.alexdevs.classicPeripherals.datagen.generators.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.animal.Turtle;

import java.util.concurrent.CompletableFuture;

public class ClassicPeripheralsDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModelGenerator::new);
        pack.addProvider(RecipeGenerator::new);
        pack.addProvider(LanguageGenerator::new);
        pack.addProvider(LootTableGenerator::new);
        pack.addProvider(TagGenerator::new);

        addPocketUpgrades(pack, fabricDataGenerator.getRegistries());
        addTurtleUpgrades(pack, fabricDataGenerator.getRegistries());
    }

    private static void addPocketUpgrades(FabricDataGenerator.Pack pack, CompletableFuture<HolderLookup.Provider> registries) {
        var fullRegistryPatch = PocketUpgradeGenerator.makeUpgradeRegistry(registries);
        pack.addProvider((FabricDataOutput output) -> new AutomaticDynamicRegistryGenerator("PocketRegistries", output, fullRegistryPatch));
    }

    private static void addTurtleUpgrades(FabricDataGenerator.Pack pack, CompletableFuture<HolderLookup.Provider> registries) {
        var fullRegistryPatch = TurtleUpgradeGenerator.makeUpgradeRegistry(registries);
        pack.addProvider((FabricDataOutput output) -> new AutomaticDynamicRegistryGenerator("TurtleRegistries", output, fullRegistryPatch));
    }
}
