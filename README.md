# elf64 risc-v disassembler

This repo is solution for one of homeworks in ITMO university Computer Tech course.
The goal of this code is to convert elf64 binary file compiled for risc-v architecture to human readable (as it can be) assembler code.

The main reference for this project was [risc-v documentation](https://riscv.org/wp-content/uploads/2017/05/riscv-spec-v2.2.pdf).

Some code generation was used to convert table from documetation mentioned above to java file with list of objects representing table entities. You can find it in ./scripts

## How it works
General pipeline:

read input file -> decode header with elf.\*\*\*Reader classes -> 
find and decode text section (instructions) -> decode each instruction by determining its type, operation code and etc. ->
label connections between instructions (jumps) -> format instructions -> write to output file

## Example output (part of out.txt, result of run on one of samples)
```
.text
00010074 register_fini: addi a5, zero, 0
00010078             c.beqz a5, LOC_00000
0001007a             c.lui a0, 65536
0001007c             addi a0, a0, 894
00010080             c.j atexit
00010082  LOC_00000: c.jr ra
00010084     _start: auipc gp, 8192
00010088             addi gp, gp, -964
0001008c             addi a0, gp, -972
00010090             addi a2, gp, -944
00010094             c.sub a2, a2, a0
00010096             c.li a1, 0
00010098             c.jal memset
0001009a             auipc a0, 0
0001009e             addi a0, a0, 792
000100a2             c.beqz a0, LOC_00001
000100a4             auipc a0, 0
000100a8             addi a0, a0, 730
000100ac             c.jal atexit
000100ae  LOC_00001: c.jal __libc_init_array
000100b0             c.lwsp a0, 0(sp)
000100b2             c.addi4spn a1, sp, 4
000100b4             c.li a2, 0
000100b6             c.jal main
000100b8             c.j exit
```

### PS
This repository is only temporary public, because it contains solution to homework which can be given to students in the next academic year.
