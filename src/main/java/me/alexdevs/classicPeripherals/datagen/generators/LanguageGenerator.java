package me.alexdevs.classicPeripherals.datagen.generators;

import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class LanguageGenerator extends FabricLanguageProvider {
    public LanguageGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder builder) {
        builder.add("itemGroup.classicperipherals", "Classic Peripherals");
        builder.add(ModBlocks.TOWER_BASE, "Radio Tower Controller");
        builder.add(ModBlocks.TOWER_SEGMENT, "Radio Tower Pole");
        builder.add(ModBlocks.TOWER_HEAD, "Radio Tower Antenna");
        builder.add(ModBlocks.ANTENNA, "Radio Antenna");
        builder.add(ModItems.COPPER_COIL, "Copper Coil");

        builder.add(ModBlocks.NFC_READER, "NFC Reader");
        builder.add(ModItems.NFC_CARD, "NFC Card");
    }
}
