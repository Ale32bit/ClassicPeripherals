package me.alexdevs.classicPeripherals.core.riscv;

@FunctionalInterface
public interface SystemCall {
    int call(RiscVEngine engine, int[] args);
}
