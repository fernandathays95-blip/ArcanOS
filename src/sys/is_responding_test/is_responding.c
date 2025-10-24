// src/sys/is_responding_test/is_responding.c
// Implementação da execução e verificação de testes de responsividade.

#include "is_responding.h"
#include "../kernel/kernl/printk/printk.h" // Para logging
#include "../kernel/kernl/kernel_monitor/monitor.h" // Para integração com o Monitor

#define MAX_TESTS 15
static ComponentResponseTest registered_tests[MAX_TESTS];
static uint32_t test_count = 0;


void irt_register_test(ComponentResponseTest *test) {
    if (test_count < MAX_TESTS) {
        test->id = test_count + 1;
        registered_tests[test_count] = *test; // Copia a estrutura
        test_count++;
        
        // Opcional: Integrar com o Kernel Monitor (watchdog)
        // Criar um SubsystemTracker e registrar no km_register_subsystem
        
        KERN_INFO("IR Test: Componente '%s' registrado (ID: %u).", test->name, test->id);
    } else {
        KERN_ERR("IR Test: Falha ao registrar '%s'. Limite de testes atingido.", test->name);
    }
}


bool irt_check_all_components() {
    bool all_ok = true;
    
    for (uint32_t i = 0; i < test_count; i++) {
        ComponentResponseTest *test = &registered_tests[i];
        
        uint64_t start_time = arch_get_current_ms();
        
        // EXECUÇÃO DO TESTE
        bool is_responsive = test->test_function();
        
        uint64_t end_time = arch_get_current_ms();
        uint64_t latency = end_time - start_time;
        
        if (!is_responsive) {
            KERN_CRIT("IR Test: Componente '%s' (ID %u) FALHOU no teste de responsividade.", 
                      test->name, test->id);
            all_ok = false;
        } else if (latency > test->max_latency_ms) {
            KERN_WARNING("IR Test: Componente '%s' LENTO (%llu ms). Máximo %llu ms.", 
                         test->name, latency, test->max_latency_ms);
        } else {
            KERN_DEBUG("IR Test: Componente '%s' OK (%llu ms).", test->name, latency);
        }
    }
    
    return all_ok;
}

// Funções externas simuladas:
extern uint64_t arch_get_current_ms() { return 0; }
