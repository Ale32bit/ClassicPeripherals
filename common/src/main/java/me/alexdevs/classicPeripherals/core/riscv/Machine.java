package me.alexdevs.classicPeripherals.core.riscv;

import java.util.ArrayList;
import java.util.List;

public class Machine {
    private List<CPU> cpuCores = new ArrayList<>();

    private final Memory memory = new Memory(0, 1024 * 16);
    private boolean halted = false;

    public Machine(int cores) {
        for (int i = 0; i < cores; i++) {
            cpuCores.add(new CPU());
        }
    }

    public boolean run(int ticks) {
        for (int i = 0; i < ticks; i++) {
            for (CPU cpu : cpuCores) {
                cpu.memory = memory;

                try {
                    cpu.step();
                } catch (Trap trap) {
                    halt(trap);
                    return false;
                }
            }
        }

        return !halted;
    }


    public Memory memory() {
        return memory;
    }

    public void halt(Trap trap) {
        halted = true;
    }


}
