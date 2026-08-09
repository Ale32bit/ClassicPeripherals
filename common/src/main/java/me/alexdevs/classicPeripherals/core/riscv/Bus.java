package me.alexdevs.classicPeripherals.core.riscv;

public interface Bus {
    int fetch32(int address) throws Trap;

    byte load8(int address) throws Trap;

    short load16(int address) throws Trap;

    int load32(int address) throws Trap;

    void store8(int address, byte value) throws Trap;

    void store16(int address, short value) throws Trap;

    void store32(int address, int value) throws Trap;
}
