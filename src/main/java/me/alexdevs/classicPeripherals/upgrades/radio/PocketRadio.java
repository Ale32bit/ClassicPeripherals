package me.alexdevs.classicPeripherals.upgrades.radio;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.peripherals.AbstractRadioPeripheral;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class PocketRadio implements IPocketUpgrade {
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
            return false;
        }

        @Override
        public boolean equals(@Nullable IPeripheral other) {
            return this == other || (other instanceof RadioPocketPeripheral o && pocket == o.pocket);
        }
    }

    private final ResourceLocation id;
    private final ItemStack stack;

    public PocketRadio(ResourceLocation id, ItemStack stack) {
        this.id = id;
        this.stack = stack;
    }

    @Override
    public @Nullable IPeripheral createPeripheral(IPocketAccess pocket) {
        return new RadioPocketPeripheral(pocket);
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
