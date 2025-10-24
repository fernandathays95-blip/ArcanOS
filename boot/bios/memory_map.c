/* memory_map.c - consulta mapa de memória via int 0x15, e820 (template)
   Gera saída simples para debug durante build. */

#include <stdint.h>

struct e820_entry {
    uint64_t base;
    uint64_t length;
    uint32_t type;
    uint32_t acpi;
} __attribute__((packed));

typedef int (*e820_cb)(struct e820_entry *e);

int query_e820(e820_cb cb) {
    uint32_t cont = 0;
    struct e820_entry e;
    for (;;) {
        uint32_t eax = 0xE820;
        uint32_t ebx = cont;
        uint32_t ecx = sizeof(e);
        uint32_t edx = 0x534D4150; /* 'SMAP' */
        uint16_t es = 0; /* callback buffer segment */
        uint32_t carry = 0;

        /* Em ambiente real isso deve usar int 0x15; aqui é apenas placeholder */
        /* Chamada real: int 0x15 AH=0xE820 */
        (void)eax; (void)ebx; (void)ecx; (void)edx; (void)es;

        /* Simular fim */
        break;
    }
    return 0;
}
