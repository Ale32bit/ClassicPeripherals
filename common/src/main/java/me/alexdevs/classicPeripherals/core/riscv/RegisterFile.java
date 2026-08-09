package me.alexdevs.classicPeripherals.core.riscv;

public class RegisterFile {
    private int[] registers = new int[32];

    public int get(int index) {
        return registers[index];
    }

    public void set(int index, int value) {
        if (index != 0) {
            registers[index] = value;
        }
    }

    public void set(int index, boolean value) {
        set(index, value ? 1 : 0);
    }

    public void clear() {
        registers = new int[32];
    }
}
