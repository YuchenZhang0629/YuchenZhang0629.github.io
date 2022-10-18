// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
//
// This program only needs to handle arguments that satisfy
// R0 >= 0, R1 >= 0, and R0*R1 < 32768.

// Put your code here.


// Initialize the answer to 0
@2
M=0

// If either of the input is 0, then skip the loop and go directly to the end
@0
D=M
@END
D;JEQ
@1
D=M
@END
D;JEQ

// Put one of the multipliers on another RAM register
@0
D=M
@3
M=D

(LOOP)
@1
D=M
@2
M=M+D
@3
M=M-1
D=M
@LOOP
D;JGT

(END)
@END
D,JMP