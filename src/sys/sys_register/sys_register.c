// src/sys/sys_register/sys_register.c
// Implementação do sistema de registro de inicialização de subsistemas.

#include "sys_register.h"
#include "../kernel/kernl/printk/printk.h" // Para KERN_INFO

#define MAX_SYS_INIT_FNS 32 // Limite máximo de subsistemas que podem se registrar

// O array para armazenar os ponteiros de função de registro
static SysInitFn registered_init_fns[MAX_SYS_INIT_FNS] = {0};
static int current_fn_count = 0;


void sys_register_init_fn(SysInitFn init_func) {
    if (init_func == NULL) {
        KERN_ERR("SYS_REG: Tentativa de registrar função NULL ignorada.");
        return;
    }
    
    if (current_fn_count < MAX_SYS_INIT_FNS) {
        // Armazena a função de inicialização no próximo slot disponível
        registered_init_fns[current_fn_count] = init_func;
        current_fn_count++;
        KERN_DEBUG("SYS_REG: Função registrada com sucesso. Total: %d.", current_fn_count);
    } else {
        KERN_CRIT("SYS_REG: Falha ao registrar subsistema. Limite de %d atingido!", MAX_SYS_INIT_FNS);
    }
}


void sys_run_init_routines(void) {
    KERN_INFO("SYS_REG: Iniciando rotinas de inicialização registradas (%d funções)...", current_fn_count);
    
    // Itera sobre todas as funções registradas e as executa
    for (int i = 0; i < current_fn_count; i++) {
        SysInitFn init_func = registered_init_fns[i];
        
        if (init_func != NULL) {
            // Executa a função. (O nome da função não é sabido aqui)
            init_func(); 
        }
    }
    
    KERN_INFO("SYS_REG: Todas as rotinas de inicialização concluídas.");
}
