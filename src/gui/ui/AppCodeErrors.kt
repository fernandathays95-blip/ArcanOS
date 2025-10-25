// src/gui/ui/AppCodeErrors.kt
package com.arcanos.launcher.gui.ui

import android.content.Context
import arcanos.util.Log
import arcanos.util.Singleton
import com.arcanos.launcher.gui.ui.Alert // Importa Alert do mesmo pacote UI

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
     * @param context O Context necessario para exibir notificacoes (via Alert.kt).
     * @param throwable A excecao (erro) que ocorreu.
     * @param friendlyMessage Uma mensagem amigavel para mostrar ao usuario.
     * @param isFatal Se o erro e considerado critico para a operacao.
     */
    fun handleError(
        context: Context, // Recebe o Context
        throwable: Throwable, 
        friendlyMessage: String = "Ocorreu um erro inesperado. Tente novamente.",
        isFatal: Boolean = false
    ) {
        // 1. Loga o erro completo para o console (necessario para debugging)
        Log.e(TAG, "ERRO CAPTURADO: $friendlyMessage. Detalhes: ${throwable.stackTraceToString()}")

        // 2. Decide a acao na UI usando Alert.kt
        if (isFatal) {
            // Se for fatal, mostra um alerta
            Alert.showAlertDialog(
                context, 
                title = "Erro Crítico do Sistema",
                message = "$friendlyMessage\n\nDetalhes tecnicos: ${throwable.message ?: "N/A"}"
            )
        } else {
            // Se for menos critico, mostra um toast discreto
            Alert.showToast(context, friendlyMessage)
        }

        // 3. *Simulacao de Relatorio Remoto*
        reportRemote(throwable, friendlyMessage)
    }
    
    /**
     * @brief Tenta executar um bloco de codigo e trata qualquer excecao lancada.
     * @param context O Context para o handler.
     * @param friendlyMessage Mensagem a ser exibida em caso de falha.
     * @param block O bloco de codigo que pode lancar uma excecao.
     */
    inline fun runCatchingWithAlert(
        context: Context,
        friendlyMessage: String,
        block: () -> Unit
    ) {
        try {
            block()
        } catch (e: Exception) {
            // Chama a funcao principal de manipulacao de erro
            handleError(context, e, friendlyMessage, isFatal = true)
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
// abstract class Context { ... }

// Anotacao de framework para Singletons
annotation class Singleton
