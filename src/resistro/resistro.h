// src/resistro/resistro.h
// Declaracoes externas de variaveis de registro do sistema.

#ifndef ARCANOS_RESISTRO_H
#define ARCANOS_RESISTRO_H

// As strings sao declaradas como 'extern const' para que o compilador saiba que
// serao definidas em resistro.c.

/**
 * @brief Nome e versao do Sistema Operacional.
 */
extern const char* const OS_NAME_VERSION;

/**
 * @brief Nome de usuario padrao ou atual.
 */
extern const char* const DEFAULT_USER;

/**
 * @brief Nome do fabricante do hardware/software.
 */
extern const char* const VENDOR_NAME;

#endif // ARCANOS_RESISTRO_H
