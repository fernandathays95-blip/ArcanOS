// src/arm64/Menu_mode/ui/menu_actions.cxx
// Arquivo responsável por executar as AÇÕES CRÍTICAS de Recovery.

#include "menu_ui.h"
#include <iostream>
#include <cstdlib> // Para chamadas de sistema (reboot, wipe)

/**
 * @brief Executa a ação do menu selecionada.
 * * @param action O código de ação (enum MenuAction).
 * @return 0 em sucesso, -1 em falha.
 */
int perform_action(uint32_t action) {
    switch (action) {
        case ACTION_REBOOT_NORMAL:
            std::cout << "[AÇÃO CRÍTICA] Reiniciando o sistema (chamando sys_reboot())...\n";
            // reboot(RB_AUTOBOOT); // Chamada de sistema real
            return 0;
            
        case ACTION_WIPE_CACHE:
            std::cout << "[AÇÃO CRÍTICA] Limpando a partição de cache...\n";
            // system("rm -rf /cache/*"); // Chamada de sistema real
            return 0;
            
        case ACTION_FACTORY_RESET:
            std::cout << "[AÇÃO CRÍTICA] FORMATANDO DADOS! (Atenção: Destrutivo)\n";
            // system("format_data_partition"); // Chamada de sistema real
            return 0;
            
        case ACTION_DIAGNOSTICS:
            std::cout << "[AÇÃO CRÍTICA] Executando testes de hardware da DPU/GPU...\n";
            // run_hardware_diagnostics(); // Chamada de sistema real
            return 0;
            
        case ACTION_SHUTDOWN:
            std::cout << "[AÇÃO CRÍTICA] Desligando o dispositivo...\n";
            // power_off(); // Chamada de sistema real
            return 0;

        default:
            std::cerr << "[ERRO] Ação não reconhecida: " << action << std::endl;
            return -1;
    }
}
