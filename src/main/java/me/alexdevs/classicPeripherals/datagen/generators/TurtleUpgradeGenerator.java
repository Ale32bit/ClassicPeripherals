package me.alexdevs.classicPeripherals.datagen.generators;

import dan200.computercraft.api.turtle.TurtleUpgradeDataProvider;
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.upgrades.ModUpgrades;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class TurtleUpgradeGenerator extends TurtleUpgradeDataProvider {
    public TurtleUpgradeGenerator(PackOutput output) {
        super(output);
    }

    @Override
    protected void addUpgrades(Consumer<Upgrade<TurtleUpgradeSerialiser<?>>> addUpgrade) {
        simpleWithCustomItem(new ResourceLocation(ClassicPeripherals.MOD_ID, "radio"), ModUpgrades.TURTLE_RADIO, ModBlocks.ANTENNA.asItem()).add(addUpgrade);
    }
}
