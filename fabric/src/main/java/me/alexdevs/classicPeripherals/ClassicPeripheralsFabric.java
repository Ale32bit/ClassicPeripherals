package me.alexdevs.classicPeripherals;

import dan200.computercraft.api.peripheral.PeripheralLookup;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.upgrades.UpgradeType;
import me.alexdevs.classicPeripherals.peripherals.Peripherals;
import me.alexdevs.classicPeripherals.platform.PeripheralProvider;
import me.alexdevs.classicPeripherals.platform.PeripheralRegistrar;
import me.alexdevs.classicPeripherals.platform.UpgradeRegistrar;
import me.alexdevs.classicPeripherals.upgrades.ModUpgrades;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ClassicPeripheralsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ClassicPeripherals.init();

        Peripherals.register(new PeripheralRegistrar() {
            @Override
            public <B extends BlockEntity> void register(BlockEntityType<B> type, PeripheralProvider<B> provider) {
                PeripheralLookup.get().registerForBlockEntity(provider::getPeripheral, type);
            }
        });

        ModUpgrades.register(new UpgradeRegistrar() {
            @Override
            @SuppressWarnings("unchecked")
            public void registerPocketUpgrade(ResourceLocation id, UpgradeType<? extends IPocketUpgrade> type) {
                var registry = (Registry<UpgradeType<? extends IPocketUpgrade>>) BuiltInRegistries.REGISTRY.get(IPocketUpgrade.typeRegistry().location());
                Registry.register(registry, id, type);
            }

            @Override
            @SuppressWarnings("unchecked")
            public void registerTurtleUpgrade(ResourceLocation id, UpgradeType<? extends ITurtleUpgrade> type) {
                var registry = (Registry<UpgradeType<? extends ITurtleUpgrade>>) BuiltInRegistries.REGISTRY.get(ITurtleUpgrade.typeRegistry().location());
                Registry.register(registry, id, type);
            }
        });

        ServerLifecycleEvents.SERVER_STARTED.register(ClassicPeripherals::onServerStarted);
    }
}
