package me.alexdevs.classicPeripherals;

import dan200.computercraft.api.peripheral.PeripheralCapability;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.upgrades.UpgradeType;
import me.alexdevs.classicPeripherals.network.S2CConfigurationPayload;
import me.alexdevs.classicPeripherals.platform.NeoForgeRegistrationHelper;
import me.alexdevs.classicPeripherals.platform.PeripheralProvider;
import me.alexdevs.classicPeripherals.platform.PeripheralRegistrar;
import me.alexdevs.classicPeripherals.platform.UpgradeRegistrar;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(ClassicPeripherals.MOD_ID)
public class ClassicPeripheralsNeoForge {
    public ClassicPeripheralsNeoForge(IEventBus modBus) {
        NeoForgeRegistrationHelper.MOD_BUS = modBus;
        ClassicPeripherals.init();

        modBus.addListener(this::registerCapabilities);
        modBus.addListener(this::registerUpgrades);
        modBus.addListener(this::onGatherData);
        modBus.addListener(this::registerPayload);

        NeoForge.EVENT_BUS.addListener(this::onServerStarted);

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

    @EventBusSubscriber(modid = ClassicPeripherals.MOD_ID)
    private static class ServerEvents {
        @SubscribeEvent
        private static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
            var player = (ServerPlayer) event.getEntity();
            player.connection.send(ClassicPeripherals.getConfigurationPayload());
        }
    }


    private void registerPayload(RegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .playToClient(
                        S2CConfigurationPayload.TYPE,
                        S2CConfigurationPayload.CODEC,
                        (payload, context) -> ClassicPeripherals.applyConfiguration(payload));
    }
}
