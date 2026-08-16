package me.alexdevs.classicPeripherals.core.riscv;

import java.util.HashMap;

public class CPU {
    public final RegisterFile registers = new RegisterFile();
    protected int pc = 0;
    protected Memory memory;
    private final HashMap<Integer, SystemCall> systemCalls = new HashMap<>();

    public void step() throws Trap {
        if ((pc & 3) != 0) {
            throw new Trap(Trap.Cause.INSN_MISALIGNED, pc);
        }

        var instruction = memory.fetch32(pc);
        pc = Instructions.execute(this, instruction, pc + 4);
    }

    protected void ecall() {
        var call = registers.get(17);
        var args = new int[]{
                registers.get(10),
                registers.get(11),
                registers.get(12),
                registers.get(13),
                registers.get(14),
                registers.get(15),
                registers.get(16),
        };

        var handler = systemCalls.get(call);

        if (handler == null) {
            registers.set(10, -38); // ENOSYS
            return;
        }

        registers.set(10, handler.call(this, args));
    }

}
