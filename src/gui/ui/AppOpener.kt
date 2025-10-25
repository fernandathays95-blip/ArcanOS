package com.arcanos.launcher.gui.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.arcanos.launcher.gui.ui.AppCodeErrors
import com.arcanos.launcher.gui.ui.Alert
import arcanos.util.Log

/**
 * @brief Utilitario Singleton para abrir outros aplicativos instalados no dispositivo.
 * Ele tenta iniciar o aplicativo pelo nome do pacote ou redireciona para a Play Store,
 * usando o AppCodeErrors para tratamento de excecoes.
 */
object AppOpener {

    private const val TAG = "AppOpener"
    private const val MARKET_URI_PREFIX = "market://details?id="
    private const val WEB_PLAY_STORE_URI_PREFIX = "https://play.google.com/store/apps/details?id="

    /**
     * @brief Tenta abrir um aplicativo pelo seu nome de pacote.
     * Se o app não estiver instalado, oferece a opcao de ir para a Play Store.
     * * @param context O Context (ex: Activity) necessario para iniciar uma nova Intent.
     * @param packageName O nome do pacote do aplicativo a ser aberto (ex: "com.whatsapp").
     * @return true se a acao de abertura foi bem-sucedida (abriu o app ou Play Store), false se falhar.
     */
    fun openApp(context: Context, packageName: String): Boolean {
        // 1. Tenta obter a Intent de Lançamento (Launch Intent) do aplicativo.
        val launchIntent: Intent? = context.packageManager.getLaunchIntentForPackage(packageName)
        
        if (launchIntent != null) {
            // 2. Se a Intent for encontrada, inicia o aplicativo.
            // Usamos AppCodeErrors.runCatchingWithAlert para encapsular a chamada de UI,
            // garantindo que qualquer SecurityException ou outro erro seja tratado.
            AppCodeErrors.runCatchingWithAlert(
                context = context,
                friendlyMessage = "Falha ao iniciar o aplicativo: $packageName.",
                block = {
                    // É obrigatório adicionar FLAG_ACTIVITY_NEW_TASK se o Context
                    // não for uma Activity.
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(launchIntent)
                    Log.i(TAG, "Aplicativo '$packageName' lançado com sucesso.")
                }
            )
            return true
        } else {
            // 3. O aplicativo não está instalado. Redireciona para a Play Store.
            Log.w(TAG, "Aplicativo '$packageName' não encontrado. Redirecionando para a Play Store.")
            openInPlayStore(context, packageName)
            return false // Retorna falso porque o app original não foi aberto.
        }
    }

    /**
     * @brief Abre a pagina do aplicativo na Google Play Store.
     * @param context O Context necessario.
     * @param packageName O nome do pacote do aplicativo.
     */
    private fun openInPlayStore(context: Context, packageName: String) {
        // Tenta abrir o link do Market. Usamos o handler de erros para robustez.
        AppCodeErrors.runCatchingWithAlert(
            context = context,
            friendlyMessage = "Não foi possível abrir a Play Store.",
            block = {
                // Tenta abrir diretamente o app da Play Store (URI "market://...")
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URI_PREFIX + packageName))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                
                Alert.showToast(context, "App não instalado. Abrindo Play Store.")
            }
        ) {
            // Se falhar (por exemplo, Play Store não instalada ou indisponível), 
            // tenta abrir no navegador.
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(WEB_PLAY_STORE_URI_PREFIX + packageName))
            webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(webIntent)
            
            Alert.showToast(context, "App não instalado. Abrindo Play Store no navegador.")
        }
    }
}


// ======================================================================
// SIMULAÇÕES DE CLASSES DE FRAMEWORK E UTILITÁRIOS NECESSÁRIOS
// (Para fins de demonstração, assumindo que foram definidos em AppCodeErrors.kt e Alert.kt)
// ======================================================================

// **Estas classes abaixo precisam ser implementadas em seus respectivos arquivos
// (Alert.kt, AppCodeErrors.kt) para que o AppOpener.kt compile e funcione corretamente**

// 1. Simulação de classes Android (Requerimentos)
// abstract class Context { ... } 
// abstract class PackageManager { ... } 
// class Intent(action: String, uri: Uri?) { ... }
// class Uri { companion object { fun parse(uriString: String): Uri { ... } } }

// 2. Simulação de classes de Log
// object Log { ... }

// 3. Importacoes necessarias do nosso proprio pacote:
// object Alert { ... }
// object AppCodeErrors { ... }
