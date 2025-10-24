# src/config/config.bzl
# Definicoes de Starlark para configuracoes globais do ArcanOS.
# Estas constantes sao usadas em arquivos BUILD para injetar valores no codigo C/C++.

# ======================================================================
# CONSTANTES DE IDENTIFICACAO DO SISTEMA
# ======================================================================

# @brief Booleano indicando se esta e uma versao de custom ROM (para fins de garantia/seguranca).
# Se FALSE, e considerada uma ROM de fabrica/oficial.
IS_CUSTOM_ROM = False

# @brief Nome do Sistema Operacional.
SYSTEM_NAME = "ArcanOS"

# @brief Versao de desenvolvimento ou de producao.
BUILD_VARIANT = "Development_Signed"

# ======================================================================
# MACRO DE EXPOSICAO
# ======================================================================

def arcanos_build_config():
    """
    Retorna um dicionario de definicoes de pre-processador (COPTS) para injecao
    no codigo fonte do ArcanOS.
    """
    
    # Flags de pre-processador para o compilador C/C++
    defines = []
    
    # Injetar o nome do sistema
    defines.append("-DARC_SYSTEM_NAME=\"%s\"" % SYSTEM_NAME)
    
    # Injetar a flag de custom ROM
    if IS_CUSTOM_ROM:
        defines.append("-DARC_IS_CUSTOM_ROM=1")
    else:
        defines.append("-DARC_IS_CUSTOM_ROM=0")
        
    # Injetar a variante de build
    defines.append("-DARC_BUILD_VARIANT=\"%s\"" % BUILD_VARIANT)
        
    return defines

# Exemplo de como usar estas definicoes em um arquivo BUILD
# Exemplo em BUILD:
# load("//src/config:config.bzl", "arcanos_build_config")
# cc_library(
#     name = "my_module",
#     srcs = ["my_module.c"],
#     copts = arcanos_build_config(),
# )
