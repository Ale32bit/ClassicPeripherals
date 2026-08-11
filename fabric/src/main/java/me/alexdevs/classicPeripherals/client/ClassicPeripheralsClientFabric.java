package me.alexdevs.classicPeripherals.client;

import dan200.computercraft.api.client.FabricComputerCraftAPIClient;
import dan200.computercraft.api.client.turtle.TurtleUpgradeModeller;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.ModRegistry;
import me.alexdevs.classicPeripherals.peripherals.scanner.screen.ScannerScreen;
import me.alexdevs.classicPeripherals.core.dataHolder.DataHolderHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ClassicPeripheralsClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
                ModRegistry.Blocks.TOWER_BASE.get(), ModRegistry.Blocks.TOWER_SEGMENT.get(),
                ModRegistry.Blocks.TOWER_HEAD.get(), ModRegistry.Blocks.ANTENNA.get());

        MenuScreens.register(ModRegistry.Screens.SCANNER.get(), ScannerScreen::new);

        FabricComputerCraftAPIClient.registerTurtleUpgradeModeller(ModRegistry.Upgrades.TURTLE_RADIO,
                TurtleUpgradeModeller.sided(model("turtle_radio_left"), model("turtle_radio_right")));

        FabricComputerCraftAPIClient.registerTurtleUpgradeModeller(ModRegistry.Upgrades.TURTLE_CRYPTO,
                TurtleUpgradeModeller.sided(model("turtle_crypto_left"), model("turtle_crypto_right")));

        FabricComputerCraftAPIClient.registerTurtleUpgradeModeller(ModRegistry.Upgrades.TURTLE_RFID, new TurtleRfidModeller());

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex == 0) return DataHolderHandler.getColor(stack);
            return 0xFF_FFFFFF;
        }, ModRegistry.Items.NFC_CARD.get());

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex == 1) return DataHolderHandler.getColor(stack);
            return 0xFF_FFFFFF;
        }, ModRegistry.Items.RFID_BADGE.get());

        ItemTooltipCallback.EVENT.register(ClassicPeripherals::onTooltip);
    }

    private static ResourceLocation model(String path) {
        return ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, "block/" + path);
    }
}
