package me.alexdevs.classicPeripherals;

import dan200.computercraft.api.client.turtle.RegisterTurtleModellersEvent;
import dan200.computercraft.api.client.turtle.TurtleUpgradeModeller;
import me.alexdevs.classicPeripherals.core.ItemDataHandler;
import me.alexdevs.classicPeripherals.registry.ModRegistry;
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
            ItemBlockRenderTypes.setRenderLayer(ModRegistry.Blocks.TOWER_BASE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModRegistry.Blocks.TOWER_SEGMENT.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModRegistry.Blocks.TOWER_HEAD.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModRegistry.Blocks.ANTENNA.get(), RenderType.cutout());
        });
    }

    private void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModRegistry.Screens.SCANNER.get(), ScannerScreen::new);
    }

    private void onRegisterTurtleModellers(RegisterTurtleModellersEvent event) {
        event.register(ModRegistry.Upgrades.TURTLE_RADIO,
                TurtleUpgradeModeller.sided(model("turtle_radio_left"), model("turtle_radio_right")));
        event.register(ModRegistry.Upgrades.TURTLE_CRYPTO,
                TurtleUpgradeModeller.sided(model("turtle_crypto_left"), model("turtle_crypto_right")));
    }

    private void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> tintIndex == 0 ? ItemDataHandler.getColor(stack) : 0xFF_FFFFFF,
                ModRegistry.Items.NFC_CARD.get());
        event.register((stack, tintIndex) -> tintIndex == 1 ? ItemDataHandler.getColor(stack) : 0xFF_FFFFFF,
                ModRegistry.Items.RFID_BADGE.get());
    }

    private static ResourceLocation model(String path) {
        return ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, "block/" + path);
    }
}
