package me.alexdevs.classicPeripherals.upgrades;


import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.upgrades.UpgradeType;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.upgrades.radio.PocketRadio;
import me.alexdevs.classicPeripherals.upgrades.radio.TurtleRadio;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class ModUpgrades {
    public static final UpgradeType<PocketRadio> POCKET_RADIO = UpgradeType.simpleWithCustomItem(PocketRadio::new);
    public static final UpgradeType<TurtleRadio> TURTLE_RADIO = UpgradeType.simpleWithCustomItem(TurtleRadio::new);

    public static void initialize() {
        @SuppressWarnings("unchecked")
        var pocketUpgradeSerializers = (Registry<UpgradeType<? extends IPocketUpgrade>>) BuiltInRegistries.REGISTRY.get(IPocketUpgrade.typeRegistry().location());
        @SuppressWarnings("unchecked")
        var turtleUpgradeSerialisers = (Registry<UpgradeType<? extends ITurtleUpgrade>>) BuiltInRegistries.REGISTRY.get(ITurtleUpgrade.typeRegistry().location());

        Registry.register(pocketUpgradeSerializers, id("radio"), POCKET_RADIO);
        Registry.register(turtleUpgradeSerialisers, id("radio"), TURTLE_RADIO);
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, path);
    }
}
