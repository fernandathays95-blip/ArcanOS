// src/arm64/Menu_mode/ui/menu_ui.cc
// Implementa a renderização de baixo nível (Framebuffer e Input de Hardware).

#include "menu_ui.h"
#include <iostream>

// Funções de I/O de baixo nível simuladas
int read_vol_up() { return 0; }
int read_vol_down() { return 0; }
int read_power_button() { return 0; }

void MenuUI::draw_unicode_text(int x, int y, const std::u32string& text) const {
    // ------------------------------------------------------------------
    // A VERDADEIRA RENDERIZAÇÃO GRÁFICA UNICODE (Usando Framebuffer/FreeType)
    // ------------------------------------------------------------------
    
    // Simulação no console:
    std::cout << "[RENDER] @ (" << x << "," << y << ") | ";
    
    // Simplesmente imprime o texto para fins de demonstração
    for (char32_t c : text) {
        if (c < 128) std::cout << (char)c;
        else std::cout << "?"; 
    }
}

void MenuUI::draw_screen() const {
    std::cout << "\n=============================================\n";
    std::cout << "        ArcanOS Recovery Mode (ARM64)        \n";
    std::cout << "=============================================\n";
    
    for (size_t i = 0; i < menu_items.size(); ++i) {
        bool is_selected = (i == selected_index);
        
        std::cout << (is_selected ? " ▶ " : "   ");
        draw_unicode_text(10, (int)i + 5, menu_items[i].icon);
        std::cout << " " << menu_items[i].text << "\n";
    }
}

void MenuUI::handle_input() {
    // Lógica de leitura de botões para navegação
    if (read_vol_up()) {
        selected_index = (selected_index - 1 + menu_items.size()) % menu_items.size();
    }
    if (read_vol_down()) {
        selected_index = (selected_index + 1) % menu_items.size();
    }
    
    // Ação de seleção é tratada no run_menu_loop quando o botão Power é pressionado
}
