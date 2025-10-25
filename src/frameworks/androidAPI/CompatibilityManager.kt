package com.arcanos.launcher.frameworks.androidAPI

import android.content.Context
import android.os.Build
import android.widget.Toast
import arcanos.util.Log

/**
 * @brief Objeto Singleton responsável por gerenciar a compatibilidade com a API do Android.
 * Oferece métodos para verificar a versão atual do sistema.
 */
object CompatibilityManager {
    private const val TAG = "AndroidAPICheck"
    
    /**
     * A versão atual da API do Android em que o sistema está sendo executado.
     */
    val currentApiLevel: Int = Build.VERSION.SDK_INT

    /**
     * @brief Verifica se a API mínima necessária para uma funcionalidade está disponível.
     * @param requiredApiLevel O número da API (SDK_INT) necessário.
     * @return true se a versão atual for maior ou igual à requerida.
     */
    fun isApiLevelSupported(requiredApiLevel: Int): Boolean {
        return currentApiLevel >= requiredApiLevel
    }

    /**
     * @brief Executa uma funcionalidade que requer uma API mínima.
     * Se a API não for suportada, exibe uma mensagem de erro e retorna false.
     *
     * @param context Contexto para exibir o Toast de erro.
     * @param requiredApiLevel O número da API necessário.
     * @param featureName O nome da funcionalidade que está sendo usada (para o erro).
     * @return true se o código da funcionalidade puder ser executado, false caso contrário.
     */
    fun checkAndRun(context: Context, requiredApiLevel: Int, featureName: String): Boolean {
        if (!isApiLevelSupported(requiredApiLevel)) {
            val errorMessage = "ERRO: Funcionalidade '$featureName' requer API $requiredApiLevel, " +
                               "mas a API suportada é $currentApiLevel. Ação bloqueada."
            
            Log.e(TAG, errorMessage)
            
            // Exibe o erro para o usuário (em ambiente de teste/desenvolvimento)
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            
            return false
        }
        
        Log.d(TAG, "API $currentApiLevel suportada para '$featureName'. Executando...")
        return true
    }
}
