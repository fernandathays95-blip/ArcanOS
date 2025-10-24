// src/sys/kernel/kernl/kernel_monitor/watchdog.c
// Implementação do Watchdog de Software/Hardware.

#include "monitor.h"
#include <stdio.h>
#include <string.h> // Para memcmp/memset

// Funções externas do sistema
extern void arch_reset_watchdog_timer();
extern void kernel_panic(const char* message);

// Array de subsistemas monitorados (tamanho fixo para simplificação)
#define MAX_SUBSYSTEMS 10
static SubsystemTracker tracked_subsytems[MAX_SUBSYSTEMS];
static uint32_t next_id = 1;


void km_init_monitor() {
    // 1. Limpa a lista de rastreamento
    memset(tracked_subsytems, 0, sizeof(tracked_subsytems));
    
    // 2. Inicializa o timer de Watchdog de Hardware (se disponível na arquitetura)
    // arch_init_watchdog(); 
    
    printf("Kernel Monitor: Watchdog inicializado.\n");
}

void km_register_subsystem(SubsystemTracker *tracker) {
    if (next_id < MAX_SUBSYSTEMS) {
        tracker->id = next_id++;
        // Copia a estrutura (e assume que o tracker->name é uma string estática)
        memcpy(&tracked_subsytems[tracker->id - 1], tracker, sizeof(SubsystemTracker));
        printf("Monitor: Subsistema '%s' (ID %u) registrado.\n", tracker->name, tracker->id);
    }
}

void km_i_am_alive(uint32_t id) {
    if (id > 0 && id <= MAX_SUBSYSTEMS) {
        tracked_subsytems[id - 1].last_check_time = arch_get_current_ms();
    }
    // Alimentar o Watchdog de Hardware para evitar um reset forçado.
    arch_reset_watchdog_timer(); 
}

// =======================================================
// Função de Verificação (Chamada em um timer de baixa prioridade)
// =======================================================
void km_check_state() {
    uint64_t current_time = arch_get_current_ms();
    
    for (int i = 0; i < MAX_SUBSYSTEMS; i++) {
        SubsystemTracker *tracker = &tracked_subsytems[i];
        
        if (tracker->id != 0) {
            uint64_t elapsed = current_time - tracker->last_check_time;
            
            if (elapsed > tracker->max_delay_ms) {
                // VIOLAÇÃO: O subsistema demorou muito para responder!
                printf("CRITICAL: Subsistema '%s' (ID %u) falhou em responder (%llu ms).\n", 
                       tracker->name, tracker->id, elapsed);
                       
                // Aciona o Kernel Panic para uma tela de erro elegante.
                kernel_panic("Kernel Monitor Timeout: System Unresponsive");
            }
        }
    }
}

// Funções externas simuladas:
extern uint64_t arch_get_current_ms() { return 0; }
