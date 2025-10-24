// src/arm64/Menu_mode/ui/menu_logic.cpp
// Implementa a lógica de navegação do Menu Mode.

#include "menu_ui.h"
#include <iostream>

MenuUI::MenuUI() : selected_index(0) {
    std::cout << "MenuUI: Inicializando subsistema ARM64 (LÓGICA)." << std::endl;
    setup_default_items();
}

void MenuUI::setup_default_items() {
    // Configura os itens do menu
    menu_items.push_back({U"🔄", "Reiniciar Sistema", ACTION_REBOOT_NORMAL});
    menu_items.push_back({U"🧹", "Limpar Cache (Wipe Cache)", ACTION_WIPE_CACHE});
    menu_items.push_back({U"🚨", "Reset de Fábrica", ACTION_FACTORY_RESET});
    menu_items.push_back({U"⚙️", "Diagnóstico de Hardware", ACTION_DIAGNOSTICS});
    menu_items.push_back({U"↩️", "Desligar", ACTION_SHUTDOWN});
}

int MenuUI::execute_selected_action() {
    uint32_t action = menu_items[selected_index].action_code;
    
    std::cout << "[LÓGICA] Executando Ação: " << menu_items[selected_index].text << " (Code: " << action << ")" << std::endl;

    // Chamada para a função real que executa a ação (definida em menu_actions.cxx)
    // int result = perform_action(action); 
    
    if (action == ACTION_REBOOT_NORMAL || action == ACTION_SHUTDOWN || action == ACTION_FACTORY_RESET) {
        // Estas ações encerram o Menu Loop
        return ACTION_EXIT_LOOP; 
    }
    
    // Outras ações (como diagnóstico) retornam ao menu
    return 0;
}


int MenuUI::run_menu_loop() {
    int exit_code = 0;
    
    while (exit_code == 0) {
        // 1. Renderizar a tela (UI.CC)
        draw_screen();
        
        // 2. Esperar e gerenciar o input (UI.CC)
        handle_input();
        
        // Simulação de detecção de seleção de item:
        // if (input_power_button_pressed()) {
        //     exit_code = execute_selected_action();
        // }
    }
    
    return 0;
}
