#ifndef ARCANOS_KERNEL_MONITOR_H
#define ARCANOS_KERNEL_MONITOR_H

#include <stdint.h>
#include <stdbool.h>

// Estrutura para rastrear um subsistema monitorado
typedef struct {
    uint32_t id;                // ID único do subsistema (ex: ZIP Loader, Scheduler)
    uint64_t last_check_time;   // Tempo da última vez que o subsistema reportou
    uint64_t max_delay_ms;      // Atraso máximo permitido antes de acionar o pânico
    const char *name;           // Nome do subsistema
} SubsystemTracker;

// =======================================================
// Funções do Kernel Monitor
// =======================================================

/**
 * @brief Inicializa o Kernel Monitor e o Watchdog de hardware.
 */
void km_init_monitor();

/**
 * @brief Registra um novo subsistema para ser monitorado.
 * @param tracker A estrutura do subsistema a ser registrada.
 */
void km_register_subsystem(SubsystemTracker *tracker);

/**
 * @brief O subsistema reporta que está vivo (reseta seu timer).
 * @param id O ID do subsistema.
 */
void km_i_am_alive(uint32_t id);

#endif // ARCANOS_KERNEL_MONITOR_H
