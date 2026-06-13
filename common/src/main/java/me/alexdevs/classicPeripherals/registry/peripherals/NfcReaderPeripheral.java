package me.alexdevs.classicPeripherals.registry.peripherals;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.AttachedComputerSet;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.core.util.StringUtil;
import me.alexdevs.classicPeripherals.registry.tiles.NfcReaderBlockEntity;
import me.alexdevs.classicPeripherals.utils.LuaUtils;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class NfcReaderPeripheral implements IPeripheral {
    private final NfcReaderBlockEntity nfcReader;
    private final AttachedComputerSet computers = new AttachedComputerSet();

    public NfcReaderPeripheral(NfcReaderBlockEntity nfcReader) {
        this.nfcReader = nfcReader;
    }

    @Override
    public @NonNull String getType() {
        return "nfc_reader";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof NfcReaderPeripheral o && nfcReader == o.nfcReader;
    }

    @Override
    public void attach(@NonNull IComputerAccess computer) {
        computers.add(computer);
    }

    @Override
    public void detach(@NonNull IComputerAccess computer) {
        computers.remove(computer);
    }

    public void read(String data, @Nullable String publicKey) {
        computers.forEach(computer -> computer.queueEvent("nfc_data", computer.getAttachmentName(), data, publicKey));
    }

    public void writeFeedback(boolean success, String message) {
        computers.forEach(computer -> computer.queueEvent("nfc_write", computer.getAttachmentName(), success, message));
    }

    public void signFeedback(String signature, String publicKey) {
        computers.forEach(computer -> computer.queueEvent("nfc_sign", computer.getAttachmentName(), signature, publicKey));
    }

    @LuaFunction(mainThread = true)
    public final void write(String data, Optional<String> label, Optional<Boolean> flagReadOnly, Optional<String> privateKey) throws LuaException {

        if (privateKey.isPresent()) {
            LuaUtils.validateKey(privateKey.get());
        }

        nfcReader.flagWrite(
                data,
                label.map(StringUtil::normaliseLabel).orElse(null),
                flagReadOnly.orElse(false),
                privateKey.orElse(null)
        );
    }

    @LuaFunction(mainThread = true)
    public final boolean cancelWrite() {
        var inWriteMode = nfcReader.isWriteMode();
        nfcReader.cancelWrite();
        return inWriteMode;
    }

    @LuaFunction(mainThread = true)
    public final void sign(String data) {
        nfcReader.flagSign(data);
    }

    @LuaFunction(mainThread = true)
    public final boolean cancelSign() {
        var inSignMode = nfcReader.isSigningMode();
        nfcReader.cancelSign();
        return inSignMode;
    }
}
