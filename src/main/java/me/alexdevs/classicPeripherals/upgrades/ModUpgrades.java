package me.alexdevs.classicPeripherals.upgrades;


import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.pocket.PocketUpgradeSerialiser;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.upgrades.radio.PocketRadio;
import me.alexdevs.classicPeripherals.upgrades.radio.TurtleRadio;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class ModUpgrades {
    public static final PocketUpgradeSerialiser<PocketRadio> POCKET_RADIO = PocketUpgradeSerialiser.simpleWithCustomItem(PocketRadio::new);

    public static final TurtleUpgradeSerialiser<TurtleRadio> TURTLE_RADIO = TurtleUpgradeSerialiser.simpleWithCustomItem(TurtleRadio::new);

    @SuppressWarnings("unchecked")
    public static void initialize() {
        var pocket = (Registry<PocketUpgradeSerialiser<?>>) BuiltInRegistries.REGISTRY.get(PocketUpgradeSerialiser.registryId().location());
        var turtle = (Registry<TurtleUpgradeSerialiser<?>>) BuiltInRegistries.REGISTRY.get(TurtleUpgradeSerialiser.registryId().location());
        Registry.register(pocket, new ResourceLocation(ClassicPeripherals.MOD_ID, "radio"), POCKET_RADIO);
        Registry.register(turtle, new ResourceLocation(ClassicPeripherals.MOD_ID, "radio"), TURTLE_RADIO);
    }
}
