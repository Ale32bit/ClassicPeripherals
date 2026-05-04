package me.alexdevs.classicPeripherals.upgrades.crypto;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import me.alexdevs.classicPeripherals.peripherals.AbstractCryptographicAcceleratorPeripheral;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class TurtleCrypto implements ITurtleUpgrade {
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

    private final ResourceLocation id;
    private final ItemStack stack;

    public TurtleCrypto(ResourceLocation id, ItemStack stack) {
        this.id = id;
        this.stack = stack;
    }

    @Override
    public TurtleUpgradeType getType() {
        return TurtleUpgradeType.PERIPHERAL;
    }

    @Override
    public @Nullable IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
        return new CryptoTurtlePeripheral(turtle);
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
