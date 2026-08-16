package me.alexdevs.classicPeripherals.core.riscv;

@FunctionalInterface
public interface SystemCall {
    int call(CPU engine, int[] args);
}
