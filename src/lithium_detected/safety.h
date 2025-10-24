#ifndef ARCANOS_LITHIUM_SAFETY_UI_H
#define ARCANOS_LITHIUM_SAFETY_UI_H

#include <string>

// Definição da cor de emergência (Vermelho)
#define SAFETY_COLOR_RED 0xFF0000 

/**
 * @brief Exibe o alerta visual imediato "LITHIUM DETECTED" na tela.
 * @param header_text O texto principal (LITHIUM DETECTED).
 * @param main_message A mensagem secundária (EMERGENCY PROTOCOL ACTIVATED).
 * @param color_code O código de cor do alerta (geralmente SAFETY_COLOR_RED).
 */
extern "C" void safety_ui_display_alert(const std::string& header_text, 
                                        const std::string& main_message, 
                                        int color_code);

/**
 * @brief Sintetiza e reproduz a fala de segurança.
 * @param text O texto a ser lido em voz alta.
 */
extern "C" void safety_ui_speech_say(const std::string& text);

/**
 * @brief Exibe a tela de desligamento final "Shutdown now!".
 * @param countdown_seconds Tempo restante para o desligamento forçado.
 */
extern "C" void safety_ui_display_shutdown(int countdown_seconds);

#endif // ARCANOS_LITHIUM_SAFETY_UI_H
