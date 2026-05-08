package me.alexdevs.classicPeripherals.datagen.generators;

import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class LanguageGenerator extends FabricLanguageProvider {

    public LanguageGenerator(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder builder) {
        builder.add("itemGroup.classicperipherals", "Classic Peripherals");
        builder.add(ModBlocks.TOWER_BASE, "Radio Tower Controller");
        builder.add(ModBlocks.TOWER_SEGMENT, "Radio Tower Pole");
        builder.add(ModBlocks.TOWER_HEAD, "Radio Tower Antenna");
        builder.add(ModBlocks.ANTENNA, "Radio Antenna");
        builder.add(ModItems.COPPER_COIL, "Copper Coil");

        builder.add(ModBlocks.NFC_READER, "NFC Reader");
        builder.add(ModItems.NFC_CARD, "NFC Card");
        builder.add(ModItems.RFID_BADGE, "RFID Badge");
        builder.add(ModBlocks.RFID_SCANNER, "RFID Scanner");
        builder.add(ModBlocks.CRYPTOGRAPHIC_ACCELERATOR, "Cryptographic Accelerator");
        builder.add(ModBlocks.SCANNER, "Scanner");

        builder.add("upgrade.radio.adjective", "Radio");
        builder.add("upgrade.crypto.adjective", "Crypto");
    }
}
