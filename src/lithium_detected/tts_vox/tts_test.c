// src/lithium_detected/tts_vox/tts_test.c
// Módulo de teste para validar a funcionalidade do TTS de emergência.

#include "tts.h"
#include <stdio.h> // Usado aqui para simular a saída de console do teste.
#include <string.h> // Para manipulação de strings (embora não essencial para este teste)

/**
 * @brief Função principal para executar o teste do módulo TTS.
 * * Chama a inicialização e o tts_say_sync() com a palavra "teste".
 * @return 0 se o teste for bem-sucedido, 1 caso contrário.
 */
int tts_run_selftest(void) {
    printf("--- ARCANOS TTS VOX: SELF-TEST ---\n");
    
    // 1. Inicializa o módulo
    if (!tts_init_module()) {
        printf("TEST FAILED: Falha ao inicializar o modulo TTS.\n");
        return 1;
    }
    
    // 2. Executa a frase de teste
    const char* test_phrase = "teste";
    printf("TEST INFO: Falando a frase de teste: \"%s\"...\n", test_phrase);
    
    // Converte a frase de teste para fala síncrona
    // Nota: Em C, precisamos envolver o char* em std::string para a interface C++
    tts_say_sync(test_phrase); 
    
    // 3. Verifica e Log de Sucesso
    printf("TEST INFO: Voz utilizada: %s\n", tts_get_voice_name());
    printf("--- ARCANOS TTS VOX: TESTE BEM-SUCEDIDO ---\n");
    
    return 0;
}

// Opcional: Função main simulada para execução direta do teste
/*
int main() {
    return tts_run_selftest();
}
*/
