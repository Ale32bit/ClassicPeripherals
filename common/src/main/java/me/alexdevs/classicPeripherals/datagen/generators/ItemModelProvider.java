package me.alexdevs.classicPeripherals.datagen.generators;

import me.alexdevs.classicPeripherals.registry.ModRegistry;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;

public class ItemModelProvider {

    public static void addItemModels(ItemModelGenerators generators) {
        generators.generateFlatItem(ModRegistry.Items.COPPER_COIL.get(), ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(ModRegistry.Items.NFC_CARD.get(), ModelTemplates.FLAT_ITEM);

        // Two-layered item: base texture + color overlay
        ModelTemplates.TWO_LAYERED_ITEM.create(
                ModelLocationUtils.getModelLocation(ModRegistry.Items.RFID_BADGE.get()),
                TextureMapping.layered(
                        TextureMapping.getItemTexture(ModRegistry.Items.RFID_BADGE.get()),
                        TextureMapping.getItemTexture(ModRegistry.Items.RFID_BADGE.get(), "_color")
                ),
                generators.output
        );
    }
}
