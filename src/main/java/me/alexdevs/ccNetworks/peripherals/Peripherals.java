package me.alexdevs.ccNetworks.peripherals;

import dan200.computercraft.api.peripheral.PeripheralLookup;
import me.alexdevs.ccNetworks.tiles.ModBlockTiles;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class Peripherals {
    public static void register() {
        var peripherals = new BlockComponentImpl<>(PeripheralLookup.get());
        peripherals.registerForBlockEntity(ModBlockTiles.TOWER_BASE, (block, dir) -> dir == Direction.DOWN ? block.peripheral() : null);
    }

    public interface BlockComponent<T, C extends @Nullable Object> {
        <B extends BlockEntity> void registerForBlockEntity(BlockEntityType<B> blockEntityType, BiFunction<? super B, C, @Nullable T> provider);
    }

    private record BlockComponentImpl<T, C extends @Nullable Object>(
            BlockApiLookup<T, C> lookup
    ) implements BlockComponent<T, C> {
        @Override
        public <B extends BlockEntity> void registerForBlockEntity(BlockEntityType<B> blockEntityType, BiFunction<? super B, C, @Nullable T> provider) {
            lookup.registerForBlockEntity(provider, blockEntityType);
        }
    }
}
