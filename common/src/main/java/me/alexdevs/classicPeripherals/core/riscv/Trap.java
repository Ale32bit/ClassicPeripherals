package me.alexdevs.classicPeripherals.core.riscv;

public final class Trap extends Exception {
    public enum Cause {
        INSN_MISALIGNED(0, "instruction address misaligned"),
        INSN_ACCESS_FAULT(1,"instruction access fault"),
        ILLEGAL_INSN(2,"illegal instruction"),
        BREAKPOINT(3,"breakpoint"),
        LOAD_MISALIGNED(4,"load address misaligned"),
        LOAD_ACCESS_FAULT(5,"load access fault"),
        STORE_MISALIGNED(6,"store address misaligned"),
        STORE_ACCESS_FAULT(7,"store access fault"),
        ECALL_U(8,"environment call from U-mode"),
        ECALL_S(9,"environment call from S-mode"),
        ECALL_M(1,"environment call from M-mode"),;

        public final int code;
        public final String name;
        Cause(int code, String name) {
            this.code = code;
            this.name = name;
        }
    }

    public final Cause cause;
    public final int value;

    public Trap(Cause cause, int value) {
        super(null, null, false, false); // no suppression, no stack trace
        this.cause = cause;
        this.value = value;
    }

    public Trap(Cause cause) {
        this(cause, 0);
    }

    public boolean isEnvironmentCall() {
        return cause == Cause.ECALL_U || cause == Cause.ECALL_S || cause == Cause.ECALL_M;
    }

    @Override
    public String getMessage() {
        return cause.name + " (mcause=" + cause.code
                + ", mtval=0x" + Integer.toHexString(value) + ")";
    }
}
