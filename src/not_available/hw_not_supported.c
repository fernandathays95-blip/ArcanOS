// src/not_available/hw_not_supported.c
// Protocolo de erro de hardware não suportado (Fatal Error).

#include "hw_not_supported.h"
#include <stdio.h> // Para printf (simulação de log)

// As funções emergency_display_set_text e emergency_speech_say são simuladas aqui.
// Em um sistema real, elas seriam implementadas em um módulo de arquitetura de baixo nível.
void emergency_display_set_text(const char* header, const char* main_text, int color_code) {
    printf("\n[EMERGENCY DISPLAY - CODE 0x%X]\n", color_code);
    printf("========================================\n");
    printf("HEADER: %s\n", header);
    printf("MESSAGE: %s\n", main_text);
    printf("========================================\n");
}

void emergency_speech_say(const char* text) {
    printf("[EMERGENCY VOICE]: %s\n", text);
}

void system_force_shutdown_immediate(void) {
    printf("\n[SYSTEM] DESLIGAMENTO FORCADO IMEDIATO.\n");
    // Em um sistema real, esta seria a chamada para a ACPI ou PMIC para desligar
    while(1) { /* Halt CPU */ }
}


void run_hw_not_supported_protocol(const char* component_name, const char* reason) {
    const char* header = "ARCANOS FATAL ERROR";
    char main_msg[256];
    char voice_msg[256];

    // Log e Preparação da Mensagem de Voz
    printf("FATAL ERROR: Incompatibilidade de Hardware detectada! Componente: %s\n", component_name);
    
    // 1. Sequência de Voz
    // O texto deve ser todo em maiúsculas na string para sugerir o "grito" (caps lock)
    snprintf(voice_msg, sizeof(voice_msg), 
             "ATENCAO. O SISTEMA IDENTIFICOU UM ERRO FATAL DE HARDWARE. O COMPONENTE %s NAO E DISPONIVEL COM O ARCANOS.", 
             component_name);
    
    emergency_speech_say(voice_msg);
    
    // 2. Mensagem Visual
    snprintf(main_msg, sizeof(main_msg), 
             "HARDWARE NOT AVAILABLE! O COMPONENTE '%s' NAO E SUPORTADO PELO ARCANOS. REASON: %s. O DISPOSITIVO SERA DESLIGADO.", 
             component_name, reason);
    
    // A função de display usará o texto todo em maiúsculas (gritando) e a cor vermelha
    emergency_display_set_text(header, main_msg, HW_ERROR_COLOR); 

    // 3. Instrução e Desligamento
    emergency_speech_say("O DISPOSITIVO SERA DESLIGADO AGORA. POR FAVOR, CONSULTE O FABRICANTE.");
    
    // Espera um momento (simulação de tempo)
    // Usar a thread_sleep aqui requer C++, mas em C faríamos um loop busy-wait ou timer.
    for (volatile int i = 0; i < 50000000; i++); 

    system_force_shutdown_immediate();
}
