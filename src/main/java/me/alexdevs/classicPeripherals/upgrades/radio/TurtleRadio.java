package me.alexdevs.classicPeripherals.upgrades.radio;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import me.alexdevs.classicPeripherals.peripherals.AbstractRadioPeripheral;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class TurtleRadio implements ITurtleUpgrade {
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
            return false;
        }

        @Override
        public boolean equals(@Nullable IPeripheral other) {
            return this == other || (other instanceof TurtleRadio.RadioTurtlePeripheral o && turtle == o.turtle);
        }
    }

    private final ResourceLocation id;
    private final ItemStack stack;

    public TurtleRadio(ResourceLocation id, ItemStack stack) {
        this.id = id;
        this.stack = stack;
    }

    @Override
    public TurtleUpgradeType getType() {
        return TurtleUpgradeType.PERIPHERAL;
    }

    @Override
    public @Nullable IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
        return new RadioTurtlePeripheral(turtle);
    }

    @Override
    public ResourceLocation getUpgradeID() {
        return id;
    }

    @Override
    public String getUnlocalisedAdjective() {
        return "Radio";
    }

    @Override
    public ItemStack getCraftingItem() {
        return stack;
    }
}
