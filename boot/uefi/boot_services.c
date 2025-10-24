/* boot_services.c - wrappers mínimos para Boot Services (placeholder)
   No EDK2, Boot Services são extensos; aqui só colocamos stubs. */

#include "efi_table.h"

void *uefi_allocate_pages(unsigned long pages) {
    /* stub - use Boot Services AllocatePages in real implementation */
    (void)pages;
    return 0;
}

void uefi_exit_boot_services(void *image, void *map_key) {
    /* stub */
    (void)image; (void)map_key;
}
