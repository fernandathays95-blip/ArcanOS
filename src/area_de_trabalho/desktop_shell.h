#ifndef ARCANOS_DESKTOP_SHELL_H
#define ARCANOS_DESKTOP_SHELL_H

#include <string>
#include <vector>
#include <map>

// Definição do Gerenciador de Janelas (Compositor - ex: Wayland/Mutter)
class WindowManager {
public:
    virtual void start_compositor() = 0;
    virtual void handle_window_event(const std::string& event) = 0;
    // ...
};

// Classe Principal: O Shell do ArcanOS (Desktop Environment)
class ArcanosDesktopShell {
public:
    // Construtor: Recebe o compositor a ser usado (Mutter, KWin, etc.)
    ArcanosDesktopShell(WindowManager* wm);
    
    // Inicializa a sessão (carrega perfis, papel de parede, ícones, etc.)
    bool initialize_session();
    
    // Loop principal da Área de Trabalho (trata eventos de UI e sistema)
    int run_event_loop();
    
    // Funções de Gerenciamento de Aplicações
    bool launch_application(const std::string& app_id);
    
private:
    WindowManager* window_manager;
    std::map<std::string, bool> running_applications;
    
    // Desenha o fundo e os painéis (GNOME Shell/Phosh)
    void draw_background() const;
    void setup_system_tray(); // Barra de notificações, bateria, etc.
};

#endif // ARCANOS_DESKTOP_SHELL_H
