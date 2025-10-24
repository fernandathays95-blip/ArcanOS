#ifndef ARCANOS_KERNEL_PRINTK_H
#define ARCANOS_KERNEL_PRINTK_H

#include <stdarg.h>

// Níveis de Log (Prioridade)
typedef enum {
    LOG_EMERG   = 0, // Emergência: O sistema está inutilizável.
    LOG_ALERT   = 1, // Alerta: Ação deve ser tomada imediatamente.
    LOG_CRIT    = 2, // Crítico: Erros de hardware ou software sérios.
    LOG_ERR     = 3, // Erro: Erros de execução.
    LOG_WARNING = 4, // Aviso: Condição anormal.
    LOG_NOTICE  = 5, // Notificação: Condições normais, mas significativas.
    LOG_INFO    = 6, // Informativo: Mensagens gerais.
    LOG_DEBUG   = 7  // Debug: Mensagens para debug.
} LogLevel;

// Função principal de formatação e log
void kernel_log(LogLevel level, const char *fmt, ...);

// Macro de conveniência para ser usada em todo o Kernel
#define KERN_EMERG(fmt, ...) kernel_log(LOG_EMERG, fmt, ##__VA_ARGS__)
#define KERN_ERR(fmt, ...)   kernel_log(LOG_ERR, fmt, ##__VA_ARGS__)
#define KERN_INFO(fmt, ...)  kernel_log(LOG_INFO, fmt, ##__VA_ARGS__)
#define KERN_DEBUG(fmt, ...) kernel_log(LOG_DEBUG, fmt, ##__VA_ARGS__)

#endif // ARCANOS_KERNEL_PRINTK_H
