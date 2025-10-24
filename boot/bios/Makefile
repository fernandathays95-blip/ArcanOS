# Makefile para BIOS components
NASM=nasm
ASMFLAGS=-f bin
OUTDIR=.

all: init.bin bootloader.bin

init.bin: init.asm
	$(NASM) $(ASMFLAGS) init.asm -o init.bin

bootloader.bin: bootloader.asm
	$(NASM) $(ASMFLAGS) bootloader.asm -o bootloader.bin

clean:
	rm -f init.bin bootloader.bin
