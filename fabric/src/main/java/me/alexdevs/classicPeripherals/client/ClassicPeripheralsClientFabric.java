package me.alexdevs.classicPeripherals.client;

import dan200.computercraft.api.client.FabricComputerCraftAPIClient;
import dan200.computercraft.api.client.turtle.TurtleUpgradeModeller;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.client.screen.ScannerScreen;
import me.alexdevs.classicPeripherals.core.ItemDataHandler;
import me.alexdevs.classicPeripherals.item.ModItems;
import me.alexdevs.classicPeripherals.screen.ModScreenHandlers;
import me.alexdevs.classicPeripherals.upgrades.ModUpgrades;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ClassicPeripheralsClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
                ModBlocks.TOWER_BASE.get(), ModBlocks.TOWER_SEGMENT.get(),
                ModBlocks.TOWER_HEAD.get(), ModBlocks.ANTENNA.get());

        MenuScreens.register(ModScreenHandlers.SCANNER.get(), ScannerScreen::new);

        FabricComputerCraftAPIClient.registerTurtleUpgradeModeller(ModUpgrades.TURTLE_RADIO,
                TurtleUpgradeModeller.sided(model("turtle_radio_left"), model("turtle_radio_right")));

        FabricComputerCraftAPIClient.registerTurtleUpgradeModeller(ModUpgrades.TURTLE_CRYPTO,
                TurtleUpgradeModeller.sided(model("turtle_crypto_left"), model("turtle_crypto_right")));

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex == 0) return ItemDataHandler.getColor(stack);
            return 0xFF_FFFFFF;
        }, ModItems.NFC_CARD.get());

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex == 1) return ItemDataHandler.getColor(stack);
            return 0xFF_FFFFFF;
        }, ModItems.RFID_BADGE.get());
    }

    private static ResourceLocation model(String path) {
        return ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, "block/" + path);
    }
}
