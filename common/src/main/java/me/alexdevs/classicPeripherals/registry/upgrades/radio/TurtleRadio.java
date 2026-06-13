package me.alexdevs.classicPeripherals.registry.upgrades.radio;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import dan200.computercraft.api.upgrades.UpgradeType;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.registry.ModRegistry;
import me.alexdevs.classicPeripherals.registry.peripherals.AbstractRadioPeripheral;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class TurtleRadio extends AbstractTurtleUpgrade {
    public static class RadioTurtlePeripheral extends AbstractRadioPeripheral {
        private final ITurtleAccess turtle;

        public RadioTurtlePeripheral(ITurtleAccess turtle) {
            this.turtle = turtle;
        }

        @Override
        public boolean isValid() {
            return true;
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
        public double getRange() {
            return 0;
        }

        @Override
        public void ping() {

        }

        @Override
        public boolean canBroadcast() {
            return ClassicPeripherals.CONFIG.antennaCanBroadcast;
        }

        @Override
        public boolean equals(@Nullable IPeripheral other) {
            return this == other || (other instanceof TurtleRadio.RadioTurtlePeripheral o && turtle == o.turtle);
        }
    }

    public TurtleRadio(ItemStack stack) {
        super(TurtleUpgradeType.PERIPHERAL, "upgrade.radio.adjective", stack);
    }

    @Override
    public UpgradeType<? extends ITurtleUpgrade> getType() {
        return ModRegistry.Upgrades.TURTLE_RADIO;
    }

    @Override
    public @Nullable IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
        return new RadioTurtlePeripheral(turtle);
    }
}
