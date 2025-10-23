# ArcanOS Bootloader Library (libbootloader)

Este diretório contém o código-fonte de baixo nível para a biblioteca do ArcanOS Bootloader.

O objetivo principal desta biblioteca é fornecer funcionalidades altamente otimizadas e mínimas para:

1.  **Inicialização de Hardware:** Configuração inicial de registradores, interrupções e CPU (específico para x86_64 e ARM64).
2.  **Gerenciamento de Memória:** Configuração inicial da paginação e tradução de endereços.
3.  **Carregamento do Kernel:** Lógica para carregar o binário do Kernel do disco/RAM para a memória correta (usando possivelmente o código de 'ziparchive' para localizar o Kernel).
4.  **Integração:** Funções utilitárias que serão usadas pelo código principal do bootloader (que ficaria em `src/bootloader/main/` ou similar).

**Arquivos Previstos:**
* `memory.c` / `memory.h` (Funções de memória)
* `hardware_init_arm64.c` (Inicialização específica ARM64)
* `hardware_init_x86_64.c` (Inicialização específica x86_64)
