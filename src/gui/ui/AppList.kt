package com.arcanos.launcher.gui.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import arcanos.util.Log
import arcanos.util.Singleton
import androidx.annotation.WorkerThread
import com.arcanos.launcher.R // Simulação para recursos

/**
 * @brief Modelo de dados que representa um aplicativo que pode ser exibido e lançado.
 * Ele inclui aplicativos nativos do Android e aplicativos compatíveis com o sistema Arcanos.
 *
 * @param label O nome de exibição do aplicativo.
 * @param packageName O nome do pacote Android (único).
 * @param iconDrawable O ícone do aplicativo para exibição na UI.
 * @param isArcanosNative Indica se o aplicativo é nativo do ecossistema Arcanos (além do Android).
 */
data class AppModel(
    val label: String,
    val packageName: String,
    val iconDrawable: Drawable?,
    val isArcanosNative: Boolean = false
)

/**
 * @brief Objeto Singleton responsável por carregar a lista completa de aplicativos
 * instalados (Android) e de sistema (Arcanos) que devem ser exibidos no Launcher.
 */
@Singleton
object AppListLoader {

    private const val TAG = "AppListLoader"

    // Cache interno para evitar recarregamento pesado a cada vez
    private var cachedAppList: List<AppModel>? = null

    /**
     * @brief Carrega a lista completa de aplicativos. Este método deve ser chamado
     * em um thread de background para evitar bloquear a UI (WorkerThread).
     *
     * @param context O Context necessário para acessar o PackageManager.
     * @param forceReload Se true, ignora o cache e recarrega do sistema.
     * @return Uma lista de AppModel com todos os aplicativos lançáveis.
     */
    @WorkerThread
    fun getLauncherApps(context: Context, forceReload: Boolean = false): List<AppModel> {
        if (cachedAppList != null && !forceReload) {
            Log.d(TAG, "Retornando lista de aplicativos em cache.")
            return cachedAppList!!
        }

        val packageManager = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        // 1. Obter todos os aplicativos lançáveis do Android (compatibilidade total)
        val allApps = packageManager.queryIntentActivities(mainIntent, 0)
        
        val appList = mutableListOf<AppModel>()

        for (info: ResolveInfo in allApps) {
            val packageName = info.activityInfo.packageName
            val label = info.loadLabel(packageManager).toString()
            val iconDrawable = info.loadIcon(packageManager)

            // Filtra o próprio launcher (opcional, mas comum para evitar redundância)
            if (packageName != context.packageName) {
                appList.add(
                    AppModel(
                        label = label,
                        packageName = packageName,
                        iconDrawable = iconDrawable,
                        isArcanosNative = isArcanosNativeApp(packageName) // Verifica se é nativo Arcanos
                    )
                )
            }
        }

        // 2. Adicionar quaisquer aplicativos Arcanos nativos que não sejam apps Android tradicionais
        //    (Simulação: Se o Arcanos tivesse apps que não aparecem no CATEGORY_LAUNCHER)
        // appList.addAll(getArcanosSystemApps(context)) 
        
        // 3. Adicionar o próprio Launcher Arcanos (pois ele foi filtrado acima)
        try {
            val selfInfo = packageManager.getApplicationInfo(context.packageName, 0)
            appList.add(
                AppModel(
                    label = context.getString(R.string.launcher_app_name), // Ex: "Launcher Arcanos"
                    packageName = context.packageName,
                    iconDrawable = selfInfo.loadIcon(packageManager),
                    isArcanosNative = true
                )
            )
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Não foi possível carregar as informações do próprio Launcher.", e)
        }


        // Ordenar a lista (geralmente por nome)
        val sortedList = appList.sortedBy { it.label.lowercase() }
        
        // Armazenar no cache e retornar
        cachedAppList = sortedList
        Log.i(TAG, "Lista de aplicativos carregada com ${sortedList.size} itens.")
        return sortedList
    }
    
    /**
     * @brief Simula uma verificação se o aplicativo pertence ao nosso sistema Arcanos.
     * @param packageName O nome do pacote.
     * @return true se for um componente essencial do sistema Arcanos.
     */
    private fun isArcanosNativeApp(packageName: String): Boolean {
        // Ex: Todos os pacotes que começam com "com.arcanos.system" são nativos.
        return packageName.startsWith("com.arcanos.")
    }

    /**
     * @brief Limpa o cache para forçar um recarregamento na próxima chamada.
     */
    fun clearCache() {
        cachedAppList = null
        Log.d(TAG, "Cache da lista de aplicativos limpo.")
    }
}

// ======================================================================
// SIMULAÇÕES DE CLASSES DE FRAMEWORK E UTILITÁRIOS
// (Para garantir que o Kotlin compile a estrutura)
// ======================================================================

// O uso de Drawables e Contexts reais é assumido.

// annotation class WorkerThread // Em um projeto real, de androidx.annotation
// object Singleton // Em um projeto real, seria apenas um `object`

// Simulação de recursos do sistema
object R {
    object string {
        // Assume que existe no seu `strings.xml`
        const val launcher_app_name = 0 // Simula o ID do recurso
    }
}
