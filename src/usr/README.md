# ArcanOS Userland Source Code (src/usr/)

Este diretório contém os scripts e arquivos de configuração para todos os componentes do Espaço de Usuário (Userland) que não são essenciais ao boot de baixo nível (sys/).

A compilação nesta pasta é orquestrada pelo sistema de build LFS (Linux From Scratch) para a arquitetura alvo.

## Estrutura e Componentes

A organização segue a separação por função e arquitetura:

1.  **common/:** Scripts para pacotes que são compilados e instalados em AMBAS as arquiteturas (ARM64 e x86_64) sem alterações significativas (ex: Glibc, Binutils, Coreutils).

2.  **mobile/:** Componentes exclusivos da interface de usuário móvel GNOME/Phosh (aplicativos, extensões, configurações específicas de tela sensível ao toque). *Nota: O GNOME Shell Mobile/Phosh já tem scripts em src/arm64/gnome_mobile/.*

3.  **server/:** Componentes e serviços de alto desempenho exclusivos do ambiente de servidor (otimizadores de rede, ferramentas de virtualização, scripts de monitoramento de HPC).

4.  **lib/:** Arquivos de configuração e patches para bibliotecas críticas de Userland (ex: Libadwaita, GTK4, Patches de desempenho de Glibc/Pthread).
