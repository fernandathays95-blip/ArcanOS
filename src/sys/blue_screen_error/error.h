#ifndef ARCANOS_FATAL_ERROR_H
#define ARCANOS_FATAL_ERROR_H

#include <stdint.h>

// Definição dos códigos de erro do ArcanOS (para diagnóstico rápido)
typedef enum {
    // 0x0000 - Erros de Hardware e Bootloader
    FATAL_BOOTLOADER_INIT_FAIL      = 0x0001,
    FATAL_MEMORY_CORRUPTION         = 0x0002,
    FATAL_ZIP_LOADER_INTEGRITY      = 0x0003, // Falha no checksum da parte 1!
    FATAL_KERNEL_LOAD_FAIL          = 0x0004,
    
    // 0x1000 - Erros de Kernel (Ex: Panic)
    FATAL_KERNEL_PANIC              = 0x1001,
    FATAL_UNHANDLED_EXCEPTION       = 0x1002,
    
    // 0x2000 - Erros de Sistema de Arquivos (FS)
    FATAL_FS_CORRUPTION             = 0x2001,
    FATAL_ROOT_MOUNT_FAIL           = 0x2002
    
} ArcanosErrorCode;

/**
 * @brief Função principal para lidar com erros fatais.
 * * Assume o controle total da tela (framebuffer) e exibe a mensagem de erro.
 * @param code O código de erro ArcanosErrorCode.
 * @param detail_address Um endereço de memória ou registro para debug (opcional).
 */
void fatal_error_handler(ArcanosErrorCode code, uint64_t detail_address);

#endif // ARCANOS_FATAL_ERROR_H
