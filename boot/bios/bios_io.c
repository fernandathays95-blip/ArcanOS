/* bios_io.c - funções helper usando interrupções BIOS (compilar com i386-elf-gcc ou similar)
   Observação: estes são helpers para usar em ferramentas de userspace do build. */

#include <stdint.h>

static inline void bios_print(const char *s) {
    while (*s) {
        uint8_t c = *s++;
        asm volatile (
            "movb $0x0E, %%ah\n\t"
            "movb %0, %%al\n\t"
            "int $0x10"
            :
            : "r"(c)
            : "ax"
        );
    }
}

static inline int bios_read_sector(uint8_t drive, uint8_t head, uint8_t track, uint8_t sector, void *buf) {
    int ret;
    asm volatile (
        "push %%es\n\t"
        "mov %%dx, %%ax\n\t"
        "movb $0x02, %%ah\n\t"   /* READ */
        "movb $1, %%al\n\t"      /* 1 setor */
        "int $0x13\n\t"
        "setc %0\n\t"
        "pop %%es\n\t"
        : "=r"(ret)
        : "d"(drive), "S"(buf), "c"(track), "b"(head), "D"(sector)
        : "ax"
    );
    return ret; /* 0 = ok, 1 = erro */
}
