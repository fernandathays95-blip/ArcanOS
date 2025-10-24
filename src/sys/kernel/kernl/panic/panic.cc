// src/sys/kernel/kernl/panic/panic.cc
// Código de baixo nível para coleta de estado da CPU no pânico.

#include "panic.h"
#include "../../../sys/blue_screen_error/error.h" // Inclui a tela de erro fatal
#include <iostream>

// Funções de log do pânico (implementadas em panic_log.cpp)
void panic_log_state(const PanicContext& context, const char* message);

// Funções de Assembly para capturar o contexto
extern "C" PanicContext capture_context(); 

extern "C" void kernel_panic(const char* message) {
    // 1. Desabilita interrupções para evitar corrupção adicional
    __asm__ volatile ("cli"); 

    // 2. Captura o contexto da CPU (registradores, pilha, etc.)
    PanicContext ctx = capture_context(); 

    // 3. Loga o pânico e o contexto (Lógica em panic_log.cpp)
    panic_log_state(ctx, message);
    
    // 4. Chama o handler de erro fatal (o 'blue screen' do ArcanOS)
    // Usamos um código de erro genérico de pânico (0x1001) e o IP para debug.
    fatal_error_handler(FATAL_KERNEL_PANIC, ctx.rip_or_pc); 
    
    // Se o fatal_error_handler retornar por algum motivo, entramos em loop
    while(1); 
}

// Simulação da função de Assembly (no código real, seria Assembly)
PanicContext capture_context() {
    PanicContext ctx;
    ctx.rip_or_pc = 0xDEADBEEF; // Valor de placeholder
    ctx.stack_ptr = 0xBADC0DE;
    ctx.error_code = 0x0;
    return ctx;
}
