#ifndef ARCANOS_HW_NOT_SUPPORTED_H
#define ARCANOS_HW_NOT_SUPPORTED_H

#include <string.h>
#include <stdbool.h>

// Assumindo interfaces de emergência (como as definidas no LITHIUM_DETECTED)
// Um subsistema de baixo nível para UI de Emergência
extern void emergency_display_set_text(const char* header, const char* main_text, int color_code);
// O TTS pode ser chamado separadamente, mas para simplificar, usaremos uma única função.
extern void emergency_speech_say(const char* text); 
extern void system_force_shutdown_immediate(void); // Desligamento imediato

// Código de cor para Erro Crítico (Vermelho)
#define HW_ERROR_COLOR 0xFF0000 


/**
 * @brief Inicia o protocolo de falha de hardware não suportado.
 * * Toma controle total da tela e comunica o erro visual e vocalmente.
 * @param component_name O nome do componente que falhou (ex: "GPU", "NVMe Controller").
 * @param reason Detalhe da falha (ex: "ID nao reconhecido", "Versao incompativel").
 */
void run_hw_not_supported_protocol(const char* component_name, const char* reason);

#endif // ARCANOS_HW_NOT_SUPPORTED_H
