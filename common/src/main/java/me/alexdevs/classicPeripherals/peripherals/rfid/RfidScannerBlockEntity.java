package me.alexdevs.classicPeripherals.peripherals.rfid;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.ModRegistry;
import me.alexdevs.classicPeripherals.core.dataHolder.DataHolderHandler;
import me.alexdevs.classicPeripherals.mixinInterface.ILivingEntityMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RfidScannerBlockEntity extends BlockEntity {

    protected final RfidScannerPeripheral peripheral = new RfidScannerPeripheral(this);

    public RfidScannerBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModRegistry.TileEntities.RFID_SCANNER.get(), pos, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RfidScannerBlockEntity scanner) {
        scanner.peripheral.tick(level);
    }

    @Nullable
    public RfidScannerPeripheral peripheral(@Nullable Direction direction) {
        return direction == null || getDirection() == direction ? peripheral : null;
    }

    public Direction getDirection() {
        return getBlockState().getValue(DirectionalBlock.FACING);
    }
}
