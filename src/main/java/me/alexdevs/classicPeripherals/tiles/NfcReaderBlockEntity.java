package me.alexdevs.classicPeripherals.tiles;

import me.alexdevs.classicPeripherals.ModComponents;
import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.block.NfcReaderBlock;
import me.alexdevs.classicPeripherals.item.NfcCardItem;
import me.alexdevs.classicPeripherals.peripherals.NfcReaderPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class NfcReaderBlockEntity extends BlockEntity {
    public static final int MAX_DATA_SIZE = 128;

    protected final NfcReaderPeripheral peripheral = new NfcReaderPeripheral(this);

    private boolean writeMode = false;
    private String pendingWriteData = "";
    @Nullable
    private String pendingLabel = null;
    private boolean pendingReadOnly = false;

    public NfcReaderBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockTiles.NFC_READER, pos, blockState);
    }

    public NfcReaderPeripheral peripheral() {
        return peripheral;
    }

    public void flagWrite(String data, @Nullable String label, Boolean flagReadOnly) {
        writeMode = true;
        pendingWriteData = data.substring(0, Math.min(data.length(), MAX_DATA_SIZE));
        pendingLabel = label;
        pendingReadOnly = flagReadOnly;

        level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(NfcReaderBlock.STATE, NfcReaderBlock.NfcReaderState.WRITING));
    }

    public void cancelWrite() {
        writeMode = false;
        pendingWriteData = "";
        pendingLabel = null;
        pendingReadOnly = false;

        level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(NfcReaderBlock.STATE, NfcReaderBlock.NfcReaderState.NONE));
    }

    public boolean isWriteMode() {
        return writeMode;
    }

    public void pingRead() {
        this.getLevel().setBlockAndUpdate(getBlockPos(), getBlockState().setValue(NfcReaderBlock.STATE, NfcReaderBlock.NfcReaderState.READING));
        level.scheduleTick(getBlockPos(), ModBlocks.NFC_READER, 10);
    }

    public void onUse(ItemStack stack) {
        if (writeMode) {
            var components = stack.getComponents();
            var readOnly = components.getOrDefault(ModComponents.NFC_READONLY, false);
            if (readOnly) {
                peripheral.writeFeedback(false, "read_only");
                cancelWrite();
                return;
            }

            var mapBuilder = DataComponentMap.builder();

            if (pendingLabel != null) {
                mapBuilder.set(DataComponents.CUSTOM_NAME, Component.literal(pendingLabel));
            } else {
                stack.remove(DataComponents.CUSTOM_NAME);
            }

            mapBuilder.set(ModComponents.NFC_DATA, pendingWriteData);
            mapBuilder.set(ModComponents.NFC_READONLY, pendingReadOnly);

            stack.applyComponents(mapBuilder.build());

            peripheral.writeFeedback(true, "success");
            cancelWrite();
        } else {
            var data = NfcCardItem.getData(stack);
            if (data.isPresent()) {
                peripheral.read(data.get());
                pingRead();
            }
        }
    }
}
