// src/update/update_defs.h
// Declarações externas de variáveis e estruturas globais do Subsistema de Update.

#ifndef ARCANOS_UPDATE_DEFS_H
#define ARCANOS_UPDATE_DEFS_H

#include <stdint.h>
#include <stdbool.h>
#include <stddef.h>

// ----------------------------------------------------------------------
// Variáveis Globais de Estado (Declaradas, a serem definidas em var.cc)
// ----------------------------------------------------------------------

// O estado atual do processo de atualização (ex: IDLE, DOWNLOAD, APPLY, REBOOT)
extern int g_update_state; 

// A versão do OS para a qual estamos atualizando
extern char g_target_os_version[32];

// O status da partição de backup (A/B)
extern bool g_is_ab_partition_valid;

// Contador de tentativas de aplicação da atualização (para rollback)
extern uint8_t g_update_try_count;


// ----------------------------------------------------------------------
// Estruturas e Constantes
// ----------------------------------------------------------------------

// Estrutura para rastrear o progresso do download
typedef struct {
    uint64_t total_size;
    uint64_t bytes_downloaded;
    bool is_verified;
} UpdateProgress;

// Declaração da instância da estrutura de progresso
extern UpdateProgress g_current_progress;


// ----------------------------------------------------------------------
// Interfaces de Módulos (Simuladas)
// ----------------------------------------------------------------------

// Ações de baixo nível (declaradas, mas implementadas em outros arquivos)
extern bool update_check_network(void);
extern int update_apply_package(const char* package_path);

#endif // ARCANOS_UPDATE_DEFS_H
