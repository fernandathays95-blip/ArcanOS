// src/area_de_trabalho/desktop_shell.cpp
// Implementação básica do Arcanos Desktop Shell.

#include "desktop_shell.h"
#include <iostream>

ArcanosDesktopShell::ArcanosDesktopShell(WindowManager* wm)
    : window_manager(wm) {
    std::cout << "Arcanos Desktop Shell inicializado." << std::endl;
}

bool ArcanosDesktopShell::initialize_session() {
    std::cout << "Inicializando sessão de usuário. Carregando configurações de perfil..." << std::endl;
    
    // 1. Inicializa o Compositor
    window_manager->start_compositor();
    
    // 2. Carrega o papel de parede
    draw_background();
    
    // 3. Configura a barra de sistema
    setup_system_tray();
    
    // 4. Inicia o primeiro aplicativo (ex: tela de bloqueio ou launcher)
    launch_application("arcanos-launcher");
    
    return true;
}

int ArcanosDesktopShell::run_event_loop() {
    std::cout << "Iniciando loop de eventos do Desktop Shell..." << std::endl;
    std::string current_event = "initial_draw";
    
    while (true) {
        // Simulação de loop de eventos (recebe eventos do Wayland/X11)
        // current_event = read_input_event(); 
        
        window_manager->handle_window_event(current_event);
        
        if (current_event == "shutdown_request") {
            std::cout << "Desktop Shell encerrando..." << std::endl;
            break;
        }
        
        // Simulação de eventos de sistema
        // if (check_battery_low()) setup_system_tray();
    }
    return 0;
}

void ArcanosDesktopShell::draw_background() const {
    std::cout << "Desenhando o fundo de tela/Workspace (Cor de fundo: Azul Arcanos)..." << std::endl;
    // O código real faria chamadas GTK/Qt/Cairo para desenhar o fundo.
}

void ArcanosDesktopShell::setup_system_tray() {
    std::cout << "Configurando barra de status (Relógio, Bateria, Conectividade)." << std::endl;
}

bool ArcanosDesktopShell::launch_application(const std::string& app_id) {
    std::cout << "Lançando aplicação: " << app_id << std::endl;
    running_applications[app_id] = true;
    return true;
}
