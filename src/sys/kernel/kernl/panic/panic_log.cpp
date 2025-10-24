// src/sys/kernel/kernl/panic/panic_log.cpp
// Lógica de log e formatação do kernel panic.

#include "panic.h"
#include <iostream>
#include <sstream>

void panic_log_state(const PanicContext& context, const char* message) {
    // 1. Envia a mensagem de pânico para o console serial/log de baixo nível
    std::cerr << "!!! ARCANOS KERNEL PANIC !!!" << std::endl;
    std::cerr << "REASON: " << message << std::endl;

    // 2. Formata os dados críticos para o log (usando stringstream)
    std::stringstream ss;
    ss << "IP/PC: 0x" << std::hex << context.rip_or_pc << "\n";
    ss << "SP: 0x" << std::hex << context.stack_ptr << "\n";
    ss << "ERR CODE: 0x" << std::hex << context.error_code << "\n";
    
    // 3. Imprime a pilha (Stack Trace)
    // *******************************************************
    // LÓGICA DE STACK TRACE AQUI:
    // Mapearia endereços de volta para nomes de funções.
    // *******************************************************
    ss << "Stack Trace: (Not implemented yet)" << std::endl;

    // 4. Salva o log completo em um dump de memória (para análise post-mortem)
    // save_memory_dump(ss.str()); 
    
    std::cerr << "--- CONTEXT DUMP ---" << std::endl;
    std::cerr << ss.str() << std::endl;
}
