package me.alexdevs.classicPeripherals.core.riscv;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.util.Arrays;

public final class Memory implements Bus {
    private static final VarHandle INT_LE =
            MethodHandles.byteArrayViewVarHandle(int[].class, ByteOrder.LITTLE_ENDIAN);
    private static final VarHandle SHORT_LE =
            MethodHandles.byteArrayViewVarHandle(short[].class, ByteOrder.LITTLE_ENDIAN);

    private final int base;
    private final byte[] memory;

    public Memory(int base, int size) {
        if (size <= 0) throw new IllegalArgumentException("size must be positive");
        this.base = base;
        this.memory = new byte[size];
    }

    private int offset(int address, int width, Trap.Cause cause) throws Trap {
        int offset = address - base;
        if (Integer.compareUnsigned(offset, memory.length - width) > 0) {
            throw new Trap(cause, address);
        }
        return offset;
    }

    @Override
    public int fetch32(int address) throws Trap {
        return (int) INT_LE.get(memory, offset(address, 4, Trap.Cause.INSN_ACCESS_FAULT));
    }

    @Override
    public byte load8(int address) throws Trap {
        return memory[offset(address, 1, Trap.Cause.LOAD_ACCESS_FAULT)];
    }

    @Override
    public short load16(int address) throws Trap {
        return (short) SHORT_LE.get(memory, offset(address, 2, Trap.Cause.LOAD_ACCESS_FAULT));
    }

    @Override
    public int load32(int address) throws Trap {
        return (int) INT_LE.get(memory, offset(address, 4, Trap.Cause.LOAD_ACCESS_FAULT));
    }

    @Override
    public void store8(int address, byte value) throws Trap {
        memory[offset(address, 1, Trap.Cause.STORE_ACCESS_FAULT)] = value;
    }

    @Override
    public void store16(int address, short value) throws Trap {
        SHORT_LE.set(memory, offset(address, 2, Trap.Cause.STORE_ACCESS_FAULT), value);
    }

    @Override
    public void store32(int address, int value) throws Trap {
        INT_LE.set(memory, offset(address, 4, Trap.Cause.STORE_ACCESS_FAULT), value);
    }

    public void write(int address, byte[] data, int offset, int length) throws Trap {
        int start = offset(address, length, Trap.Cause.STORE_ACCESS_FAULT);
        System.arraycopy(data, offset, memory, start, length);
    }

    public byte[] read(int address, int length) throws Trap {
        int start = offset(address, length, Trap.Cause.LOAD_ACCESS_FAULT);
        return Arrays.copyOfRange(memory, start, start + length);
    }

    public int base() { return base; }

    public int size() { return memory.length; }

    public byte[] backing() { return memory; }

    public void restore(byte[] image) {
        if (image.length != memory.length) {
            throw new IllegalArgumentException(
                    "expected " + memory.length + " bytes, got " + image.length);
        }
        System.arraycopy(image, 0, memory, 0, image.length);
    }

    public void clear() { Arrays.fill(memory, (byte) 0); }
}
