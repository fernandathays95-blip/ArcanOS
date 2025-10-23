#ifndef LIBBOOTLOADER_MEM_H
#define LIBBOOTLOADER_MEM_H

#include <stdint.h>
#include <stddef.h>

// Endereço base de onde o Kernel será carregado na RAM.
#define KERNEL_LOAD_ADDR 0x100000

// Funções de Gerenciamento de Memória Mínima
// Essas funções são cruciais para a inicialização da CPU.

/**
 * @brief Limpa um bloco de memória para um valor específico.
 * * @param dest Endereço inicial da memória.
 * @param val Valor a ser escrito em cada byte.
 * @param count Número de bytes a serem limpos.
 */
void bl_memset(void *dest, int val, size_t count);

/**
 * @brief Copia dados de uma região de memória para outra.
 * * @param dest Endereço de destino.
 * @param src Endereço de origem.
 * @param count Número de bytes a serem copiados.
 */
void bl_memcpy(void *dest, const void *src, size_t count);

#endif // LIBBOOTLOADER_MEM_H
