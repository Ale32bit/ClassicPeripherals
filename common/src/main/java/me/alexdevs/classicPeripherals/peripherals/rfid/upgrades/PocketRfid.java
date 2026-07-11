package me.alexdevs.classicPeripherals.peripherals.rfid.upgrades;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.AbstractPocketUpgrade;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.upgrades.UpgradeType;
import me.alexdevs.classicPeripherals.ModRegistry;
import me.alexdevs.classicPeripherals.peripherals.rfid.AbstractRfidScannerPeripheral;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class PocketRfid extends AbstractPocketUpgrade {
    public static class RfidPocketPeripheral extends AbstractRfidScannerPeripheral {
        private static final int LIGHT_COLOR = 0x00FF00;
        private final IPocketAccess pocket;
        private int previousLight = 0;

        public RfidPocketPeripheral(IPocketAccess pocket) {
            this.pocket = pocket;
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
        protected void updateState(boolean active) {
            if (active) {
                previousLight = pocket.getLight();
                pocket.setLight(LIGHT_COLOR);
            } else {
                pocket.setLight(previousLight);
            }
        }

        @Override
        public boolean equals(@Nullable IPeripheral other) {
            return this == other || (other instanceof RfidPocketPeripheral o && pocket == o.pocket);
        }
    }

    public PocketRfid(ItemStack stack) {
        super("upgrade.rfid.adjective", stack);
    }

    @Override
    public UpgradeType<? extends IPocketUpgrade> getType() {
        return ModRegistry.Upgrades.POCKET_RFID;
    }

    @Override
    public @Nullable IPeripheral createPeripheral(IPocketAccess access) {
        return new RfidPocketPeripheral(access);
    }

    @Override
    public void update(IPocketAccess pocket, @Nullable IPeripheral peripheral) {
        if (!pocket.getLevel().isClientSide) {
            if (peripheral instanceof PocketRfid.RfidPocketPeripheral scanner) {
                scanner.tick(pocket.getLevel());
            }
        }
    }
}
