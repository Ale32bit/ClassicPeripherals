package me.alexdevs.classicPeripherals.peripherals.satellite.upgrades;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.AbstractPocketUpgrade;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.upgrades.UpgradeType;
import me.alexdevs.classicPeripherals.ModRegistry;
import me.alexdevs.classicPeripherals.core.satellite.SatelliteDevice;
import me.alexdevs.classicPeripherals.peripherals.satellite.AbstractSatelliteDishPeripheral;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class PocketSatellite extends AbstractPocketUpgrade {
    public static class Peripheral extends AbstractSatelliteDishPeripheral {
        private final IPocketAccess pocket;

        public Peripheral(IPocketAccess pocket) {
            this.pocket = pocket;
        }

        @Override
        public ServerLevel getLevel() {
            return pocket.getLevel();
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
            return pocket.getPosition();
        }

        @Override
        public boolean equals(@Nullable IPeripheral other) {
            return this == other || (other instanceof Peripheral o && pocket == o.pocket);
        }
    }

    public PocketSatellite(ItemStack stack) {
        super("upgrade.satellite.adjective", stack);
    }

    @Override
    public UpgradeType<? extends IPocketUpgrade> getType() {
        return ModRegistry.Upgrades.POCKET_SATELLITE;
    }

    @Override
    public @Nullable IPeripheral createPeripheral(IPocketAccess access) {
        return new Peripheral(access);
    }
}
