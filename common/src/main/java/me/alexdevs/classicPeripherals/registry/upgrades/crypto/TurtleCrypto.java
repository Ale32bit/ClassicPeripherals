package me.alexdevs.classicPeripherals.registry.upgrades.crypto;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import dan200.computercraft.api.upgrades.UpgradeType;
import me.alexdevs.classicPeripherals.registry.ModRegistry;
import me.alexdevs.classicPeripherals.registry.peripherals.AbstractCryptographicAcceleratorPeripheral;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class TurtleCrypto extends AbstractTurtleUpgrade {
    public static class CryptoTurtlePeripheral extends AbstractCryptographicAcceleratorPeripheral {
        private final ITurtleAccess turtle;

        public CryptoTurtlePeripheral(ITurtleAccess pocket) {
            this.turtle = pocket;
        }

        @Override
        public boolean equals(@Nullable IPeripheral other) {
            return this == other || (other instanceof CryptoTurtlePeripheral o && turtle == o.turtle);
        }
    }

    public TurtleCrypto(ItemStack stack) {
        super(TurtleUpgradeType.PERIPHERAL, "upgrade.crypto.adjective", stack);
    }

    @Override
    public UpgradeType<? extends ITurtleUpgrade> getType() {
        return ModRegistry.Upgrades.TURTLE_CRYPTO;
    }

    @Override
    public @Nullable IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
        return new TurtleCrypto.CryptoTurtlePeripheral(turtle);
    }
}
