package me.alexdevs.ccNetworks.client.data;

import me.alexdevs.ccNetworks.block.ModBlocks;
import me.alexdevs.ccNetworks.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class LanguageGenerator extends FabricLanguageProvider {
    public LanguageGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder builder) {
        builder.add("itemGroup.ccnetworks", "CC Networks");
        builder.add(ModBlocks.TOWER_BASE, "Radio Tower Controller");
        builder.add(ModBlocks.TOWER_SEGMENT, "Radio Tower Pole");
        builder.add(ModBlocks.TOWER_HEAD, "Radio Tower Antenna");
        builder.add(ModBlocks.ANTENNA, "Radio Antenna");
        builder.add(ModItems.COPPER_COIL, "Copper Coil");
    }
}
