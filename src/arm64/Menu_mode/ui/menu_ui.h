#ifndef ARCANOS_MENU_UI_H
#define ARCANOS_MENU_UI_H

#include <vector>
#include <string>
#include <cstdint> // Para tipos fixos como uint32_t

// Estrutura para representar cada item do Menu Mode
struct MenuItem {
    // Para renderizar caracteres especiais (Unicode Icons: ⚙️, 🔄, 💾)
    std::u32string icon; 
    std::string text;
    uint32_t action_code; // Código para a ação a ser executada
};

// Classe principal da Interface do Menu Mode
class MenuUI {
public:
    MenuUI();
    
    // Método principal: Gerencia o loop de interação e executa a ação final.
    int run_menu_loop();

private:
    std::vector<MenuItem> menu_items;
    int selected_index;

    // Métodos de Lógica (definidos em menu_logic.cpp)
    void setup_default_items();
    int execute_selected_action();
    
    // Métodos de Renderização (definidos em menu_ui.cc)
    void draw_screen() const;
    void draw_unicode_text(int x, int y, const std::u32string& text) const;
    void handle_input(); // Lógica de leitura de botões
};

// Define os códigos de ação do Menu Mode (Globalmente acessíveis)
enum MenuAction : uint32_t {
    ACTION_REBOOT_NORMAL = 1,
    ACTION_WIPE_CACHE = 2,
    ACTION_FACTORY_RESET = 3,
    ACTION_DIAGNOSTICS = 4,
    ACTION_SHUTDOWN = 5,
    ACTION_EXIT_LOOP = 99 // Código de saída para run_menu_loop
};

#endif // ARCANOS_MENU_UI_H
