package me.alexdevs.classicPeripherals;

import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.core.StateSaverAndLoader;
import me.alexdevs.classicPeripherals.item.ModItems;
import me.alexdevs.classicPeripherals.luaApi.ModLuaApiProvider;
import me.alexdevs.classicPeripherals.luaApi.PocketNfcAccess;
import me.alexdevs.classicPeripherals.platform.Registrar;
import me.alexdevs.classicPeripherals.platform.RegistrySupplier;
import me.alexdevs.classicPeripherals.platform.Services;
import me.alexdevs.classicPeripherals.recipe.ModRecipes;
import me.alexdevs.classicPeripherals.screen.ModScreenHandlers;
import me.alexdevs.classicPeripherals.tiles.ModBlockTiles;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassicPeripherals {
    public static final String MOD_ID = "classicperipherals";
    public static final Logger LOG = LoggerFactory.getLogger("Classic Peripherals");

    public static final ClassicPeripheralsConfig CONFIG = ClassicPeripheralsConfig.createToml(
            Services.PLATFORM.getConfigDir(),
            "",
            MOD_ID,
            ClassicPeripheralsConfig.class
    );

    private static final Registrar<CreativeModeTab> CREATIVE_TABS = Services.REGISTRATION.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final RegistrySupplier<CreativeModeTab> CREATIVE_TAB = CREATIVE_TABS.register("classicperipherals",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup.classicperipherals"))
                    .icon(() -> ModBlocks.TOWER_HEAD.get().asItem().getDefaultInstance())
                    .displayItems((parameters, entries) -> {
                        entries.accept(ModBlocks.TOWER_BASE.get());
                        entries.accept(ModBlocks.TOWER_SEGMENT.get());
                        entries.accept(ModBlocks.TOWER_HEAD.get());
                        entries.accept(ModBlocks.ANTENNA.get());
                        entries.accept(ModItems.COPPER_COIL.get());
                        entries.accept(ModBlocks.NFC_READER.get());
                        entries.accept(ModItems.NFC_CARD.get());
                        entries.accept(ModBlocks.RFID_SCANNER.get());
                        entries.accept(ModItems.RFID_BADGE.get());
                        entries.accept(ModBlocks.CRYPTOGRAPHIC_ACCELERATOR.get());
                        entries.accept(ModBlocks.SCANNER.get());
                    })
                    .build());

    private static @Nullable StateSaverAndLoader stateSaverAndLoader;

    public static void init() {
        ModComponents.initialize();
        ModBlocks.initialize();
        ModItems.initialize();
        ModBlockTiles.initialize();
        ModRecipes.initialize();
        ModScreenHandlers.initialize();
        ModLuaApiProvider.initialize();
    }

    public static void onServerStarted(MinecraftServer server) {
        PocketNfcAccess.clear();
        stateSaverAndLoader = StateSaverAndLoader.getServerState(server);
    }

    public static @Nullable StateSaverAndLoader getState() {
        return stateSaverAndLoader;
    }
}
