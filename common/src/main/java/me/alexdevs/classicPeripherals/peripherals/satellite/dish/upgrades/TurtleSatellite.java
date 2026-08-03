package me.alexdevs.classicPeripherals.peripherals.satellite.dish.upgrades;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import dan200.computercraft.api.upgrades.UpgradeType;
import me.alexdevs.classicPeripherals.ModRegistry;
import me.alexdevs.classicPeripherals.core.satellite.SatelliteDevice;
import me.alexdevs.classicPeripherals.peripherals.satellite.dish.AbstractSatelliteDishPeripheral;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class TurtleSatellite extends AbstractTurtleUpgrade {
    public static class Peripheral extends AbstractSatelliteDishPeripheral {
        private final ITurtleAccess turtle;
        private final TurtleSide side;

        public Peripheral(ITurtleAccess turtle, TurtleSide side) {
            this.turtle = turtle;
            this.side = side;
        }

        @Override
        public ServerLevel getLevel() {
            return (ServerLevel) turtle.getLevel();
        }

        @Override
        public void onDataReceived(String data, SatelliteDevice source) {

        }

        @Override
        public int getRange() {
            return 0;
        }

        @Override
        public Vec3 getPosition() {
            return Vec3.atLowerCornerOf(turtle.getPosition());
        }

        @Override
        public boolean equals(@Nullable IPeripheral other) {
            return this == other || (other instanceof Peripheral o && turtle == o.turtle);
        }
    }

    public TurtleSatellite(ItemStack stack) {
        super(TurtleUpgradeType.PERIPHERAL, "upgrade.satellite.adjective", stack);
    }

    @Override
    public UpgradeType<? extends ITurtleUpgrade> getType() {
        return ModRegistry.Upgrades.TURTLE_SATELLITE;
    }

    @Override
    public @Nullable IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
        return new Peripheral(turtle, side);
    }
}
