// src/recovery/recovery.c
// Lógica principal do Ambiente de Recuperação ArcanOS.

#include "recovery.h"
#include "../sys/kernel/kernl/printk/printk.h" // Para KERN_INFO
#include <stdio.h>

int recovery_run_menu(void) {
    // Instancia o Menu Mode (que é em C++) e executa seu loop de interação.
    // O código de C chamaria a lógica de C++ aqui (via linkage 'extern "C"')
    
    KERN_INFO("RECOVERY: Iniciando Menu Mode UI...");
    
    // Simulação da execução da classe MenuUI (definida em C++)
    MenuUI menu_app;
    int action_code = menu_app.run_menu_loop();
    
    KERN_INFO("RECOVERY: Menu Mode encerrado. Código de Ação: %d.", action_code);
    return action_code;
}

int recovery_main(RecoveryStartMode start_mode) {
    KERN_EMERG("=========================================");
    KERN_INFO("RECOVERY: ArcanOS Recovery Mode ativado.");
    
    const char* reason = "Normal";
    if (start_mode == RECOVERY_MODE_FORCED_MENU) reason = "Hardware Button";
    if (start_mode == RECOVERY_MODE_KERNEL_FAIL) reason = "Kernel Panic Auto-Start";

    KERN_INFO("RECOVERY: Modo de Início: %s (Code: %d).", reason, start_mode);
    KERN_EMERG("=========================================");

    // Se a recuperação foi acionada por um erro, tentamos diagnosticar/reparar.
    if (start_mode == RECOVERY_MODE_KERNEL_FAIL) {
        KERN_WARNING("RECOVERY: Tentando reparo automático de FS...");
        // auto_repair_filesystem();
    }
    
    // Inicia o Menu principal para interação do usuário
    int final_action = recovery_run_menu();
    
    // Após a saída do menu, o kernel tomará a ação final (reboot, shutdown, wipe).
    return final_action; 
}
