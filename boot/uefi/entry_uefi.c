/* entry_uefi.c - entrada mínima UEFI para ArcanOS
   Compilar com: x86_64-elf-gcc -ffreestanding -fshort-wchar -c entry_uefi.c
   Linkar como PE/COFF UEFI app (EDK2 recommended). */

#include "efi_table.h"

EFI_STATUS efi_main(EFI_HANDLE ImageHandle, EFI_SYSTEM_TABLE *SystemTable) {
    const CHAR16 msg[] = { 'A','r','c','a','n','O','S',' ', 'U','E','F','I',' ','B','o','o','t','l','o','a','d','e','r',0x0D,0x0A,0 };
    if (SystemTable && SystemTable->ConOut && SystemTable->ConOut->OutputString) {
        SystemTable->ConOut->OutputString(SystemTable->ConOut, (CHAR16*)msg);
    }
    /* Aqui você chamaria serviços de boot e carregaria o kernel ELF/PE */
    return EFI_SUCCESS;
}

/* For EDK2 the entry point name is different; this is minimal example. */
