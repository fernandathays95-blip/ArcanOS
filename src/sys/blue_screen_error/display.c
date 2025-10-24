// src/sys/blue_screen_error/display.c
// Exibe a tela de erro fatal (Blue Screen) diretamente no hardware.

#include "error.h"
#include <stdio.h>

// Endereço de memória do framebuffer (varia por arquitetura, pode ser azul!)
#define FRAMEBUFFER_BASE_ADDR 0xEE000000 
#define DISPLAY_COLOR_FATAL   0x000000FF // Azul Brilhante (RGB)

// Funções de I/O de baixo nível simuladas (framework minimalista)
extern void arch_draw_pixel(uint32_t x, uint32_t y, uint32_t color);
extern void arch_draw_char(uint32_t x, uint32_t y, char c, uint32_t color);

void fatal_error_handler(ArcanosErrorCode code, uint64_t detail_address) {
    // 1. Assumir o controle do hardware gráfico (ignorar o OS atual)
    // O código real faria I/O de baixo nível para configurar o display.
    
    // 2. Limpar a tela com a cor de erro (Azul)
    // for (y = 0; y < HEIGHT; y++) { ... }
    
    // 3. Exibir a mensagem de erro no console ou framebuffer
    
    // Buffer para formatar a string de erro (tamanho limitado)
    char error_msg[128]; 
    char detail_msg[64];
    
    // Imprime a mensagem de erro principal (usando um printf de baixo nível)
    sprintf(error_msg, "FATAL ERROR: ArcanOS has stopped.");
    sprintf(detail_msg, "Code: 0x%04X @ 0x%016llX", code, detail_address);

    // Renderiza o cabeçalho
    arch_draw_string(10, 10, error_msg, 0xFFFFFFFF); // Branco no Azul

    // Renderiza o código de erro
    arch_draw_string(10, 30, detail_msg, 0xFFFFFFFF); 
    
    // Mensagem de instrução (ex: "Pressione POWER para entrar no Menu Mode")
    arch_draw_string(10, 50, "Please hold POWER button to enter Recovery Mode.", 0xFFFFFF00); // Amarelo
    
    // 4. Parar a CPU (Evitar mais corrupção)
    while(1) {
        __asm__ volatile ("hlt"); // Instrução de parada de CPU
    }
}

// Funções externas (apenas para o compilador):
void arch_draw_pixel(uint32_t x, uint32_t y, uint32_t color) {}
void arch_draw_char(uint32_t x, uint32_t y, char c, uint32_t color) {}
void arch_draw_string(uint32_t x, uint32_t y, const char *str, uint32_t color) {} // Adicionado
