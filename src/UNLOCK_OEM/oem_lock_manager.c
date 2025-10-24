// src/UNLOCK_OEM/oem_lock_manager.c
// Lógica de gerenciamento do Desbloqueio/Bloqueio do Bootloader (OEM Unlocking).

#include "oem_lock_manager.h"
#include <string.h> // Para strcmp
#include "../sys/kernel/kernl/printk/printk.h" // Para logging

// Chave de desbloqueio HARDCODED para fins de desenvolvimento
// NO MUNDO REAL: Esta chave seria baseada em uma hash única do dispositivo.
#define OEM_DEV_UNLOCK_KEY "ARC_OEM_UNLOCK_2025"

// Estado simulado do flag (em um sistema real estaria na NVRAM ou eMMC)
static OemLockState current_lock_state = OEM_STATE_LOCKED;


OemLockState oem_lock_get_state(void) {
    // No mundo real: Leria a flag persistente
    // return nvram_read_oem_flag(); 

    KERN_INFO("OEM_LOCK: Estado atual lido: %d", current_lock_state);
    return current_lock_state;
}

OemErrorCode oem_lock_attempt_unlock(const char* unlock_key) {
    KERN_WARNING("OEM_LOCK: Tentativa de desbloqueio iniciada.");

    if (current_lock_state != OEM_STATE_LOCKED) {
        KERN_WARNING("OEM_LOCK: Dispositivo ja esta UNLOCKED.");
        return OEM_SUCCESS;
    }

    if (strcmp(unlock_key, OEM_DEV_UNLOCK_KEY) != 0) {
        KERN_ERROR("OEM_LOCK: Chave de desbloqueio incorreta fornecida.");
        return OEM_ERR_WRONG_KEY;
    }

    // A operação de desbloqueio é irreversível ou cara em termos de segurança
    current_lock_state = OEM_STATE_UNLOCKED;

    // No mundo real: Envia comando para o bootloader/firmware atualizar a flag NVRAM
    // if (!nvram_write_oem_flag(OEM_STATE_UNLOCKED)) return OEM_ERR_WRITE_FAIL;

    KERN_EMERG("OEM_LOCK: SUCESSO! Bootloader AGORA ESTA DESBLOQUEADO. A garantia pode estar ANULADA.");
    return OEM_SUCCESS;
}

OemErrorCode oem_lock_attempt_lock(const char* relock_key) {
    KERN_INFO("OEM_LOCK: Tentativa de bloqueio iniciada.");

    // Simulação: Requer que a chave seja "LOCK_IT"
    if (strcmp(relock_key, "LOCK_IT") != 0) {
        KERN_ERROR("OEM_LOCK: Chave de bloqueio incorreta.");
        return OEM_ERR_WRONG_KEY;
    }

    if (current_lock_state == OEM_STATE_LOCKED) {
        KERN_INFO("OEM_LOCK: Ja estava bloqueado.");
        return OEM_SUCCESS;
    }

    // A operação de bloqueio é tipicamente menos crítica, mas precisa de validação
    current_lock_state = OEM_STATE_LOCKED;

    // No mundo real: Envia comando para o bootloader/firmware atualizar a flag NVRAM
    // if (!nvram_write_oem_flag(OEM_STATE_LOCKED)) return OEM_ERR_WRITE_FAIL;
    
    KERN_INFO("OEM_LOCK: SUCESSO! Bootloader BLOQUEADO. Apos o reboot, o estado de seguranca sera restaurado.");
    return OEM_SUCCESS;
}
