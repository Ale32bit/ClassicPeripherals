package me.alexdevs.classicPeripherals.tiles;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.block.RfidScannerBlock;
import me.alexdevs.classicPeripherals.core.ItemDataHandler;
import me.alexdevs.classicPeripherals.item.ModItems;
import me.alexdevs.classicPeripherals.mixinInterface.ILivingEntityMixin;
import me.alexdevs.classicPeripherals.peripherals.RfidScannerPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

        if (level.isClientSide)
            return List.of();

        var range = ClassicPeripherals.CONFIG.rfidScanRange;

        // Scan player inventories
        var nearbyPlayers = level.players().stream()
                .filter(player -> player.position().distanceToSqr(origin) <= range * range)
                .map(player -> (ServerPlayer) player)
                .toList();

        for (var player : nearbyPlayers) {
            var inventory = player.getInventory();
            var distance = player.position().distanceTo(origin);
            for (var stack : inventory.items) {
                if (!stack.is(ModItems.RFID_BADGE)) {
                    continue;
                }

                var data = ItemDataHandler.getData(stack);
                if (data.isEmpty()) {
                    continue;
                }

                data.ifPresent(s -> badges.add(new ScannedRfidBadge(s, distance)));
            }

            var equippedData = ModItems.RFID_BADGE.getEquippedData(player);
            equippedData.ifPresent(s -> badges.add(new ScannedRfidBadge(s, distance)));
        }

        var aabb = AABB.ofSize(origin, range, range, range);
        var nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, aabb);

        for (var livingEntity : nearbyEntities) {
            // Skip players because already fully scanned
            if (livingEntity instanceof Player) {
                continue;
            }

            var distance = livingEntity.position().distanceTo(origin);

            // Scan living entities with RFID injected
            var entity = (ILivingEntityMixin) livingEntity;
            var injectedData = entity.getRfidData();
            injectedData.ifPresent(s -> badges.add(new ScannedRfidBadge(s, distance)));

            // Scan living entities holding RFID badge
            if (livingEntity.isHolding(ModItems.RFID_BADGE)) {
                var handSlots = livingEntity.getHandSlots();
                for (var handStack : handSlots) {
                    if (handStack.is(ModItems.RFID_BADGE)) {
                        var data = ItemDataHandler.getData(handStack);
                        data.ifPresent(s -> badges.add(new ScannedRfidBadge(s, distance)));
                    }
                }
            }
        }

        // Dropped RFID badges
        var nearbyDroppedItems = level.getEntitiesOfClass(ItemEntity.class, aabb);
        for (var droppedItem : nearbyDroppedItems) {
            var stack = droppedItem.getItem();
            if(!stack.is(ModItems.RFID_BADGE)) {
                continue;
            }

            var distance = droppedItem.position().distanceTo(origin);
            var badgeData = ItemDataHandler.getData(stack);
            badgeData.ifPresent(s -> badges.add(new ScannedRfidBadge(s, distance)));
        }

        return badges;
    }

    public record ScannedRfidBadge(String data, double distance) {
    }
}
