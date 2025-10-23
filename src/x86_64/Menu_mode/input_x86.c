// src/x86_64/Menu_mode/input_x86.c
// Responsável por detectar a ativação do Menu Mode em plataformas x86_64.

#include <stdint.h>
#include <time.h>
#include <unistd.h> // Para simulação de sleep/delay

#define REQUIRED_HOLD_TIME_S 15

// Simulação de entrada de botão:
// Em PCs/Servidores, isso pode ser mapeado para:
// 1. ACPI: Detecção de evento de 'Power Button' (via BIOS/Firmware)
// 2. Teclado: Combinação de teclas (Ex: Ctrl+Alt+Del por 15s, ou tecla F12)

/**
 * @brief Simula a leitura de um evento de hardware no x86_64.
 * * @param event_id O identificador do evento a ser lido (ex: ACPI Power Button).
 * @return Estado do evento (1 se ativo/pressionado, 0 caso contrário).
 */
int read_hardware_event(uint32_t event_id) {
    // *******************************************************
    // LÓGICA DE HARDWARE AQUI:
    // O código real faria I/O de baixo nível para portas ACPI ou 
    // leria o buffer de teclado diretamente (PS/2 ou USB).
    // *******************************************************
    
    // Por simplicidade, assumimos que 0x01 é o Power Button e 0x02 é o Vol- (simulado por F12)
    if (event_id == 0x01 || event_id == 0x02) {
        // Retorna um valor simulado. Na prática, leria um registrador.
        return 0; 
    }
    return 0;
}


/**
 * @brief Verifica se a entrada de Menu Mode foi ativada na arquitetura x86_64
 * * (simulando a combinação POWER + VOL_DOWN por 15s).
 * @return 1 se o Menu Mode deve ser ativado, 0 caso contrário.
 */
int check_menu_mode_activation_x86() {
    uint64_t start_time = 0;
    int power_event_active = 0;
    int vol_down_event_active = 0;
    
    // Simula a detecção da combinação inicial (por exemplo: F12 + Power Button ACPI)
    power_event_active = read_hardware_event(0x01); 
    vol_down_event_active = read_hardware_event(0x02); 

    if (power_event_active && vol_down_event_active) {
        start_time = time(NULL); // Obtém o tempo inicial
        
        while ( (time(NULL) - start_time) < REQUIRED_HOLD_TIME_S ) {
            // Verifica novamente (pode usar 'usleep' ou um timer de CPU)
            power_event_active = read_hardware_event(0x01);
            vol_down_event_active = read_hardware_event(0x02);
            
            if (!power_event_active || !vol_down_event_active) {
                return 0; // Combinação interrompida
            }
            // usleep(100000); // Aguarda um pouco para não consumir CPU
        }
        
        // 15 segundos alcançados
        return 1;
    }
    
    return 0;
}
