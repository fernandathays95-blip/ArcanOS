// src/bootloader/arch/arm64/init.c
// Funções de inicialização específicas para arquitetura ARM64 (Celulares)

#include "../../libbootloader/mem.h" // Funções de memória

/**
 * @brief Ponto de entrada do bootloader para ARM64.
 * * Esta função é responsável por:
 * 1. Configurar o modo de exceção (EL1 ou EL2).
 * 2. Inicializar o mapeamento de memória (MMU) e paginação.
 * 3. Configurar a porta serial/console para debug (UART).
 * 4. Chamar o carregador de Kernel.
 */
void arch_init_arm64() {
    // 1. Configurar Paging / MMU
    // ... Código de baixo nível de Assembly/C ...

    // 2. Inicializar I/O (para debug)
    // ... Código para configurar o UART ...
    
    // 3. Limpar BSS e Pilha
    // bl_memset(...); 

    // 4. Chamar o carregador principal (Core Loader)
    // core_loader_load_kernel(); 
}
