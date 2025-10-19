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
    public static final PocketUpgradeSerialiser<PocketRadio> POCKET_RADIO =
            pocket("radio", PocketUpgradeSerialiser.simpleWithCustomItem(PocketRadio::new));

    public static final TurtleUpgradeSerialiser<TurtleRadio> TURTLE_RADIO =
            turtle("radio", TurtleUpgradeSerialiser.simpleWithCustomItem(TurtleRadio::new));

    @SuppressWarnings("unchecked")
    public static <T extends IPocketUpgrade> PocketUpgradeSerialiser<T> pocket(String name, PocketUpgradeSerialiser<T> upgrade) {
        var id = new ResourceLocation(ClassicPeripherals.MOD_ID, name);
        var registry = (Registry<? super PocketUpgradeSerialiser<?>>) BuiltInRegistries.REGISTRY.get(PocketUpgradeSerialiser.registryId().location());
        if(registry == null) {
            throw new IllegalStateException("ComputerCraft has not yet initialized!");
        }

        return Registry.register(registry, id, upgrade);
    }

    @SuppressWarnings("unchecked")
    public static <T extends ITurtleUpgrade> TurtleUpgradeSerialiser<T> turtle(String name, TurtleUpgradeSerialiser<T> upgrade) {
        var id = new ResourceLocation(ClassicPeripherals.MOD_ID, name);
        var registry = (Registry<? super TurtleUpgradeSerialiser<?>>) BuiltInRegistries.REGISTRY.get(TurtleUpgradeSerialiser.registryId().location());
        if(registry == null) {
            throw new IllegalStateException("ComputerCraft has not yet initialized!");
        }

        return Registry.register(registry, id, upgrade);
    }

    public static void initialize() {
    }
}
