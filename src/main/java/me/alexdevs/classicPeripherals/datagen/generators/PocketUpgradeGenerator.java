package me.alexdevs.classicPeripherals.datagen.generators;

import dan200.computercraft.api.pocket.PocketUpgradeDataProvider;
import dan200.computercraft.api.pocket.PocketUpgradeSerialiser;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.upgrades.ModUpgrades;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class PocketUpgradeGenerator extends PocketUpgradeDataProvider {
    public PocketUpgradeGenerator(PackOutput output) {
        super(output);
    }

    @Override
    protected void addUpgrades(Consumer<Upgrade<PocketUpgradeSerialiser<?>>> addUpgrade) {
        simpleWithCustomItem(new ResourceLocation(ClassicPeripherals.MOD_ID, "radio"), ModUpgrades.POCKET_RADIO, ModBlocks.ANTENNA.asItem()).add(addUpgrade);
    }

}
