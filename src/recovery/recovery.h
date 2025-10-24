// src/recovery/recovery.h (Atualização)

#ifndef ARCANOS_RECOVERY_H
#define ARCANOS_RECOVERY_H

#include <stdint.h>
#include <stdbool.h>

// Inclui o Menu Mode (UI de texto ou gráfico simples)
#include "../arm64/Menu_mode/ui/menu_ui.h" 

// Inclui o driver de entrada de baixo nível (assumindo que ele está em sys/drivers)
#include "../sys/drivers/input/touch_driver.h" 

// ... (Restante do enum RecoveryStartMode é o mesmo) ...

// Funções de Inicialização de Hardware
void recovery_init_hardware(void);

// ... (Restante das Funções Principais é o mesmo) ...

#endif // ARCANOS_RECOVERY_H
