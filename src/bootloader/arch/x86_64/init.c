// src/bootloader/arch/x86_64/init.c
// Funções de inicialização específicas para arquitetura x86_64 (Servidores)

#include "../../libbootloader/mem.h" // Funções de memória

/**
 * @brief Ponto de entrada do bootloader para x86_64.
 * * Esta função é responsável por:
 * 1. Mudar do modo Real Mode (16-bit) para Protected Mode (32-bit).
 * 2. Mudar de 32-bit para Long Mode (64-bit).
 * 3. Configurar tabelas GDT/IDT.
 * 4. Inicializar o mapeamento de memória (Paging) e configurar ACPI/APIC.
 * 5. Chamar o carregador de Kernel.
 */
void arch_init_x86_64() {
    // 1. Transição de modo (assembly)
    // ... Chamadas de funções de Assembly ...

    // 2. Configurar tabelas GDT/IDT
    // ... Código para configurar descritores ...
    
    // 3. Inicializar I/O e memória
    // bl_memset(...);

    // 4. Chamar o carregador principal (Core Loader)
    // core_loader_load_kernel(); 
}
