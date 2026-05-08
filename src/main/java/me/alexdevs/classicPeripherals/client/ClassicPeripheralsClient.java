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

public class ClassicPeripheralsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.TOWER_BASE, ModBlocks.TOWER_SEGMENT, ModBlocks.TOWER_HEAD, ModBlocks.ANTENNA);

        MenuScreens.register(ModScreenHandlers.SCANNER, ScannerScreen::new);

        FabricComputerCraftAPIClient.registerTurtleUpgradeModeller(ModUpgrades.TURTLE_RADIO,
                TurtleUpgradeModeller.sided(
                        model("turtle_radio_left"),
                        model("turtle_radio_right")
                )
        );

        FabricComputerCraftAPIClient.registerTurtleUpgradeModeller(ModUpgrades.TURTLE_CRYPTO,
                TurtleUpgradeModeller.sided(
                        model("turtle_crypto_left"),
                        model("turtle_crypto_right")
                )
        );

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex == 0) {
                return ItemDataHandler.getColor(stack);
            }

            return 0xFF_FFFFFF;
        }, ModItems.NFC_CARD);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex == 1) {
                return ItemDataHandler.getColor(stack);
            }

            return 0xFF_FFFFFF;
        }, ModItems.RFID_BADGE);
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, path);
    }

    private static ResourceLocation model(String path) {
        return id("block/" + path);
    }
}
