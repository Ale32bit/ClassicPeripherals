package me.alexdevs.classicPeripherals;

import dan200.computercraft.api.client.turtle.RegisterTurtleModellersEvent;
import dan200.computercraft.api.client.turtle.TurtleUpgradeModeller;
import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.core.ItemDataHandler;
import me.alexdevs.classicPeripherals.item.ModItems;
import me.alexdevs.classicPeripherals.screen.ModScreenHandlers;
import me.alexdevs.classicPeripherals.upgrades.ModUpgrades;
import me.alexdevs.classicPeripherals.client.screen.ScannerScreen;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class ClassicPeripheralsClientNeoForge {
    public ClassicPeripheralsClientNeoForge(IEventBus modBus) {
        modBus.addListener(this::onClientSetup);
        modBus.addListener(this::onRegisterMenuScreens);
        modBus.addListener(this::onRegisterTurtleModellers);
        modBus.addListener(this::onRegisterItemColors);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.TOWER_BASE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.TOWER_SEGMENT.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.TOWER_HEAD.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.ANTENNA.get(), RenderType.cutout());
        });
    }

    private void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModScreenHandlers.SCANNER.get(), ScannerScreen::new);
    }

    private void onRegisterTurtleModellers(RegisterTurtleModellersEvent event) {
        event.register(ModUpgrades.TURTLE_RADIO,
                TurtleUpgradeModeller.sided(model("turtle_radio_left"), model("turtle_radio_right")));
        event.register(ModUpgrades.TURTLE_CRYPTO,
                TurtleUpgradeModeller.sided(model("turtle_crypto_left"), model("turtle_crypto_right")));
    }

    private void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> tintIndex == 0 ? ItemDataHandler.getColor(stack) : 0xFF_FFFFFF,
                ModItems.NFC_CARD.get());
        event.register((stack, tintIndex) -> tintIndex == 1 ? ItemDataHandler.getColor(stack) : 0xFF_FFFFFF,
                ModItems.RFID_BADGE.get());
    }

    private static ResourceLocation model(String path) {
        return ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, "block/" + path);
    }
}
