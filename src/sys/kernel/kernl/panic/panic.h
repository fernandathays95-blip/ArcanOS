#ifndef ARCANOS_KERNEL_PANIC_H
#define ARCANOS_KERNEL_PANIC_H

#include <string>
#include <cstdint>

// Estrutura para salvar o estado da CPU no momento do pânico.
// O conteúdo real é específico da arquitetura (x86_64, ARM64).
struct PanicContext {
    uint64_t rip_or_pc;   // Instruction Pointer (x86_64) ou Program Counter (ARM64)
    uint64_t stack_ptr;   // Stack Pointer
    uint64_t error_code;  // Código de erro da exceção/interrupção
    // ... outros registradores essenciais
};

// =======================================================
// A função principal chamada por qualquer subsistema do Kernel
// =======================================================
/**
 * @brief Inicia o processo de pânico do kernel, coleta o contexto e encerra o sistema.
 * @param message Uma string de alto nível descrevendo a causa do pânico.
 */
extern "C" void kernel_panic(const char* message);

#endif // ARCANOS_KERNEL_PANIC_H
