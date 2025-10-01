package me.alexdevs.classicPeripherals.tiles;

import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.block.NfcReaderBlock;
import me.alexdevs.classicPeripherals.peripherals.NfcReaderPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class NfcReaderBlockEntity extends BlockEntity {
    public static final int MAX_DATA_SIZE = 128;

    protected NfcReaderPeripheral peripheral = new NfcReaderPeripheral(this);

    private boolean writeMode = false;
    private String pendingWriteData = "";
    @Nullable
    private String pendingLabel = null;

    public NfcReaderBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockTiles.NFC_READER, pos, blockState);
    }

    public NfcReaderPeripheral peripheral() {
        return peripheral;
    }

    public void flagWrite(String data, @Nullable String label) {
        writeMode = true;
        pendingWriteData = data.substring(0, Math.min(data.length(), MAX_DATA_SIZE));
        pendingLabel = label;

        level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(NfcReaderBlock.STATE, NfcReaderBlock.NfcReaderState.WRITING));
    }

    public void cancelWrite() {
        writeMode = false;
        pendingWriteData = "";
        pendingLabel = null;
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
        var tag = stack.getOrCreateTag();
        if(writeMode) {
            if(pendingLabel != null) {
                stack.setHoverName(Component.literal(pendingLabel));
            } else {
                stack.resetHoverName();
            }

            tag.putString("data", pendingWriteData);

            peripheral.queueWrite();
            cancelWrite();
        } else {
            var data = tag.getString("data");
            if(!data.isEmpty()) {
                peripheral.read(data);
                pingRead();
            }
        }
    }
}
