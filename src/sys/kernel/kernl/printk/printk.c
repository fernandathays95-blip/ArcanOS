// src/sys/kernel/kernl/printk/printk.c
// Implementação do sistema de log do Kernel (printk).

#include "printk.h"
#include <stdio.h>
#include <string.h>

// Funções de I/O de baixo nível
extern void arch_console_putc(char c); // Escreve no console de vídeo ou serial

// Buffer para formatar a string
#define PRINTK_BUFFER_SIZE 256
static char printk_buffer[PRINTK_BUFFER_SIZE];

// Mapeamento dos níveis para prefixos
const char *log_prefixes[] = {
    [LOG_EMERG]   = "<0>EMERG: ",
    [LOG_ALERT]   = "<1>ALERT: ",
    [LOG_CRIT]    = "<2>CRIT: ",
    [LOG_ERR]     = "<3>ERROR: ",
    [LOG_WARNING] = "<4>WARN: ",
    [LOG_NOTICE]  = "<5>NOTICE: ",
    [LOG_INFO]    = "<6>INFO: ",
    [LOG_DEBUG]   = "<7>DEBUG: "
};

void printk_write_string(const char *str) {
    while (*str) {
        arch_console_putc(*str++);
    }
}

void kernel_log(LogLevel level, const char *fmt, ...) {
    va_list args;

    // 1. Adiciona o prefixo (nível de log)
    if (level <= LOG_DEBUG) {
        printk_write_string(log_prefixes[level]);
    }
    
    // 2. Formata a mensagem principal usando vsnprintf (versão minimalista)
    va_start(args, fmt);
    
    // Usamos vsnprintf (ou uma versão minimalista do kernel)
    // snprintf(printk_buffer, PRINTK_BUFFER_SIZE, fmt, args);
    // Para simplificação no esboço, apenas marcamos o buffer.
    strncpy(printk_buffer, fmt, PRINTK_BUFFER_SIZE);
    
    va_end(args);

    // 3. Roteia a saída para o console de baixo nível
    printk_write_string(printk_buffer);
    printk_write_string("\n"); // Nova linha
}

// Função de hardware simulada
void arch_console_putc(char c) {
    // No código real, esta função escreveria para a porta serial (x86_64) 
    // ou para o UART/FrameBuffer (ARM64).
}
