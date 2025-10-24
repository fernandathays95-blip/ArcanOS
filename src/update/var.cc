// src/update/var.cc
// Definições e Inicializações de variáveis globais do Subsistema de Update.
// Este arquivo existe para garantir que todas as variaveis globais (declaradas com 'extern')
// em diversos headers (.h) sejam definidas em UM SO ARQUIVO de objeto.

#include "update_defs.h"

// Note: Se houvesse mais headers de update (ex: update_security.h, update_ui.h),
// eles seriam incluidos AQUI também.
// #include "update_security.h"
// #include "update_ui.h" 


// ======================================================================
// DEFINIÇÕES E INICIALIZAÇÕES DAS VARIÁVEIS GLOBAIS
// ======================================================================

// Variáveis de Estado (Definindo as 'extern' de update_defs.h)
int g_update_state = 0; // 0 = IDLE

// A versão para a qual estamos atualizando (inicialmente vazia)
char g_target_os_version[32] = {0};

// O status da partição de backup
bool g_is_ab_partition_valid = false;

// Contador de tentativas de aplicação da atualização
uint8_t g_update_try_count = 0;

// Instância da estrutura de progresso (Inicializada)
UpdateProgress g_current_progress = {
    .total_size = 0,
    .bytes_downloaded = 0,
    .is_verified = false
};

// ======================================================================
// (Outras definições de variaveis globais iriam aqui)
// ======================================================================
