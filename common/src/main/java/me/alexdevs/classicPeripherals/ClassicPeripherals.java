package me.alexdevs.classicPeripherals;

import me.alexdevs.classicPeripherals.core.RadioNetwork;
import me.alexdevs.classicPeripherals.core.satellite.SatelliteNetwork;
import me.alexdevs.classicPeripherals.core.StateSaverAndLoader;
import me.alexdevs.classicPeripherals.peripherals.nfc.luaApi.PocketNfcAccess;
import me.alexdevs.classicPeripherals.platform.Registrar;
import me.alexdevs.classicPeripherals.platform.RegistrySupplier;
import me.alexdevs.classicPeripherals.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ClassicPeripherals {
    public static final String MOD_ID = "classicperipherals";

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
                    .icon(() -> ModRegistry.Blocks.TOWER_HEAD.get().asItem().getDefaultInstance())
                    .displayItems((parameters, entries) -> {
                        entries.accept(ModRegistry.Blocks.TOWER_BASE.get());
                        entries.accept(ModRegistry.Blocks.TOWER_SEGMENT.get());
                        entries.accept(ModRegistry.Blocks.TOWER_HEAD.get());
                        entries.accept(ModRegistry.Blocks.ANTENNA.get());
                        entries.accept(ModRegistry.Items.COPPER_COIL.get());
                        entries.accept(ModRegistry.Blocks.NFC_READER.get());
                        entries.accept(ModRegistry.Items.NFC_CARD.get());
                        entries.accept(ModRegistry.Blocks.RFID_SCANNER.get());
                        entries.accept(ModRegistry.Items.RFID_BADGE.get());
                        entries.accept(ModRegistry.Blocks.CRYPTOGRAPHIC_ACCELERATOR.get());
                        entries.accept(ModRegistry.Blocks.CRYPTOGRAPHIC_ACCELERATOR_SLIM.get());
                        entries.accept(ModRegistry.Blocks.SCANNER.get());
                        entries.accept(ModRegistry.Blocks.SATELLITE_DISH.get());
                    })
                    .build());

    private static @Nullable StateSaverAndLoader stateSaverAndLoader;
    private static @Nullable RadioNetwork radioNetwork;
    private static @Nullable SatelliteNetwork satelliteNetwork;

    public static void init() {
        ModRegistry.initialize();
    }

    public static void onServerStarted(MinecraftServer server) {
        PocketNfcAccess.clear();
        stateSaverAndLoader = StateSaverAndLoader.getServerState(server);
        radioNetwork = new RadioNetwork();
        satelliteNetwork = new SatelliteNetwork(server);

        //satelliteNetwork.addSatellite(new Satellite(satelliteNetwork, UUID.randomUUID(), BlockPos.ZERO.atY(3000), server.overworld(), Satellite.RuntimeType.gps));
    }

    public static void onServerStopped(MinecraftServer server) {
        radioNetwork = null;
        satelliteNetwork = null;
    }

    public static @Nullable StateSaverAndLoader getState() {
        return stateSaverAndLoader;
    }

    public static @Nullable RadioNetwork getRadioNetwork() {
        return radioNetwork;
    }

    public static @Nullable SatelliteNetwork getSatelliteNetwork() {
        return satelliteNetwork;
    }

    public static void tick(MinecraftServer server) {
    }

    public static void levelTick(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            if (satelliteNetwork != null) {
                satelliteNetwork.tick(serverLevel);
            }
        }
    }
}
