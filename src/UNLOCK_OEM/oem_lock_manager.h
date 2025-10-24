#ifndef ARCANOS_OEM_LOCK_MANAGER_H
#define ARCANOS_OEM_LOCK_MANAGER_H

#include <stdbool.h>
#include <stdint.h>
#include <stddef.h>

// Definições de estados do bloqueio OEM
typedef enum {
    OEM_STATE_LOCKED = 0,             // Dispositivo seguro (padrão)
    OEM_STATE_UNLOCKED = 1,           // Bootloader desbloqueado (Garantia Anulada)
    OEM_STATE_LOCKED_USER_ENFORCED = 2 // Bloqueado, mas com flags persistentes do usuário
} OemLockState;

// Definições de códigos de erro
typedef enum {
    OEM_SUCCESS         = 0,
    OEM_ERR_WRONG_KEY   = 1,
    OEM_ERR_WRITE_FAIL  = 2,
    OEM_ERR_NOT_ALLOWED = 3  // Ex: Tentativa de desbloqueio em um dispositivo de produção final
} OemErrorCode;

// ----------------------------------------------------------------------
// Funções Principais de Gerenciamento
// ----------------------------------------------------------------------

/**
 * @brief Obtém o estado atual do bootloader (Bloqueado/Desbloqueado).
 * * Lê diretamente de uma área segura de armazenamento persistente (eMMC/NVRAM).
 * @return O estado atual do OemLockState.
 */
OemLockState oem_lock_get_state(void);

/**
 * @brief Tenta desbloquear o bootloader.
 * * Requer uma chave de segurança específica (geralmente uma hash ou código de acesso).
 * * O estado deve ser escrito em NVRAM de forma irreversível ou de alto custo.
 * @param unlock_key A chave de desbloqueio fornecida.
 * @return OEM_SUCCESS se desbloqueado, ou código de erro.
 */
OemErrorCode oem_lock_attempt_unlock(const char* unlock_key);

/**
 * @brief Tenta bloquear o bootloader novamente.
 * * Nem sempre é possível se o estado UNLOCKED for permanente.
 * @param relock_key Chave/senha do usuário para confirmar o bloqueio.
 * @return OEM_SUCCESS se bloqueado, ou código de erro.
 */
OemErrorCode oem_lock_attempt_lock(const char* relock_key);

#endif // ARCANOS_OEM_LOCK_MANAGER_H
