#ifndef ARCANOS_TTS_VOX_H
#define ARCANOS_TTS_VOX_H

#include <string>
#include <stdbool.h>

/**
 * @brief Inicializa o subsistema Text-to-Speech (TTS).
 * @return true se a inicialização for bem-sucedida, false caso contrário.
 */
extern "C" bool tts_init_module(void);

/**
 * @brief Converte texto em fala e a reproduz imediatamente no canal de emergência.
 * * Esta função é síncrona: ela só retorna após a fala terminar.
 * @param text O texto a ser falado.
 */
extern "C" void tts_say_sync(const std::string& text);

/**
 * @brief Obtém o nome da voz de emergência (para log/diagnóstico).
 * @return O nome da voz utilizada.
 */
extern "C" const char* tts_get_voice_name(void);

#endif // ARCANOS_TTS_VOX_H
