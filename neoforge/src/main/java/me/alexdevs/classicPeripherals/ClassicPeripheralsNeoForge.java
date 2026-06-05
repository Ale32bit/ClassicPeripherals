package me.alexdevs.classicPeripherals;

import dan200.computercraft.api.peripheral.PeripheralCapability;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.upgrades.UpgradeType;
import me.alexdevs.classicPeripherals.peripherals.Peripherals;
import me.alexdevs.classicPeripherals.platform.NeoForgeRegistrationHelper;
import me.alexdevs.classicPeripherals.platform.PeripheralProvider;
import me.alexdevs.classicPeripherals.platform.PeripheralRegistrar;
import me.alexdevs.classicPeripherals.platform.UpgradeRegistrar;
import me.alexdevs.classicPeripherals.upgrades.ModUpgrades;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(ClassicPeripherals.MOD_ID)
public class ClassicPeripheralsNeoForge {
    public ClassicPeripheralsNeoForge(IEventBus modBus) {
        NeoForgeRegistrationHelper.MOD_BUS = modBus;
        ClassicPeripherals.init();

        modBus.addListener(this::registerCapabilities);
        modBus.addListener(this::registerUpgrades);

        NeoForge.EVENT_BUS.addListener(this::onServerStarted);

        if (FMLEnvironment.dist.isClient()) {
            new ClassicPeripheralsClientNeoForge(modBus);
        }
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        Peripherals.register(new PeripheralRegistrar() {
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
        ModUpgrades.register(new UpgradeRegistrar() {
            @Override
            public void registerPocketUpgrade(ResourceLocation id, UpgradeType<? extends IPocketUpgrade> type) {
                event.register(IPocketUpgrade.typeRegistry(), id, () -> type);
            }

            @Override
            public void registerTurtleUpgrade(ResourceLocation id, UpgradeType<? extends ITurtleUpgrade> type) {
                event.register(ITurtleUpgrade.typeRegistry(), id, () -> type);
            }
        });
    }

    private void onServerStarted(ServerStartedEvent event) {
        ClassicPeripherals.onServerStarted(event.getServer());
    }
}
