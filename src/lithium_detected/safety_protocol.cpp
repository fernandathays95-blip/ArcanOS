// src/lithium_detected/safety_protocol.cpp
// Lógica e temporização do Protocolo de Segurança de Emergência.

#include "safety.h" // Inclui as funções de UI/Áudio
#include <iostream>
#include <string>
#include <chrono>
#include <thread>
#include <cstdlib>

// Interfaces de controle do sistema (simuladas)
extern "C" void system_disable_all_features();
extern "C" void system_force_shutdown(int delay_seconds);


/**
 * @brief Implementa o protocolo de segurança para falha de bateria de lítio.
 * * Assume controle total do dispositivo, priorizando a segurança do usuário.
 * @param session_name O nome da sessão ou aplicativo que disparou o erro.
 */
extern "C" void run_lithium_safety_protocol(const std::string& session_name) {
    
    const int SHUTDOWN_DELAY = 10;
    
    // ===================================================================
    // 1. Alerta Visual Imediato
    // ===================================================================
    safety_ui_display_alert("LITHIUM DETECTED", 
                            "EMERGENCY PROTOCOL ACTIVATED", 
                            SAFETY_COLOR_RED); 
    
    std::cerr << "!!! CRITICAL SAFETY ALERT: LITHIUM THERMAL RUNAWAY DETECTED !!!" << std::endl;


    // ===================================================================
    // 2. Sequência de Voz e Desativação do Sistema
    // ===================================================================
    
    // Primeira Parte: Sessão Fritada e Instruções de Descarte
    safety_ui_speech_say("Alerta de Segurança Critico. A sessao " + session_name + " causou uma falha termica na bateria.");
    std::this_thread::sleep_for(std::chrono::seconds(1));
    safety_ui_speech_say("Por favor, jogue o aparelho fora imediatamente em segurança e longe de materiais inflamaveis.");
    std::this_thread::sleep_for(std::chrono::seconds(2));
    
    // Segunda Parte: Erro Clínico e Desativação
    safety_ui_speech_say("Este dispositivo sofreu um erro clinico de bateria interna.");
    std::this_thread::sleep_for(std::chrono::seconds(1));
    safety_ui_speech_say("O Arcanos ira desativar todos os recursos do sistema, incluindo rede e comunicacao, para prevenir danos.");
    
    // Terceira Parte: Desativação do Sistema
    system_disable_all_features();


    // ===================================================================
    // 3. Alerta Final e Contagem Regressiva para Desligamento
    // ===================================================================
    
    safety_ui_display_shutdown(SHUTDOWN_DELAY);
    
    safety_ui_speech_say("Shutdown now! Desligue o aparelho imediatamente.");
    
    // Inicia a contagem regressiva para desligamento forçado
    system_force_shutdown(SHUTDOWN_DELAY); 
    
    // Opcional: Loop de emergência
    while(true) {
        std::this_thread::sleep_for(std::chrono::seconds(SHUTDOWN_DELAY));
    }
}
