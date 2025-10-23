#include "mem.h" // Inclui as funções de memória definidas acima
#include <stdint.h>
#include <stddef.h>

// =======================================================
// Implementação de Funções de Memória (para ser usada pelo bootloader)
// Estas são implementações simples, sem otimizações de cache da CPU (por enquanto).
// =======================================================

void bl_memset(void *dest, int val, size_t count) {
    char *d = (char *)dest;
    while (count--) {
        *d++ = (char)val;
    }
}

void bl_memcpy(void *dest, const void *src, size_t count) {
    char *d = (char *)dest;
    const char *s = (const char *)src;
    while (count--) {
        *d++ = *s++;
    }
}

// =======================================================
// Funções de Utilitário Genéricas
// =======================================================

/**
 * @brief Função simples para imprimir uma string (no console serial, por exemplo).
 * (Requer uma função de I/O de baixo nível, que virá nos arquivos de arquitetura)
 */
void bl_print_str(const char *str) {
    // Note: A implementação de 'bl_putc' (colocar caractere na tela/serial) 
    // deve ser definida em arquivos específicos da arquitetura (x86_64 ou arm64).
    while (*str) {
        // bl_putc(*str); // Descomentar quando bl_putc for implementada
        str++;
    }
}

