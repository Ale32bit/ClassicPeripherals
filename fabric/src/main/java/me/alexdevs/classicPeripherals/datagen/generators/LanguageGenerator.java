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
        builder.add(ModBlocks.TOWER_BASE.get(), "Radio Tower Controller");
        builder.add(ModBlocks.TOWER_SEGMENT.get(), "Radio Tower Pole");
        builder.add(ModBlocks.TOWER_HEAD.get(), "Radio Tower Antenna");
        builder.add(ModBlocks.ANTENNA.get(), "Radio Antenna");
        builder.add(ModItems.COPPER_COIL.get(), "Copper Coil");

        builder.add(ModBlocks.NFC_READER.get(), "NFC Reader");
        builder.add(ModItems.NFC_CARD.get(), "NFC Card");
        builder.add(ModItems.RFID_BADGE.get(), "RFID Badge");
        builder.add(ModBlocks.RFID_SCANNER.get(), "RFID Scanner");
        builder.add(ModBlocks.CRYPTOGRAPHIC_ACCELERATOR.get(), "Cryptographic Accelerator");
        builder.add(ModBlocks.SCANNER.get(), "Scanner");

        builder.add("upgrade.radio.adjective", "Radio");
        builder.add("upgrade.crypto.adjective", "Crypto");
    }
}
