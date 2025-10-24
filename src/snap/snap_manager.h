#ifndef ARCANOS_SNAP_MANAGER_H
#define ARCANOS_SNAP_MANAGER_H

#include <stdbool.h>
#include <stdint.h>
#include <stddef.h>

// Definições de códigos de erro
typedef enum {
    SNAP_SUCCESS          = 0,
    SNAP_ERR_NOT_FOUND    = 1,
    SNAP_ERR_INVALID_PKG  = 2,
    SNAP_ERR_INTEGRITY    = 3,
    SNAP_ERR_SANDBOX_FAIL = 4
} SnapErrorCode;

// Estrutura de Metadados de um Snap
typedef struct {
    char name[64];
    char version[16];
    uint32_t revision;
    bool is_confined; // Indica se está em sandbox
} SnapMetadata;


// ----------------------------------------------------------------------
// Funções Principais de Gerenciamento de Snaps
// ----------------------------------------------------------------------

/**
 * @brief Inicializa o subsistema de gerenciamento de snaps (Snapd-like).
 * @return SNAP_SUCCESS ou código de erro.
 */
SnapErrorCode snap_manager_init(void);

/**
 * @brief Instala um pacote snap a partir de um arquivo de imagem.
 * * Envolve verificação de integridade e configuração de sandbox.
 * @param path_to_snap O caminho para o arquivo .snap.
 * @param metadata Buffer para preencher com os metadados do snap instalado.
 * @return SNAP_SUCCESS ou código de erro.
 */
SnapErrorCode snap_manager_install(const char* path_to_snap, SnapMetadata* metadata);

/**
 * @brief Inicia uma aplicação contida (snap) em seu ambiente isolado (sandbox).
 * @param snap_name O nome do snap a ser executado.
 * @param app_name O nome do comando/aplicativo dentro do snap.
 * @return SNAP_SUCCESS se o snap for executado com sucesso, ou código de erro.
 */
SnapErrorCode snap_manager_run(const char* snap_name, const char* app_name);

/**
 * @brief Lista todos os snaps instalados no sistema.
 * @param list_buffer Buffer para armazenar a lista de metadados.
 * @param max_entries O tamanho máximo do buffer.
 * @return O número de snaps instalados.
 */
size_t snap_manager_list_installed(SnapMetadata* list_buffer, size_t max_entries);

#endif // ARCANOS_SNAP_MANAGER_H
