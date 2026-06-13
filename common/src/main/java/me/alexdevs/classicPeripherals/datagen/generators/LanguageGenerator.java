package me.alexdevs.classicPeripherals.datagen.generators;

import com.google.gson.JsonObject;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.registry.ModRegistry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class LanguageGenerator implements DataProvider {
    private final PackOutput.PathProvider pathProvider;
    private final Map<String, String> translations = new LinkedHashMap<>();

    public LanguageGenerator(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "lang");
        addTranslations();
    }

    private void addTranslations() {
        add("itemGroup.classicperipherals", "Classic Peripherals");
        add(ModRegistry.Blocks.TOWER_BASE.get(), "Radio Tower Controller");
        add(ModRegistry.Blocks.TOWER_SEGMENT.get(), "Radio Tower Pole");
        add(ModRegistry.Blocks.TOWER_HEAD.get(), "Radio Tower Antenna");
        add(ModRegistry.Blocks.ANTENNA.get(), "Radio Antenna");
        add(ModRegistry.Items.COPPER_COIL.get(), "Copper Coil");

        add(ModRegistry.Blocks.NFC_READER.get(), "NFC Reader");
        add(ModRegistry.Items.NFC_CARD.get(), "NFC Card");
        add(ModRegistry.Items.RFID_BADGE.get(), "RFID Badge");
        add(ModRegistry.Blocks.RFID_SCANNER.get(), "RFID Scanner");
        add(ModRegistry.Blocks.CRYPTOGRAPHIC_ACCELERATOR.get(), "Cryptographic Accelerator");
        add(ModRegistry.Blocks.SCANNER.get(), "Scanner");

        add("upgrade.radio.adjective", "Radio");
        add("upgrade.crypto.adjective", "Crypto");
    }

    private void add(String key, String value) {
        translations.put(key, value);
    }

    private void add(Block block, String value) {
        translations.put(block.getDescriptionId(), value);
    }

    private void add(Item item, String value) {
        translations.put(item.getDescriptionId(), value);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        var json = new JsonObject();
        translations.forEach(json::addProperty);
        return DataProvider.saveStable(cache, json,
                pathProvider.json(ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, "en_us")));
    }

    @Override
    public String getName() {
        return "Languages";
    }
}
