// src/lithium_detected/safety.cc
// Implementação da Interface de Usuário (Visual e Áudio) para o Protocolo de Segurança de Lítio.

#include "safety.h"
#include <iostream>
#include <string>

// Interfaces de baixo nível (simuladas)
extern "C" void graphics_draw_fullscreen_color(int color);
extern "C" void graphics_draw_text(int x, int y, const std::string& text, int color);
extern "C" void audio_play_synth(const std::string& text);


extern "C" void safety_ui_display_alert(const std::string& header_text, 
                                        const std::string& main_message, 
                                        int color_code) 
{
    // 1. Tomar controle total da tela e definir a cor de fundo (simulação)
    graphics_draw_fullscreen_color(color_code); 

    // 2. Desenhar o cabeçalho (LITHIUM DETECTED) no topo (vermelho)
    // O texto deve ser branco ou amarelo para contraste com o fundo vermelho
    graphics_draw_text(10, 50, header_text, 0xFFFFFF); // Canto superior

    // 3. Desenhar a mensagem principal no centro
    graphics_draw_text(500, 500, main_message, 0xFFFFFF);

    std::cerr << "[SAFETY UI] ALERTA VISUAL: " << header_text << std::endl;
}

extern "C" void safety_ui_speech_say(const std::string& text) {
    audio_play_synth(text);
    // Simulação:
    std::cout << "[SAFETY UI - VOICE] FALANDO: " << text << std::endl;
}

extern "C" void safety_ui_display_shutdown(int countdown_seconds) {
    std::string countdown_msg = "Shutdown now! (Automatic shutdown in " + std::to_string(countdown_seconds) + "s)";
    
    // Redesenha a tela com a mensagem final de desligamento
    graphics_draw_fullscreen_color(SAFETY_COLOR_RED); 
    graphics_draw_text(500, 500, countdown_msg, 0xFFFFFF);
    
    std::cerr << "[SAFETY UI] ALERTA FINAL: " << countdown_msg << std::endl;
}
