#ifndef ARCANOS_SYS_REGISTER_H
#define ARCANOS_SYS_REGISTER_H

#include <stdint.h>

// ----------------------------------------------------
// 1. Tipo de Função de Registro (Sua intenção original)
// ----------------------------------------------------

/**
 * @brief O tipo de função que um subsistema deve fornecer para se registrar no Kernel.
 * * Funções desse tipo são executadas uma vez durante a inicialização do Kernel.
 */
typedef void (*SysInitFn)(void);

// ----------------------------------------------------
// 2. Interface de Registro
// ----------------------------------------------------

/**
 * @brief Registra um subsistema para inicialização tardia.
 * * @param init_func O ponteiro para a função de inicialização do subsistema.
 */
void sys_register_init_fn(SysInitFn init_func);

/**
 * @brief Executa todas as funções de inicialização registradas.
 * * Chamado uma vez no final do boot do Kernel.
 */
void sys_run_init_routines(void);

#endif // ARCANOS_SYS_REGISTER_H
