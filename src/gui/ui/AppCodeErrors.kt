// src/gui/util/AppCodeErrors.kt
package com.arcanos.launcher.util

import arcanos.util.Log
import arcanos.util.Singleton
import com.arcanos.launcher.util.Alert // Importa o utilitario de alerta

/**
 * @brief Singleton centralizado para manipulacao e relatorio de erros do aplicativo.
 * Ele intercepta as excecoes e decide a acao apropriada: log, relatorio remoto, 
 * e notificacao ao usuario via Alert.
 */
@Singleton
object AppCodeErrors {
    private const val TAG = "AppCodeErrors"
    
    /**
     * @brief Processa um erro capturado no codigo.
     * @param throwable A excecao (erro) que ocorreu.
     * @param friendlyMessage Uma mensagem amigavel para mostrar ao usuario.
     * @param isFatal Se o erro e considerado critico para a operacao.
     */
    fun handleError(
        throwable: Throwable, 
        friendlyMessage: String = "Ocorreu um erro inesperado. Tente novamente.",
        isFatal: Boolean = false
    ) {
        // 1. Loga o erro completo para o console (necessario para debugging)
        Log.e(TAG, "ERRO CAPTURADO: $friendlyMessage. Detalhes: ${throwable.stackTraceToString()}")

        // 2. Decide a acao na UI
        if (isFatal) {
            // Se for fatal, mostra um alerta para o usuario parar a operacao
            Alert.showAlertDialog(
                title = "Erro Crítico do Sistema",
                message = "$friendlyMessage\n\nDetalhes tecnicos: ${throwable.message}"
            )
        } else {
            // Se for menos critico, mostra um toast discreto
            Alert.showToast(friendlyMessage)
        }

        // 3. *Simulacao de Relatorio Remoto*
        // Em um app real, voce enviaria isso para Sentry, Crashlytics, etc.
        reportRemote(throwable, friendlyMessage)
    }
    
    /**
     * @brief Tenta executar um bloco de codigo e trata qualquer excecao lancada.
     * @param friendlyMessage Mensagem a ser exibida em caso de falha.
     * @param block O bloco de codigo que pode lancar uma excecao.
     */
    inline fun runCatchingWithAlert(
        friendlyMessage: String,
        block: () -> Unit
    ) {
        try {
            block()
        } catch (e: Exception) {
            handleError(e, friendlyMessage, isFatal = true)
        }
    }

    /**
     * @brief Simula o envio de um relatorio de erro para um servico remoto.
     */
    private fun reportRemote(throwable: Throwable, message: String) {
        // Exemplo: Sentry.captureException(throwable)
        Log.i(TAG, "Relatorio de erro enviado para o servidor: $message")
    }
}

// ======================================================================
// SIMULAÇÕES DE CLASSES DE FRAMEWORK NECESSARIAS (Reutilizadas ou novas)
// ======================================================================

// Ja definido em Alert.kt:
// object Log { ... } 

// Anotacao de framework para Singletons
annotation class Singleton

