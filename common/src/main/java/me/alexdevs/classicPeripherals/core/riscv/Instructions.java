package me.alexdevs.classicPeripherals.core.riscv;

public class Instructions {
    public enum InstructionFormat {
        R, // reg-reg arithm
        I, // imm arithm, loads and jumps
        S, // stores
        B, // branches
        U, // large imm
        J, // jumps
    }

    private static int getImmediate(InstructionFormat format, int inst) {
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

    private static void assertAlign(int address, int mask, Trap.Cause cause) throws Trap {
        if ((address & mask) != 0) {
            throw new Trap(cause, address);
        }
    }

    public static int execute(CPU cpu, int instruction, int nextPc) throws Trap {
        var inst = Instruction.of(instruction);

        nextPc = switch (inst.opcode) {
            case 0b0110111 -> lui(cpu, inst, nextPc, InstructionFormat.U);
            case 0b0010111 -> auipc(cpu, inst, nextPc, InstructionFormat.U);
            case 0b1101111 -> jal(cpu, inst, nextPc, InstructionFormat.J);
            case 0b1100111 -> jalr(cpu, inst, nextPc, InstructionFormat.I);
            case 0b1100011 -> branch(cpu, inst, nextPc, InstructionFormat.B);
            case 0b0000011 -> load(cpu, inst, nextPc, InstructionFormat.I);
            case 0b0100011 -> store(cpu, inst, nextPc, InstructionFormat.S);
            case 0b0010011 -> opimm(cpu, inst, nextPc, InstructionFormat.I);
            case 0b0110011 -> op(cpu, inst, nextPc, InstructionFormat.R);
            case 0b0001111 -> miscmem(cpu, inst, nextPc, InstructionFormat.I);
            case 0b1110011 -> system(cpu, inst, nextPc, InstructionFormat.I);
            default -> throw new Trap(Trap.Cause.ILLEGAL_INSN, inst.raw);
        };

        return nextPc;
    }

    public static int lui(CPU cpu, Instruction inst, int nextPc, InstructionFormat format) {
        var immediate = getImmediate(format, inst.raw);
        cpu.registers.set(inst.rd, immediate);
        return nextPc;
    }

    public static int auipc(CPU cpu, Instruction inst, int nextPc, InstructionFormat format) {
        var immediate = getImmediate(format, inst.raw);
        cpu.registers.set(inst.rd, immediate + cpu.pc);
        return nextPc;
    }

    public static int jal(CPU cpu, Instruction inst, int nextPc, InstructionFormat format) throws Trap {
        var immediate = getImmediate(format, inst.raw);
        var target = cpu.pc + immediate;

        if ((target & 3) != 0) {
            throw new Trap(Trap.Cause.INSN_MISALIGNED, target);
        }

        cpu.registers.set(inst.rd, nextPc);
        return target;
    }

    public static int jalr(CPU cpu, Instruction inst, int nextPc, InstructionFormat format) throws Trap {
        var immediate = getImmediate(format, inst.raw);
        var target = (cpu.registers.get(inst.rs1) + immediate) & ~1;

        if ((target & 3) != 0) {
            throw new Trap(Trap.Cause.INSN_MISALIGNED, target);
        }

        cpu.registers.set(inst.rd, nextPc);
        return target;
    }

    public static int branch(CPU cpu, Instruction inst, int nextPc, InstructionFormat format) throws Trap {
        var immediate = getImmediate(format, inst.raw);
        var target = cpu.pc + immediate;
        var r1 = cpu.registers.get(inst.rs1);
        var r2 = cpu.registers.get(inst.rs2);

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

    public static int load(CPU cpu, Instruction inst, int nextPc, InstructionFormat format) throws Trap {
        var immediate = getImmediate(format, inst.raw);
        var r1 = cpu.registers.get(inst.rs1);
        var address = r1 + immediate;

        var data = switch (inst.funct3) {
            case 0b000 -> cpu.memory.load8(address);
            case 0b100 -> cpu.memory.load8(address) & 0xFF;
            case 0b001 -> {
                assertAlign(address, 1, Trap.Cause.LOAD_MISALIGNED);
                yield cpu.memory.load16(address);
            }
            case 0b101 -> {
                assertAlign(address, 1, Trap.Cause.LOAD_MISALIGNED);
                yield cpu.memory.load16(address) & 0xFFFF;
            }
            case 0b010 -> {
                assertAlign(address, 3, Trap.Cause.LOAD_MISALIGNED);
                yield cpu.memory.load32(address);
            }
            default -> throw new Trap(Trap.Cause.ILLEGAL_INSN, inst.raw);
        };

        cpu.registers.set(inst.rd, data);
        return nextPc;
    }

    public static int store(CPU cpu, Instruction inst, int nextPc, InstructionFormat format) throws Trap {
        var immediate = getImmediate(format, inst.raw);
        var address = cpu.registers.get(inst.rs1) + immediate;
        var value = cpu.registers.get(inst.rs2);

        switch (inst.funct3) {
            case 0b000 -> cpu.memory.store8(address, (byte) value);
            case 0b001 -> {
                assertAlign(address, 1, Trap.Cause.STORE_MISALIGNED);
                cpu.memory.store16(address, (short) value);
            }
            case 0b010 -> {
                assertAlign(address, 3, Trap.Cause.STORE_MISALIGNED);
                cpu.memory.store32(address, value);
            }
            default -> throw new Trap(Trap.Cause.ILLEGAL_INSN, inst.raw);
        }

        return nextPc;
    }

    public static int opimm(CPU cpu, Instruction inst, int nextPc, InstructionFormat format) throws Trap {
        var immediate = getImmediate(format, inst.raw);
        var r1 = cpu.registers.get(inst.rs1);

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

        cpu.registers.set(inst.rd, data);

        return nextPc;
    }

    public static int op(CPU cpu, Instruction inst, int nextPc, InstructionFormat format) throws Trap {
        var r1 = cpu.registers.get(inst.rs1);
        var r2 = cpu.registers.get(inst.rs2);
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

        cpu.registers.set(inst.rd, data);
        return nextPc;
    }

    public static int miscmem(CPU cpu, Instruction inst, int nextPc, InstructionFormat format) throws Trap {
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

    public static int system(CPU cpu, Instruction inst, int nextPc, InstructionFormat format) throws Trap {
        if (inst.funct3 != 0 || inst.rd != 0 || inst.rs1 != 0) {
            throw new Trap(Trap.Cause.ILLEGAL_INSN, inst.raw);
        }

        var immediate = getImmediate(format, inst.raw);
        return switch (immediate) {
            case 0 -> {
                cpu.pc = nextPc;
                cpu.ecall();
                yield cpu.pc;
            }
            case 1 -> throw new Trap(Trap.Cause.BREAKPOINT, 0);
            default -> throw new Trap(Trap.Cause.ILLEGAL_INSN, inst.raw);
        };
    }

    public record Instruction(int opcode, int rd, int funct3, int rs1, int rs2, int funct7, int raw) {
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
}
