package com.arcanos.launcher.frameworks.androidAPI

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import arcanos.util.Log

/**
 * @brief Utilitário que lida com o lançamento de componentes Android (Atividades)
 * e gerencia opções de API específicas.
 */
object ActivityLauncher {
    private const val TAG = "ActivityLauncher"

    // Ações de API que dependem de versão
    private const val API_LEVEL_FOR_ADVANCED_OPTIONS = Build.VERSION_CODES.LOLLIPOP // API 21

    /**
     * @brief Lança uma Intent (atividade) com a opção de usar animações e
     * opções de lançamento avançadas, dependendo da versão do Android.
     *
     * @param context Contexto da aplicação.
     * @param intent A Intent a ser lançada.
     * @param useAdvancedOptions Se true, tenta usar ActivityOptions avançadas.
     */
    fun start(context: Context, intent: Intent, useAdvancedOptions: Boolean = true) {
        if (useAdvancedOptions && CompatibilityManager.isApiLevelSupported(API_LEVEL_FOR_ADVANCED_OPTIONS)) {
            
            // API 21+ suporta ActivityOptions para animações de transição
            val featureName = "Lançamento com ActivityOptions"
            
            if (CompatibilityManager.checkAndRun(context, API_LEVEL_FOR_ADVANCED_OPTIONS, featureName)) {
                
                try {
                    // Simulação de transição de janela do Android (para o Android 5.0+)
                    val options = ActivityOptions.makeCustomAnimation(
                        context, 
                        android.R.anim.fade_in, 
                        android.R.anim.fade_out
                    )
                    
                    context.startActivity(intent, options.toBundle())
                    Log.d(TAG, "Atividade lançada com ActivityOptions (API $API_LEVEL_FOR_ADVANCED_OPTIONS+)")
                    return
                } catch (e: Exception) {
                    Log.e(TAG, "Falha ao usar ActivityOptions, lançando Intent padrão.", e)
                    // Continua para o lançamento padrão abaixo em caso de falha
                }
            }
        }
        
        // Lançamento padrão (Fallback)
        try {
            context.startActivity(intent)
            Log.d(TAG, "Atividade lançada via Intent padrão.")
        } catch (e: Exception) {
            Log.e(TAG, "Falha crítica ao lançar atividade via Intent.", e)
            // Em caso de falha, você pode notificar a UI de volta.
        }
    }
}
