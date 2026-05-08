package me.alexdevs.classicPeripherals.tiles;

import dan200.computercraft.shared.util.WorldUtil;
import me.alexdevs.classicPeripherals.block.ScannerBlock;
import me.alexdevs.classicPeripherals.peripherals.ScannerPeripheral;
import me.alexdevs.classicPeripherals.screen.ScannerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class ScannerBlockEntity extends BlockEntity implements MenuProvider, Container {
    protected final ScannerPeripheral peripheral = new ScannerPeripheral(this);
    private ItemStack storedItem = ItemStack.EMPTY;

    private ItemStack printoutStack = ItemStack.EMPTY;

    private final AtomicBoolean ejectQueued = new AtomicBoolean(false);
    private final AtomicBoolean loadQueued = new AtomicBoolean(true);

    public ScannerBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockTiles.SCANNER, pos, blockState);
    }

    public ScannerPeripheral peripheral() {
        return peripheral;
    }

    public ItemStack getPrintout() {
        return getItem(0);
    }

    public void eject() {
        ejectQueued.set(true);
    }

    public void setPrintoutStack(ItemStack stack) {
        setItem(0, stack);
        setChanged();
    }

    private void ejectContent() {
        if (getLevel().isClientSide) {
            return;
        }

        var stack = getPrintout();
        if (stack.isEmpty()) {
            return;
        }

        setPrintoutStack(ItemStack.EMPTY);

        WorldUtil.dropItemStack(getLevel(), getBlockPos(), getDirection(), stack);
        getLevel().levelEvent(LevelEvent.SOUND_DISPENSER_DISPENSE, getBlockPos(), 0);
    }

    public Direction getDirection() {
        return getBlockState().getValue(HorizontalDirectionalBlock.FACING);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ScannerBlockEntity scanner) {
        if (scanner.ejectQueued.getAndSet(false)) {
            scanner.ejectContent();
        }

        if (scanner.loadQueued.getAndSet(false)) {
            scanner.updateState();
        }
    }

    private void updateState() {
        var newStack = getPrintout();
        if (ItemStack.isSameItemSameComponents(newStack, printoutStack)) {
            return;
        }

        var newPrintout = newStack.copy();
        updateBlockState(!newPrintout.isEmpty());

        if (!printoutStack.isEmpty()) {
            peripheral.emitScannerEvent(false);
        }

        printoutStack = newPrintout;

        if (!newStack.isEmpty()) {
            peripheral.emitScannerEvent(true);
        }
    }

    private void updateBlockState(boolean hasPrintout) {
        var blockState = getBlockState();
        if (blockState.getValue(ScannerBlock.TRAY) == hasPrintout) {
            return;
        }

        getLevel().setBlockAndUpdate(getBlockPos(), blockState.setValue(ScannerBlock.TRAY, hasPrintout));
    }

    @Override
    public void setChanged() {
        if (getLevel() != null && !getLevel().isClientSide) {
            updateState();
        }

        super.setChanged();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.classicperipherals.scanner");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        if (!storedItem.isEmpty()) {
            nbt.put("StoredItem", storedItem.save(registries));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        var parsedStack = ItemStack.parse(registries, nbt.getCompound("StoredItem"));
        setPrintoutStack(parsedStack.orElse(ItemStack.EMPTY));
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return storedItem.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return storedItem;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot == 0 && !storedItem.isEmpty()) {
            ItemStack result = storedItem.split(amount);
            if (storedItem.isEmpty()) {
                storedItem = ItemStack.EMPTY;
            }
            setChanged();
            return result;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot == 0) {
            ItemStack result = storedItem;
            storedItem = ItemStack.EMPTY;
            return result;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot == 0) {
            storedItem = stack;
            if (stack.getCount() > getMaxStackSize()) {
                stack.setCount(getMaxStackSize());
            }
            setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        storedItem = ItemStack.EMPTY;
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
        updateState();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ScannerMenu(i, inventory, this);
    }
}
