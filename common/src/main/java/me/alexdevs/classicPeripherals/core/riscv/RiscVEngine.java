package me.alexdevs.classicPeripherals.core.riscv;

import java.util.HashMap;
import java.util.function.Function;

public class RiscVEngine {
    public enum InstructionFormat {
        R, // reg-reg arithm
        I, // imm arithm, loads and jumps
        S, // stores
        B, // branches
        U, // large imm
        J, // jumps
    }

    private final RegisterFile registers = new RegisterFile();
    private final Memory memory = new Memory(0, 1024 * 16);
    private int pc = 0;
    private boolean halted = false;

    private final HashMap<Integer, SystemCall> systemCalls = new HashMap<>();

    public int run(int budget) {
        int retired = 0;
        try {
            while (retired < budget && !halted) {
                step();
                retired++;
            }
        } catch (Trap trap) {
            halt(trap);
        }
        return retired;
    }

    public void step() throws Trap {
        if ((pc & 3) != 0) {
            throw new Trap(Trap.Cause.INSN_MISALIGNED, pc);
        }

        var instruction = memory.fetch32(pc);
        pc = execute(instruction, pc + 4);
    }

    public Memory memory() {
        return memory;
    }

    public void halt(Trap trap) {
        halted = true;
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

    private int execute(int instruction, int nextPc) throws Trap {
        var inst = Instruction.of(instruction);

        nextPc = switch (inst.opcode) {
            case 0b0110111 -> lui(inst, nextPc, InstructionFormat.U);
            case 0b0010111 -> auipc(inst, nextPc, InstructionFormat.U);
            case 0b1101111 -> jal(inst, nextPc, InstructionFormat.J);
            case 0b1100111 -> jalr(inst, nextPc, InstructionFormat.I);
            case 0b1100011 -> branch(inst, nextPc, InstructionFormat.B);
            case 0b0000011 -> load(inst, nextPc, InstructionFormat.I);
            case 0b0100011 -> store(inst, nextPc, InstructionFormat.S);
            case 0b0010011 -> opimm(inst, nextPc, InstructionFormat.I);
            case 0b0110011 -> op(inst, nextPc, InstructionFormat.R);
            case 0b0001111 -> miscmem(inst, nextPc, InstructionFormat.I);
            case 0b1110011 -> system(inst, nextPc, InstructionFormat.I);
            default -> throw new Trap(Trap.Cause.ILLEGAL_INSN, inst.raw);
        };

        return nextPc;
    }

    private int getImmediate(InstructionFormat format, int inst) {
        return switch (format) {
            case I -> inst >> 20;
            case S -> ((inst >> 25) << 5) | ((inst >>> 7) & 0x1F);
            case B -> ((inst >> 31) << 12) // :sob:
                    | (((inst >>> 7) & 0x1) << 11)
                    | (((inst >>> 25) & 0x3F) << 5)
                    | (((inst >>> 8) & 0xF) << 1);
            case U -> inst & 0xFF_FF_F0_00;
            case J -> ((inst >> 31) << 20)
                    | (inst & 0xFF000)
                    | (((inst >>> 20) & 0x1) << 11)
                    | (((inst >>> 21) & 0x3FF) << 1);
            case R -> 0;
        };
    }

    private int lui(Instruction inst, int nextPc, InstructionFormat format) {
        var immediate = getImmediate(format, inst.raw);
        registers.set(inst.rd, immediate);
        return nextPc;
    }

    private int auipc(Instruction inst, int nextPc, InstructionFormat format) {
        var immediate = getImmediate(format, inst.raw);
        registers.set(inst.rd, immediate + pc);
        return nextPc;
    }

    private int jal(Instruction inst, int nextPc, InstructionFormat format) throws Trap {
        var immediate = getImmediate(format, inst.raw);
        var target = pc + immediate;

        if ((target & 3) != 0) {
            throw new Trap(Trap.Cause.INSN_MISALIGNED, target);
        }

        registers.set(inst.rd, nextPc);
        return target;
    }

    private int jalr(Instruction inst, int nextPc, InstructionFormat format) throws Trap {
        var immediate = getImmediate(format, inst.raw);
        var target = (registers.get(inst.rs1) + immediate) & ~1;

        if ((target & 3) != 0) {
            throw new Trap(Trap.Cause.INSN_MISALIGNED, target);
        }

        registers.set(inst.rd, nextPc);
        return target;
    }

    private int branch(Instruction inst, int nextPc, InstructionFormat format) throws Trap {
        var immediate = getImmediate(format, inst.raw);
        var target = pc + immediate;
        var r1 = registers.get(inst.rs1);
        var r2 = registers.get(inst.rs2);

        var isTrue = switch (inst.funct3) {
            case 0b000 -> r1 == r2;
            case 0b001 -> r1 != r2;
            case 0b100 -> r1 < r2;
            case 0b101 -> r1 >= r2;
            case 0b110 -> Integer.compareUnsigned(r1, r2) < 0;
            case 0b111 -> Integer.compareUnsigned(r1, r2) >= 0;
            default -> throw new Trap(Trap.Cause.ILLEGAL_INSN, inst.raw);
        };

        if (isTrue) {
            if ((target & 3) != 0) {
                throw new Trap(Trap.Cause.INSN_MISALIGNED, target);
            }
            nextPc = target;
        }

        return nextPc;
    }

    private int load(Instruction inst, int nextPc, InstructionFormat format) throws Trap {
        var immediate = getImmediate(format, inst.raw);
        var r1 = registers.get(inst.rs1);
        var address = r1 + immediate;

        var data = switch (inst.funct3) {
            case 0b000 -> memory.load8(address);
            case 0b100 -> memory.load8(address) & 0xFF;
            case 0b001 -> {
                assertAlign(address, 1, Trap.Cause.LOAD_MISALIGNED);
                yield memory.load16(address);
            }
            case 0b101 -> {
                assertAlign(address, 1, Trap.Cause.LOAD_MISALIGNED);
                yield memory.load16(address) & 0xFFFF;
            }
            case 0b010 -> {
                assertAlign(address, 3, Trap.Cause.LOAD_MISALIGNED);
                yield memory.load32(address);
            }
            default -> throw new Trap(Trap.Cause.ILLEGAL_INSN, inst.raw);
        };

        registers.set(inst.rd, data);
        return nextPc;
    }

    private int store(Instruction inst, int nextPc, InstructionFormat format) throws Trap {
        var immediate = getImmediate(format, inst.raw);
        var address = registers.get(inst.rs1) + immediate;
        var value = registers.get(inst.rs2);

        switch (inst.funct3) {
            case 0b000 -> memory.store8(address, (byte) value);
            case 0b001 -> {
                assertAlign(address, 1, Trap.Cause.STORE_MISALIGNED);
                memory.store16(address, (short) value);
            }
            case 0b010 -> {
                assertAlign(address, 3, Trap.Cause.STORE_MISALIGNED);
                memory.store32(address, value);
            }
            default -> throw new Trap(Trap.Cause.ILLEGAL_INSN, inst.raw);
        }

        return nextPc;
    }

    private int opimm(Instruction inst, int nextPc, InstructionFormat format) throws Trap {
        var immediate = getImmediate(format, inst.raw);
        var r1 = registers.get(inst.rs1);

        var shamt = inst.rs2;

        var data = switch (inst.funct3) {
            case 0b000 -> r1 + immediate;
            case 0b010 -> r1 < immediate ? 1 : 0;
            case 0b011 -> Integer.compareUnsigned(r1, immediate) < 0 ? 1 : 0;
            case 0b100 -> r1 ^ immediate;
            case 0b110 -> r1 | immediate;
            case 0b111 -> r1 & immediate;
            case 0b001 -> {
                if (inst.funct7 != 0b0000000) {
                    throw new Trap(Trap.Cause.ILLEGAL_INSN, inst.raw);
                }
                yield r1 << shamt;
            }
            case 0b101 -> switch (inst.funct7) {
                case 0b0000000 -> r1 >>> shamt;
                case 0b0100000 -> r1 >> shamt;
                default -> throw new Trap(Trap.Cause.ILLEGAL_INSN, inst.raw);
            };
            default -> throw new Trap(Trap.Cause.ILLEGAL_INSN, inst.raw);
        };

        registers.set(inst.rd, data);

        return nextPc;
    }

    private int op(Instruction inst, int nextPc, InstructionFormat format) throws Trap {
        var r1 = registers.get(inst.rs1);
        var r2 = registers.get(inst.rs2);
        var shamt = r2 & 0b11111;

        if (inst.funct7 != 0b0000000 && inst.funct7 != 0b0100000) {
            throw new Trap(Trap.Cause.ILLEGAL_INSN, inst.raw);
        }

        var alternate = inst.funct7 == 0b0100000;
        if (alternate && inst.funct3 != 0b000 && inst.funct3 != 0b101) {
            throw new Trap(Trap.Cause.ILLEGAL_INSN, inst.raw);
        }

        var data = switch (inst.funct3) {
            case 0b000 -> alternate ? r1 - r2 : r1 + r2;
            case 0b001 -> r1 << shamt;
            case 0b010 -> r1 < r2 ? 1 : 0;
            case 0b011 -> Integer.compareUnsigned(r1, r2) < 0 ? 1 : 0;
            case 0b100 -> r1 ^ r2;
            case 0b101 -> alternate ? r1 >> shamt : r1 >>> shamt;
            case 0b110 -> r1 | r2;
            default -> r1 & r2;
        };

        registers.set(inst.rd, data);
        return nextPc;
    }

    private int miscmem(Instruction inst, int nextPc, InstructionFormat format) throws Trap {
        switch (inst.funct3) {
            case 0b000:
            case 0b001:
                // no-op
                break;

            default:
                throw new Trap(Trap.Cause.ILLEGAL_INSN, inst.raw);
        }

        return nextPc;
    }

    private int system(Instruction inst, int nextPc, InstructionFormat format) throws Trap {
        if (inst.funct3 != 0 || inst.rd != 0 || inst.rs1 != 0) {
            throw new Trap(Trap.Cause.ILLEGAL_INSN, inst.raw);
        }

        var immediate = getImmediate(format, inst.raw);
        return switch (immediate) {
            case 0 -> {
                pc = nextPc;
                ecall();
                yield pc;
            }
            case 1 -> throw new Trap(Trap.Cause.BREAKPOINT, 0);
            default -> throw new Trap(Trap.Cause.ILLEGAL_INSN, inst.raw);
        };
    }

    private record Instruction(int opcode, int rd, int funct3, int rs1, int rs2, int funct7, int raw) {
        public static Instruction of(int inst) {
            var opcode = inst & 0x7F;
            var rd = (inst >>> 7) & 0x1F;
            var funct3 = (inst >>> 12) & 0x7;
            var rs1 = (inst >>> 15) & 0x1F;
            var rs2 = (inst >>> 20) & 0x1F;
            var funct7 = (inst >>> 25);

            return new Instruction(opcode, rd, funct3, rs1, rs2, funct7, inst);
        }
    }

    private static void assertAlign(int address, int mask, Trap.Cause cause) throws Trap {
        if ((address & mask) != 0) {
            throw new Trap(cause, address);
        }
    }
}
