package me.alexdevs.classicPeripherals.upgrades.crypto;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.AbstractPocketUpgrade;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.upgrades.UpgradeType;
import me.alexdevs.classicPeripherals.peripherals.AbstractCryptographicAcceleratorPeripheral;
import me.alexdevs.classicPeripherals.upgrades.ModUpgrades;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class PocketCrypto extends AbstractPocketUpgrade {
    public static class PocketCryptoPeripheral extends AbstractCryptographicAcceleratorPeripheral {
        private final IPocketAccess pocket;

        public PocketCryptoPeripheral(IPocketAccess pocket) {
            this.pocket = pocket;
        }

        @Override
        public boolean equals(@Nullable IPeripheral other) {
            return this == other || (other instanceof PocketCrypto.PocketCryptoPeripheral o && pocket == o.pocket);
        }
    }

    public PocketCrypto(ItemStack stack) {
        super("upgrade.crypto.adjective", stack);
    }

    @Override
    public UpgradeType<? extends IPocketUpgrade> getType() {
        return ModUpgrades.POCKET_CRYPTO;
    }


    @Override
    public @Nullable IPeripheral createPeripheral(IPocketAccess access) {
        return new PocketCrypto.PocketCryptoPeripheral(access);
    }
}
