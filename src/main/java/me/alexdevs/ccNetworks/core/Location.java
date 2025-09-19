package me.alexdevs.ccNetworks.core;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public record Location(BlockPos pos, Level level) {

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;

        if(obj instanceof Location other) {
            return this.pos.equals(other.pos) && this.level.equals(other.level);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return pos.hashCode() + level.hashCode();
    }

    @Override
    public @NotNull String toString() {
        return "Location{" +
                "pos=" + pos +
                ", level=" + level +
                '}';
    }
}
