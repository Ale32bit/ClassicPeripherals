package me.alexdevs.classicPeripherals.peripherals.rfid.upgrades;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import dan200.computercraft.api.upgrades.UpgradeType;
import me.alexdevs.classicPeripherals.ModRegistry;
import me.alexdevs.classicPeripherals.peripherals.rfid.AbstractRfidScannerPeripheral;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class TurtleRfid extends AbstractTurtleUpgrade {
    public static class RfidTurtlePeripheral extends AbstractRfidScannerPeripheral {
        private final ITurtleAccess turtle;
        private final TurtleSide side;

        public RfidTurtlePeripheral(ITurtleAccess turtle, TurtleSide side) {
            this.turtle = turtle;
            this.side = side;
        }

        @Override
        public Level getLevel() {
            return turtle.getLevel();
        }

        @Override
        public Vec3 getPosition() {
            return Vec3.atLowerCornerOf(turtle.getPosition());
        }

        @Override
        public boolean equals(@Nullable IPeripheral other) {
            return this == other || (other instanceof RfidTurtlePeripheral o && turtle == o.turtle);
        }

        @Override
        protected void updateState(boolean active) {
            turtle.setUpgradeData(side, DataComponentPatch.builder().set(ModRegistry.DataComponents.ON.get(), active).build());
        }
    }

    public TurtleRfid(ItemStack stack) {
        super(TurtleUpgradeType.PERIPHERAL, "upgrade.rfid.adjective", stack);
    }

    @Override
    public UpgradeType<? extends ITurtleUpgrade> getType() {
        return ModRegistry.Upgrades.TURTLE_RFID;
    }

    @Override
    public @Nullable IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
        return new RfidTurtlePeripheral(turtle, side);
    }

    @Override
    public void update(ITurtleAccess turtle, TurtleSide side) {
        if (!turtle.getLevel().isClientSide) {
            var peripheral = turtle.getPeripheral(side);
            if (peripheral instanceof RfidTurtlePeripheral scanner) {
                scanner.tick(turtle.getLevel());
            }
        }
    }
}
