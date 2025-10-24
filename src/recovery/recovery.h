#ifndef ARCANOS_RECOVERY_H
#define ARCANOS_RECOVERY_H

#include <stdint.h>
#include <stdbool.h>

// Inclui o Menu Mode (UI de texto ou gráfico simples)
#include "../arm64/Menu_mode/ui/menu_ui.h" 

// ----------------------------------------------------------------------
// Códigos de Modo de Recuperação (Para o Kernel saber o que fazer após o reset)
// ----------------------------------------------------------------------
typedef enum {
    RECOVERY_MODE_NORMAL_BOOT = 0, // Reinicialização padrão (sem erro)
    RECOVERY_MODE_FORCED_MENU = 1, // Menu ativado por botão de hardware (Vol Up + Power)
    RECOVERY_MODE_KERNEL_FAIL = 2, // Ativado após um Kernel Panic (modo forçado)
    RECOVERY_MODE_WIPE_DATA   = 3  // Código de ação de limpeza de dados
} RecoveryStartMode;


// ----------------------------------------------------------------------
// Funções Principais
// ----------------------------------------------------------------------

/**
 * @brief Inicia o ambiente de recuperação ArcanOS.
 * * Este é o ponto de entrada quando o sistema detecta a necessidade de recuperação.
 * @param start_mode O modo pelo qual a recuperação foi ativada.
 * @return O código de ação solicitado pelo usuário ou -1 se a recuperação falhar.
 */
int recovery_main(RecoveryStartMode start_mode);

/**
 * @brief Chama a rotina de execução de Menu Mode.
 * * @return O código de ação (ex: ACTION_REBOOT_NORMAL, ACTION_SHUTDOWN).
 */
int recovery_run_menu(void);

#endif // ARCANOS_RECOVERY_H
