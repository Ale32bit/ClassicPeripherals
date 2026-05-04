package me.alexdevs.classicPeripherals.upgrades.crypto;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import me.alexdevs.classicPeripherals.peripherals.AbstractCryptographicAcceleratorPeripheral;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class PocketCrypto implements IPocketUpgrade {
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

    private final ResourceLocation id;
    private final ItemStack stack;

    public PocketCrypto(ResourceLocation id, ItemStack stack) {
        this.id = id;
        this.stack = stack;
    }

    @Override
    public @Nullable IPeripheral createPeripheral(IPocketAccess pocket) {
        return new PocketCryptoPeripheral(pocket);
    }

    @Override
    public ResourceLocation getUpgradeID() {
        return id;
    }

    @Override
    public String getUnlocalisedAdjective() {
        return "upgrade.crypto.adjective";
    }

    @Override
    public ItemStack getCraftingItem() {
        return stack;
    }
}
