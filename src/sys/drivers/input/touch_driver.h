// src/sys/drivers/input/touch_driver.h
// Interface mínima para o driver de touchscreen no modo de recuperação.

#ifndef ARCANOS_RECOVERY_TOUCH_DRIVER_H
#define ARCANOS_RECOVERY_TOUCH_DRIVER_H

#include <stdbool.h>

/**
 * @brief Inicializa o driver de touchscreen.
 * * Este driver deve ser uma versão mínima que não depende do Kernel completo.
 * @return TRUE se o driver foi iniciado com sucesso, FALSE caso contrário.
 */
bool recovery_touch_init(void);

/**
 * @brief Verifica se há um evento de toque pendente.
 * @param x Ponteiro para armazenar a coordenada X.
 * @param y Ponteiro para armazenar a coordenada Y.
 * @return TRUE se um toque for detectado, FALSE caso contrário.
 */
bool recovery_get_touch_event(int* x, int* y);

#endif // ARCANOS_RECOVERY_TOUCH_DRIVER_H
