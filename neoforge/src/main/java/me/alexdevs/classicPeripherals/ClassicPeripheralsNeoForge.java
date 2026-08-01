package me.alexdevs.classicPeripherals;

import dan200.computercraft.api.peripheral.PeripheralCapability;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.upgrades.UpgradeType;
import me.alexdevs.classicPeripherals.platform.NeoForgeRegistrationHelper;
import me.alexdevs.classicPeripherals.platform.PeripheralProvider;
import me.alexdevs.classicPeripherals.platform.PeripheralRegistrar;
import me.alexdevs.classicPeripherals.platform.UpgradeRegistrar;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(ClassicPeripherals.MOD_ID)
public class ClassicPeripheralsNeoForge {
    public ClassicPeripheralsNeoForge(IEventBus modBus) {
        NeoForgeRegistrationHelper.MOD_BUS = modBus;
        ClassicPeripherals.init();

        modBus.addListener(this::registerCapabilities);
        modBus.addListener(this::registerUpgrades);
        modBus.addListener(this::onGatherData);

        NeoForge.EVENT_BUS.addListener(this::onServerStarted);
        NeoForge.EVENT_BUS.addListener(this::onServerStopping);
        NeoForge.EVENT_BUS.addListener(this::onServerTick);
        NeoForge.EVENT_BUS.addListener(this::onLevelTick);

        if (FMLEnvironment.dist.isClient()) {
            new ClassicPeripheralsClientNeoForge(modBus);
        }
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        ModRegistry.Peripherals.register(new PeripheralRegistrar() {
            @Override
            public <B extends BlockEntity> void register(BlockEntityType<B> type, PeripheralProvider<B> provider) {
                event.registerBlockEntity(
                        PeripheralCapability.get(),
                        type,
                        provider::getPeripheral
                );
            }
        });
    }

    private void registerUpgrades(RegisterEvent event) {
        ModRegistry.Upgrades.register(new UpgradeRegistrar() {
            @Override
            public void registerPocketUpgrade(String id, UpgradeType<? extends IPocketUpgrade> type) {
                event.register(IPocketUpgrade.typeRegistry(), withPath(id), () -> type);
            }

            @Override
            public void registerTurtleUpgrade(String id, UpgradeType<? extends ITurtleUpgrade> type) {
                event.register(ITurtleUpgrade.typeRegistry(), withPath(id), () -> type);
            }
        });
    }

    private void onGatherData(GatherDataEvent event) {
        ClassicPeripheralsDataGenerator.gatherData(event);
    }

    private void onServerStarted(ServerStartedEvent event) {
        ClassicPeripherals.onServerStarted(event.getServer());
    }

    private void onServerStopping(ServerStoppingEvent event) {
        ClassicPeripherals.onServerStopping(event.getServer());
    }

    private void onServerTick(ServerTickEvent.Pre event) {
        ClassicPeripherals.tick(event.getServer());
    }

    private void onLevelTick(LevelTickEvent.Pre event) {
        ClassicPeripherals.levelTick(event.getLevel());
    }
}
