package me.alexdevs.classicPeripherals.peripherals;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.AttachedComputerSet;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.core.util.StringUtil;
import me.alexdevs.classicPeripherals.tiles.NfcReaderBlockEntity;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

import static dan200.computercraft.core.util.StringUtil.normaliseLabel;

public class NfcReaderPeripheral implements IPeripheral {
    private final NfcReaderBlockEntity nfcReader;
    private final AttachedComputerSet computers = new AttachedComputerSet();

    public NfcReaderPeripheral(NfcReaderBlockEntity nfcReader) {
        this.nfcReader = nfcReader;
    }

    @Override
    public String getType() {
        return "nfc_reader";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof NfcReaderPeripheral o && nfcReader == o.nfcReader;
    }

    @Override
    public void attach(IComputerAccess computer) {
        computers.add(computer);
    }

    @Override
    public void detach(IComputerAccess computer) {
        computers.remove(computer);
    }

    public void read(String data) {
        computers.forEach(computer -> computer.queueEvent("nfc_data", computer.getAttachmentName(), data));
    }

    public void queueWrite() {
        computers.forEach(computer -> computer.queueEvent("nfc_write", computer.getAttachmentName()));
    }

    @LuaFunction(mainThread = true)
    public final void write(String data, Optional<String> label) {
        nfcReader.flagWrite(data, label.map(StringUtil::normaliseLabel).orElse(null));
    }

    @LuaFunction(mainThread = true)
    public final boolean cancelWrite() {
        var inWriteMode = nfcReader.isWriteMode();
        nfcReader.cancelWrite();
        return inWriteMode;
    }
}
