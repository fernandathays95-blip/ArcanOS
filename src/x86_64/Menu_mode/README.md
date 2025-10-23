# ArcanOS Menu Mode (x86_64)

Esta pasta documenta e contém arquivos específicos para a ativação do Menu Mode em plataformas x86_64 (Servidores/Desktops).

**Requisito do Usuário:** O Menu Mode é ativado quando o dispositivo detecta a entrada simulada de "botão power + vol- por 15s".

**Implementação:**
* **Entrada de Botão:** A lógica de detecção de entrada (leitura de GPIOs simulados ou comandos ACPI/BIOS) é tratada em um arquivo à parte (`input_x86.c`).
* **Interface:** A interface (Menu Mode UI) utiliza o mesmo código-fonte da ARM64 (`src/arm64/Menu_mode/ui/`), garantindo a consistência visual com Unicode.
* **Ações:** As opções de recuperação (reset, diagnóstico) são tratadas por scripts de serviço do x86_64.
