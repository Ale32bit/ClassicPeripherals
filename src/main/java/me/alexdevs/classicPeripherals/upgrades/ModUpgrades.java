package me.alexdevs.classicPeripherals.upgrades;


import dan200.computercraft.api.pocket.PocketUpgradeSerialiser;
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser;
import me.alexdevs.classicPeripherals.upgrades.radio.PocketRadio;
import me.alexdevs.classicPeripherals.upgrades.radio.TurtleRadio;

public class ModUpgrades {
    public static final PocketUpgradeSerialiser<PocketRadio> POCKET_RADIO = PocketUpgradeSerialiser.simpleWithCustomItem(PocketRadio::new);

    public static final TurtleUpgradeSerialiser<TurtleRadio> TURTLE_RADIO = TurtleUpgradeSerialiser.simpleWithCustomItem(TurtleRadio::new);
}
