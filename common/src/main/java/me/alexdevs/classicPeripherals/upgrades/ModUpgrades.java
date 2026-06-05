package me.alexdevs.classicPeripherals.upgrades;

import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.upgrades.UpgradeType;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.platform.UpgradeRegistrar;
import me.alexdevs.classicPeripherals.upgrades.crypto.PocketCrypto;
import me.alexdevs.classicPeripherals.upgrades.crypto.TurtleCrypto;
import me.alexdevs.classicPeripherals.upgrades.radio.PocketRadio;
import me.alexdevs.classicPeripherals.upgrades.radio.TurtleRadio;
import net.minecraft.resources.ResourceLocation;

public class ModUpgrades {
    public static final UpgradeType<PocketRadio> POCKET_RADIO = UpgradeType.simpleWithCustomItem(PocketRadio::new);
    public static final UpgradeType<TurtleRadio> TURTLE_RADIO = UpgradeType.simpleWithCustomItem(TurtleRadio::new);

    public static final UpgradeType<PocketCrypto> POCKET_CRYPTO = UpgradeType.simpleWithCustomItem(PocketCrypto::new);
    public static final UpgradeType<TurtleCrypto> TURTLE_CRYPTO = UpgradeType.simpleWithCustomItem(TurtleCrypto::new);

    public static void register(UpgradeRegistrar registrar) {
        registrar.registerPocketUpgrade(id("radio"), POCKET_RADIO);
        registrar.registerTurtleUpgrade(id("radio"), TURTLE_RADIO);
        registrar.registerPocketUpgrade(id("crypto"), POCKET_CRYPTO);
        registrar.registerTurtleUpgrade(id("crypto"), TURTLE_CRYPTO);
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, path);
    }
}
