// src/recovery/recovery.c
// Lógica principal e orquestração do Ambiente de Recuperação ArcanOS.

#include "recovery.h"
#include <stdio.h> // Para printf (via KERN_INFO/ERR)

// ----------------------------------------------------------------------
// Funções de Inicialização de Hardware e Ambiente
// ----------------------------------------------------------------------

// Função externa simulada para inicializar gráficos simples (Framebuffer)
extern void arch_init_simple_graphics(); 

void recovery_init_hardware(void) {
    KERN_INFO("RECOVERY: Inicializando hardware essencial...");

    // 1. Inicialização Gráfica (FrameBuffer/Console simples)
    // Isso garante que o Menu Mode possa desenhar na tela.
    arch_init_simple_graphics();
    KERN_INFO("RECOVERY: Subsistema gráfico básico ativado.");

    // 2. Inicialização do Driver de Touchscreen (CRÍTICO para UI)
    if (recovery_touch_init()) {
        KERN_INFO("RECOVERY: Touchscreen ativado com sucesso.");
    } else {
        KERN_WARNING("RECOVERY: Falha ao inicializar Touchscreen. Apenas botões/teclado disponíveis.");
    }
}

// ----------------------------------------------------------------------
// Funções de Menu e Lógica de Alto Nível
// ----------------------------------------------------------------------

int recovery_run_menu(void) {
    // Note: A MenuUI é uma classe C++ que depende dos drivers de input inicializados.
    
    KERN_INFO("RECOVERY: Iniciando Menu Mode UI...");
    
    // Instancia e executa o Menu Mode (MenuUI deve ser extern "C" ou ter um wrapper)
    MenuUI menu_app;
    int action_code = menu_app.run_menu_loop();
    
    KERN_INFO("RECOVERY: Menu Mode encerrado. Código de Ação: 0x%04X.", action_code);
    return action_code;
}

// ----------------------------------------------------------------------
// Ponto de Entrada
// ----------------------------------------------------------------------

int recovery_main(RecoveryStartMode start_mode) {
    KERN_EMERG("=========================================");
    KERN_INFO("RECOVERY: ArcanOS Recovery Mode ativado.");
    
    const char* reason = "Normal";
    if (start_mode == RECOVERY_MODE_FORCED_MENU) reason = "Hardware Button";
    if (start_mode == RECOVERY_MODE_KERNEL_FAIL) reason = "Kernel Panic Auto-Start";
    if (start_mode == RECOVERY_MODE_WIPE_DATA) reason = "Wipe Data Request";

    KERN_INFO("RECOVERY: Modo de Início: %s (Code: %d).", reason, start_mode);
    KERN_EMERG("=========================================");

    // Etapa 1: Inicialização do Hardware
    recovery_init_hardware(); 

    // Etapa 2: Diagnóstico/Reparo Básico (Se falha de Kernel)
    if (start_mode == RECOVERY_MODE_KERNEL_FAIL) {
        KERN_WARNING("RECOVERY: Tentando reparo automático de FS...");
        // auto_repair_filesystem(); // Função hipotética de reparo
    }
    
    // Etapa 3: Interação com o Usuário
    int final_action = recovery_run_menu();
    
    // Etapa 4: Log e Retorno (Para o Kernel/Loader agir)
    KERN_INFO("RECOVERY: Finalizando e retornando Ação 0x%04X para o Loader.", final_action);

    return final_action; 
}
