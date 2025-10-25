package com.arcanos.launcher.gui.ui

import android.content.Context
import arcanos.util.Log
import com.arcanos.launcher.gui.ui.AppCodeErrors

/**
 * @brief Arquivo de funcoes de extensao e utilitarios para garantir a execucao 
 * segura de blocos de codigo que interagem com o sistema ou UI.
 *
 * Ele fornece uma camada de protecao para operacoes criticas, como abrir aplicativos 
 * (`AppOpener`) ou interagir com componentes potencialmente instaveis.
 */

private const val TAG = "ExecutionSafety"

/**
 * @brief Executa um bloco de código com proteção contra exceções não tratadas,
 * usando o manipulador central `AppCodeErrors`.
 *
 * Esta é uma variação de `AppCodeErrors.runCatchingWithAlert` que usa um valor de
 * retorno genérico para permitir que o chamador decida o que fazer após a falha.
 *
 * @param context O Context necessário para o manipulador de erros (UI).
 * @param friendlyMessage Mensagem amigável para o usuário em caso de falha.
 * @param block O bloco de código a ser executado.
 * @return O resultado do bloco `block` se for bem-sucedido, ou um valor 
 * padrão (`defaultReturnValue`) se uma exceção for capturada.
 */
inline fun <T> runSafely(
    context: Context,
    friendlyMessage: String,
    defaultReturnValue: T,
    block: () -> T
): T {
    return try {
        Log.d(TAG, "Tentando executar bloco de código com segurança...")
        block()
    } catch (e: Exception) {
        // Usa o manipulador central para logar, alertar e reportar o erro.
        // O erro é tratado como FATAL para garantir que o usuário veja a notificação.
        AppCodeErrors.handleError(
            context = context,
            throwable = e,
            friendlyMessage = friendlyMessage,
            isFatal = true 
        )
        // Retorna o valor padrão para que o fluxo do programa continue sem travar.
        defaultReturnValue
    } catch (e: Error) {
        // Captura erros graves (como OutOfMemoryError)
        AppCodeErrors.handleError(
            context = context,
            throwable = e,
            friendlyMessage = "Erro Crítico do Sistema! $friendlyMessage",
            isFatal = true
        )
        defaultReturnValue
    }
}

// ======================================================================
// Exemplo de Uso (Aplicado ao AppOpener para robustez)
// ======================================================================

/*
// Se AppOpener.kt fosse refatorado para usar essa função:
fun openApp(context: Context, packageName: String): Boolean {
    // ... tenta obter a Intent ...
    
    // Antes:
    // context.startActivity(launchIntent)

    // Depois (Usando runSafely):
    return runSafely(
        context = context,
        friendlyMessage = "Não foi possível lançar o aplicativo '$packageName' devido a um problema interno.",
        defaultReturnValue = false // Retorno padrão em caso de falha
    ) {
        val launchIntent: Intent? = context.packageManager.getLaunchIntentForPackage(packageName)

        if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(launchIntent)
            Log.i(TAG, "Aplicativo '$packageName' lançado com segurança.")
            true // Retorno de sucesso
        } else {
            // ... lógica de Play Store ...
            false // Retorno de falha no launch
        }
    }
}
*/


// ======================================================================
// SIMULAÇÕES DE CLASSES DE FRAMEWORK NECESSARIAS (Reutilizadas)
// ======================================================================
// Já definido em AppCodeErrors.kt:
// abstract class Context { ... } 
// object AppCodeErrors { fun handleError(...) { ... } }
// object Log { fun d(...) { ... } }
