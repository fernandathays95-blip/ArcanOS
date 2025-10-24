#ifndef ARCANOS_IS_RESPONDING_TEST_H
#define ARCANOS_IS_RESPONDING_TEST_H

#include <stdbool.h>
#include <stdint.h>

// Tipo de função de teste de responsividade
// Esta função é chamada pelo Kernel Monitor e deve retornar TRUE se o subsistema
// estiver ativo e responsivo, ou FALSE se estiver travado.
typedef bool (*ResponsivenessTestFn)(void);

// Estrutura para rastrear um teste registrado
typedef struct {
    const char *name;                  // Nome do subsistema (ex: "Scheduler", "Filesystem")
    uint32_t id;                       // ID único do teste
    ResponsivenessTestFn test_function; // A função de teste
    uint64_t max_latency_ms;           // Latência máxima aceitável para o teste
} ComponentResponseTest;

// =======================================================
// Funções de Registro e Execução
// =======================================================

/**
 * @brief Registra um novo teste de responsividade para um componente.
 * @param test A estrutura de teste a ser registrada.
 */
void irt_register_test(ComponentResponseTest *test);

/**
 * @brief Executa todos os testes registrados e retorna o status.
 * @return TRUE se todos os componentes responderam, FALSE se algum falhou.
 */
bool irt_check_all_components();

#endif // ARCANOS_IS_RESPONDING_TEST_H
