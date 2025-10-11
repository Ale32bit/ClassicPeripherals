package me.alexdevs.classicPeripherals.tiles;

import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.block.RfidScannerBlock;
import me.alexdevs.classicPeripherals.item.ModItems;
import me.alexdevs.classicPeripherals.item.NfcCardItem;
import me.alexdevs.classicPeripherals.peripherals.RfidScannerPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RfidScannerBlockEntity extends BlockEntity {
    public static final int RANGE = 8;

    protected final RfidScannerPeripheral peripheral = new RfidScannerPeripheral(this);

    public RfidScannerBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockTiles.RFID_SCANNER.get(), pos, blockState);
    }

    @Nullable
    public RfidScannerPeripheral peripheral(@Nullable Direction direction) {
        return direction == null || getDirection() == direction ? peripheral : null;
    }

    public Direction getDirection() {
        return getBlockState().getValue(DirectionalBlock.FACING);
    }

    public void scheduleScan() {
        this.getLevel().setBlockAndUpdate(getBlockPos(), getBlockState().setValue(RfidScannerBlock.ACTIVE, true));
        level.scheduleTick(getBlockPos(), ModBlocks.RFID_SCANNER.get(), 2);
    }

    public void scan() {
        var badges = scanBadges();
        peripheral.emitScanEvent(badges);
    }

    private List<ScannedRfidBadge> scanBadges() {
        var badges = new ArrayList<ScannedRfidBadge>();

        var level = getLevel();
        var origin = peripheral.getPosition();

        if(level.isClientSide)
            return List.of();

        var nearbyPlayers = level.players().stream()
                .filter(player -> player.position().distanceToSqr(origin) <= RANGE * RANGE)
                .map(player -> (ServerPlayer)player)
                .toList();

        for (var player : nearbyPlayers) {
            var inventory = player.getInventory();
            for(var stack : inventory.items) {
                if(!stack.is(ModItems.RFID_BADGE)) {
                    continue;
                }

                var data = NfcCardItem.getData(stack);
                if(data.isEmpty()) {
                    continue;
                }

                badges.add(new ScannedRfidBadge(data.get(), player.position().distanceTo(origin)));
            }
        }

        return badges;
    }

    public static record ScannedRfidBadge(String data, double distance) {

    }
}
