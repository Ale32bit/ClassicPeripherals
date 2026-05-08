package me.alexdevs.classicPeripherals.tiles;

import dan200.computercraft.shared.pocket.core.PocketServerComputer;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.block.NfcReaderBlock;
import me.alexdevs.classicPeripherals.core.Crypto;
import me.alexdevs.classicPeripherals.item.IDataItem;
import me.alexdevs.classicPeripherals.item.NfcCardItem;
import me.alexdevs.classicPeripherals.luaApi.PocketNfcAccess;
import me.alexdevs.classicPeripherals.peripherals.NfcReaderPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class NfcReaderBlockEntity extends BlockEntity {
    protected final NfcReaderPeripheral peripheral = new NfcReaderPeripheral(this);

    public enum ReaderState {
        IDLE,
        WRITING,
        SIGNING,
    }

    private record PendingWrite(String data, @Nullable String label, Boolean readOnly, @Nullable String privateKey) {
    }

    private record PendingSign(String data) {
    }

    private ReaderState state = ReaderState.IDLE;

    @Nullable
    private PendingWrite pendingWrite = null;

    @Nullable
    private PendingSign pendingSign = null;

    public NfcReaderBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockTiles.NFC_READER, pos, blockState);
    }

    public NfcReaderPeripheral peripheral() {
        return peripheral;
    }

    public void flagWrite(String data, @Nullable String label, Boolean flagReadOnly, @Nullable String privateKey) {
        var maxDataSize = ClassicPeripherals.CONFIG.nfcMaxDataSize;

        if (isSigningMode()) {
            cancelSign();
        }

        state = ReaderState.WRITING;

        if (privateKey != null && privateKey.length() != 32) {
            throw new IllegalArgumentException("Invalid private key length");
        }

        data = data.substring(0, Math.min(data.length(), maxDataSize));
        pendingWrite = new PendingWrite(data, label, flagReadOnly, privateKey);

        level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(NfcReaderBlock.STATE, NfcReaderBlock.NfcReaderState.WRITING));
    }

    public void cancelWrite() {
        state = ReaderState.IDLE;

        pendingWrite = null;

        level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(NfcReaderBlock.STATE, NfcReaderBlock.NfcReaderState.NONE));
    }

    public boolean isWriteMode() {
        return state == ReaderState.WRITING;
    }

    public void flagSign(String data) {
        if (isWriteMode()) {
            cancelWrite();
        }

        state = ReaderState.SIGNING;
        pendingSign = new PendingSign(data);

        level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(NfcReaderBlock.STATE, NfcReaderBlock.NfcReaderState.SIGNING));
    }

    public void cancelSign() {
        state = ReaderState.IDLE;
        pendingSign = null;

        level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(NfcReaderBlock.STATE, NfcReaderBlock.NfcReaderState.NONE));
    }

    public boolean isSigningMode() {
        return state == ReaderState.SIGNING;
    }

    public void pingRead() {
        this.getLevel().setBlockAndUpdate(getBlockPos(), getBlockState().setValue(NfcReaderBlock.STATE, NfcReaderBlock.NfcReaderState.READING));
        level.scheduleTick(getBlockPos(), ModBlocks.NFC_READER, 10);
    }

    private boolean doSigning(String privateKey) {
        if (privateKey == null || pendingSign == null) {
            // ignore and still wait for it
            return false;
        }

        var signature = Crypto.sign(pendingSign.data, privateKey);
        var publicKey = Crypto.derivePublicKey(privateKey);
        peripheral.signFeedback(signature, publicKey);
        cancelSign();
        return true;
    }

    public void onUse(ItemStack stack) {
        if (isWriteMode() && pendingWrite != null) {
            var readOnly = IDataItem.isReadOnly(stack);
            if (readOnly) {
                peripheral.writeFeedback(false, "read_only");
                cancelWrite();
                return;
            }

            if (pendingWrite.label != null) {
                IDataItem.setLabel(stack, pendingWrite.label);
            } else {
                IDataItem.clearLabel(stack);
            }

            // setting/clearing name overwrites the tag precedently created.
            IDataItem.setData(stack, pendingWrite.data);
            IDataItem.setReadOnly(stack, pendingWrite.readOnly);

            if (pendingWrite.privateKey != null) {
                IDataItem.setPrivateKey(stack, pendingWrite.privateKey);
            }

            peripheral.writeFeedback(true, "success");
            cancelWrite();

        } else if (isSigningMode()) {
            var privateKey = IDataItem.getPrivateKey(stack);
            doSigning(privateKey);

        } else {
            var data = IDataItem.getData(stack);
            var privateKey = IDataItem.getPrivateKey(stack);
            String publicKey = null;
            if (privateKey != null) {
                publicKey = Crypto.derivePublicKey(privateKey);
            }

            if (data.isPresent()) {
                peripheral.read(data.get(), publicKey);
                pingRead();
            }
        }
    }

    public void onPocketUse(PocketServerComputer pocket) {
        var id = pocket.getID();

        if (isWriteMode() && pendingWrite != null) {
            pocket.queueEvent("nfc_data", new Object[]{NfcCardItem.INTERNAL_SIDE, pendingWrite.data, pendingWrite.label, pendingWrite.readOnly, pendingWrite.privateKey});
            cancelWrite();
        }

        var privateKey = PocketNfcAccess.getPrivateKey(id);
        var publicKey = privateKey.map(Crypto::derivePublicKey).orElse(null);
        if (isSigningMode() && pendingSign != null) {
            if (!doSigning(privateKey.orElse(null))) {
                return;
            }
        }

        var data = PocketNfcAccess.pop(id);
        data.ifPresent(value -> peripheral.read(value, publicKey));
    }
}
