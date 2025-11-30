package me.alexdevs.classicPeripherals.tiles;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.ModComponents;
import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.block.NfcReaderBlock;
import me.alexdevs.classicPeripherals.item.AbstractDataItem;
import me.alexdevs.classicPeripherals.peripherals.NfcReaderPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class NfcReaderBlockEntity extends BlockEntity {
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
        var maxDataSize = ClassicPeripherals.CONFIG.nfcMaxDataSize;

        writeMode = true;
        pendingWriteData = data.substring(0, Math.min(data.length(), maxDataSize));
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
            var readOnly = AbstractDataItem.isReadOnly(stack);
            if (readOnly) {
                peripheral.writeFeedback(false, "read_only");
                cancelWrite();
                return;
            }

            if (pendingLabel != null) {
                AbstractDataItem.setLabel(stack, pendingLabel);
            } else {
                AbstractDataItem.clearLabel(stack);
            }

            // setting/clearing name overwrites the tag precedently created.
            AbstractDataItem.setData(stack, pendingWriteData);
            AbstractDataItem.setReadOnly(stack, pendingReadOnly);

            peripheral.writeFeedback(true, "success");
            cancelWrite();
        } else {
            var data = AbstractDataItem.getData(stack);
            if (data.isPresent()) {
                peripheral.read(data.get());
                pingRead();
            }
        }
    }
}
