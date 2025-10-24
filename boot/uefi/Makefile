# Makefile minimal (nota: para criar um .efi real, use EDK2/gnu-efi)
CC=x86_64-elf-gcc
CFLAGS=-ffreestanding -fno-builtin -fshort-wchar -I.

all: entry.o boot_services.o runtime_services.o
	@echo "Objects ready. Use EDK2 or gnu-efi to produce a .efi binary."

entry.o: entry_uefi.c efi_table.h
	$(CC) $(CFLAGS) -c entry_uefi.c -o entry.o

boot_services.o: boot_services.c efi_table.h
	$(CC) $(CFLAGS) -c boot_services.c -o boot_services.o

runtime_services.o: runtime_services.c efi_table.h
	$(CC) $(CFLAGS) $(CFLAGS) -c runtime_services.c -o runtime_services.o

clean:
	rm -f *.o
