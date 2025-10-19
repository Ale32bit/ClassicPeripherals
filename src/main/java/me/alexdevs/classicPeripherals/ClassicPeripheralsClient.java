package me.alexdevs.classicPeripherals;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.ForgeComputerCraftAPI;
import dan200.computercraft.api.client.turtle.RegisterTurtleModellersEvent;
import dan200.computercraft.api.client.turtle.TurtleUpgradeModeller;
import dan200.computercraft.core.computer.Computer;
import me.alexdevs.classicPeripherals.item.ModItems;
import me.alexdevs.classicPeripherals.upgrades.ModUpgrades;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterNamedRenderTypesEvent;

@Mod(value = ClassicPeripherals.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = ClassicPeripherals.MOD_ID, value = Dist.CLIENT)
public class ClassicPeripheralsClient {

    public void onInitializeClient() {
        //BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.TOWER_BASE, ModBlocks.TOWER_SEGMENT, ModBlocks.TOWER_HEAD, ModBlocks.ANTENNA);
    }

    public ClassicPeripheralsClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        //container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    static void onItemColorHandler(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            if (tintIndex == 0) {
                var components = stack.getComponents();
                if (components.has(ModComponents.NFC_COLOR.get())) {
                    return 0xFF_000000 | components.getOrDefault(ModComponents.NFC_COLOR.get(), 0xFFFFFF);
                }
            }

            return 0xFF_FFFFFF;
        }, ModItems.NFC_CARD.get());

        event.register((stack, tintIndex) -> {
            if (tintIndex == 1) {
                var components = stack.getComponents();
                if (components.has(ModComponents.NFC_COLOR.get())) {
                    return 0xFF_000000 | components.getOrDefault(ModComponents.NFC_COLOR.get(), 0xFFFFFF);
                }
            }

            return 0xFF_FFFFFF;
        }, ModItems.RFID_BADGE.get());
    }

    @SubscribeEvent
    public static void onUpgradeModeller(RegisterTurtleModellersEvent event) {
        event.register(ModUpgrades.TURTLE_RADIO, TurtleUpgradeModeller.sided(
                model("turtle_radio_left"),
                model("turtle_radio_right")
        ));
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, path);
    }

    private static ResourceLocation model(String path) {
        return id("block/" + path);
    }
}
