#ifndef ARCANOS_RECOVERY_H
#define ARCANOS_RECOVERY_H

#include <stdint.h>
#include <stdbool.h>

// Interfaces de Subsistema (Assumidas)
#include "../arm64/Menu_mode/ui/menu_ui.h" 
#include "../sys/drivers/input/touch_driver.h" 
#include "../sys/kernel/kernl/printk/printk.h" 

// Definição das Ações de Saída do Menu (para o Kernel)
// Estes valores são retornados pelo recovery_main()
typedef enum {
    ACTION_REBOOT_NORMAL  = 0x0000, // Reinicialização padrão do sistema
    ACTION_SHUTDOWN       = 0x0001, // Desligamento imediato
    ACTION_WIPE_DATA      = 0x1000, // Limpar dados do usuário (Factory Reset)
    ACTION_REINSTALL_OS   = 0x1001, // Iniciar rotina de reinstalação (via USB/Rede)
    ACTION_DIAGNOSTICS    = 0x1002  // Executar conjunto de testes de hardware
} RecoveryActionCode;

// Códigos de Modo de Recuperação (Para o Kernel saber por que Recovery foi ativado)
typedef enum {
    RECOVERY_MODE_NORMAL_BOOT = 0, // Reinicialização padrão
    RECOVERY_MODE_FORCED_MENU = 1, // Menu ativado por botão de hardware (Vol Up + Power)
    RECOVERY_MODE_KERNEL_FAIL = 2, // Ativado após um Kernel Panic
    RECOVERY_MODE_WIPE_DATA   = 3  // Código de ação de limpeza de dados
} RecoveryStartMode;


// ----------------------------------------------------------------------
// Funções Principais
// ----------------------------------------------------------------------

/**
 * @brief Inicializa hardware mínimo necessário para o Recovery Mode (Gráfico, Input).
 */
void recovery_init_hardware(void);

/**
 * @brief Orquestra o loop de interface Menu Mode.
 * @return O código de ação (RecoveryActionCode) solicitado pelo usuário.
 */
int recovery_run_menu(void);

/**
 * @brief Ponto de entrada principal para o ambiente de recuperação ArcanOS.
 * @param start_mode O modo pelo qual a recuperação foi ativada.
 * @return O código de ação final (RecoveryActionCode).
 */
int recovery_main(RecoveryStartMode start_mode);

#endif // ARCANOS_RECOVERY_H
