package me.alexdevs.classicPeripherals.datagen;

import me.alexdevs.classicPeripherals.datagen.generators.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ClassicPeripheralsDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModelGenerator::new);
        pack.addProvider(RecipeGenerator::new);
        pack.addProvider(LanguageGenerator::new);
        pack.addProvider(LootTableGenerator::new);
        pack.addProvider(TagGenerator::new);
    }
}
