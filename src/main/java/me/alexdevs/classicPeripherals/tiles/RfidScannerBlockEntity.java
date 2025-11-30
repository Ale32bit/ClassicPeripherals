package me.alexdevs.classicPeripherals.tiles;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.block.RfidScannerBlock;
import me.alexdevs.classicPeripherals.item.ModItems;
import me.alexdevs.classicPeripherals.item.NfcCardItem;
import me.alexdevs.classicPeripherals.peripherals.RfidScannerPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RfidScannerBlockEntity extends BlockEntity {
    public static final int SCAN_TIME = 4;

    protected final RfidScannerPeripheral peripheral = new RfidScannerPeripheral(this);
    private long scanAt = 0;
    private boolean scheduleScan = false;
    private boolean startedScan = false;

    public RfidScannerBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockTiles.RFID_SCANNER, pos, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RfidScannerBlockEntity scanner) {
        if (level.isClientSide) {
            return;
        }

        if (!scanner.scheduleScan) {
            return;
        }

        if (!scanner.startedScan) {
            scanner.getLevel().setBlockAndUpdate(pos, state.setValue(RfidScannerBlock.ACTIVE, true));
            scanner.startedScan = false;
        }

        if (level.getGameTime() >= scanner.scanAt) {
            scanner.scan();
            scanner.getLevel().setBlockAndUpdate(pos, state.setValue(RfidScannerBlock.ACTIVE, false));
            scanner.scheduleScan = false;
        }
    }

    @Nullable
    public RfidScannerPeripheral peripheral(@Nullable Direction direction) {
        return direction == null || getDirection() == direction ? peripheral : null;
    }

    public Direction getDirection() {
        return getBlockState().getValue(DirectionalBlock.FACING);
    }

    public void scheduleScan() {
        scanAt = level.getGameTime() + SCAN_TIME;
        scheduleScan = true;
        startedScan = true;
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

        var range = ClassicPeripherals.CONFIG.rfidScanRange;

        var nearbyPlayers = level.players().stream()
                .filter(player -> player.position().distanceToSqr(origin) <= range * range)
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
