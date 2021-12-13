package riscv;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class InstructionPrinterTest {

    private String randomName(Random random) {
        StringBuilder s = new StringBuilder();
        int len = random.nextInt(10);
        for (int i = 0; i < len; i++) {
            s.append((char) random.nextInt());
        }
        return s.toString();
    }

    @Test
    void print() {
        Random random = new Random(0);

        assertEquals("00000000             ECALL zero, zero, 0",
                InstructionPrinter.print(new Instruction(new ProtoInstruction(InstructionType.I,
                        "ECALL", 0b1110011, 0, 0),
                        0, 0, 0, 0)));

        for (int i = 0; i < 100; i++) {
            int rd = random.nextInt(32);
            int r1 = random.nextInt(32);
            int r2 = random.nextInt(32);
            int imm = random.nextInt(1 << 11);
            int address = random.nextInt(Integer.MAX_VALUE);
            String name = randomName(random);

            Instruction instrR = new Instruction(new ProtoInstruction(InstructionType.R,
                    name, random.nextInt(), random.nextInt(), random.nextInt()),
                    rd, r1, r2, imm);
            instrR.setAddress(address);

            assertEquals(String.format("%08x             %s %s, %s, %s", address,
                    name,
                    RegisterNamingConverter.toAbi(rd),
                    RegisterNamingConverter.toAbi(r1),
                    RegisterNamingConverter.toAbi(r2)),
                    InstructionPrinter.print(instrR));

            Instruction instrI = new Instruction(new ProtoInstruction(InstructionType.I,
                    name, random.nextInt(), random.nextInt(), random.nextInt()),
                    rd, r1, r2, imm);
            instrI.setAddress(address);

            assertEquals(String.format("%08x             %s %s, %s, %d", address,
                    name,
                    RegisterNamingConverter.toAbi(rd),
                    RegisterNamingConverter.toAbi(r1),
                    imm),
                    InstructionPrinter.print(instrI));

            Instruction instrS = new Instruction(new ProtoInstruction(
                    random.nextBoolean() ? InstructionType.S : InstructionType.B,
                    name, random.nextInt(), random.nextInt(), random.nextInt()),
                    rd, r1, r2, imm);
            instrS.setAddress(address);

            assertEquals(String.format("%08x             %s %s, %s, %d", address,
                    name,
                    RegisterNamingConverter.toAbi(r1),
                    RegisterNamingConverter.toAbi(r2),
                    imm),
                    InstructionPrinter.print(instrS));

            imm = random.nextInt() << 12;
            Instruction instrU = new Instruction(new ProtoInstruction(
                    random.nextBoolean() ? InstructionType.U : InstructionType.J,
                    name, random.nextInt(), random.nextInt(), random.nextInt()),
                    rd, r1, r2, imm);
            instrU.setAddress(address);

            assertEquals(String.format("%08x             %s %s, %d", address,
                    name,
                    RegisterNamingConverter.toAbi(rd),
                    imm),
                    InstructionPrinter.print(instrU));


            assertEquals(String.format("%08x             %s %s, %s, %s", 0,
                    "CSRRW",
                    RegisterNamingConverter.toAbi(rd),
                    "fcsr",
                    RegisterNamingConverter.toAbi(r1)),
                    InstructionPrinter.print(new Instruction(new ProtoInstruction(InstructionType.I,
                            "CSRRW", 0b1110011, 0b001),
                            rd, r1, r2, 0x003)));

            assertEquals(String.format("%08x             %s %s, %s, %s", 0,
                    "CSRRCI",
                    RegisterNamingConverter.toAbi(rd),
                    "cycle",
                    r1), // = zimm
                    InstructionPrinter.print(new Instruction(new ProtoInstruction(InstructionType.I,
                            "CSRRCI", 0b1110011, 0b111),
                            rd, r1, r2, 0xC00)));
        }
    }
}