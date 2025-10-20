package me.alexdevs.classicPeripherals.upgrades.radio;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.AbstractPocketUpgrade;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.upgrades.UpgradeType;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.peripherals.AbstractRadioPeripheral;
import me.alexdevs.classicPeripherals.upgrades.ModUpgrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class PocketRadio extends AbstractPocketUpgrade {
    public static class RadioPocketPeripheral extends AbstractRadioPeripheral {
        private final IPocketAccess pocket;

        public RadioPocketPeripheral(IPocketAccess pocket) {
            this.pocket = pocket;
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public Level getLevel() {
            return pocket.getLevel();
        }

        @Override
        public Vec3 getPosition() {
            return pocket.getPosition();
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
            return this == other || (other instanceof RadioPocketPeripheral o && pocket == o.pocket);
        }
    }

    public PocketRadio(ItemStack stack) {
        super("radio", stack);
    }

    @Override
    public UpgradeType<? extends IPocketUpgrade> getType() {
        return ModUpgrades.POCKET_RADIO;
    }


    @Override
    public @Nullable IPeripheral createPeripheral(IPocketAccess access) {
        return new PocketRadio.RadioPocketPeripheral(access);
    }
}
