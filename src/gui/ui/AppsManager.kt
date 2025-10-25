// src/gui/ui/AppsManager.kt
package com.arcanos.launcher.ui

// Importacoes simuladas de frameworks do ArcanOS
import arcanos.app.PackageInfo
import arcanos.app.PackageManager
import arcanos.threading.AsyncExecutor // Simula o Dispatcher/CoroutineScope
import arcanos.util.Log

// Um alias para PackageInfo para clareza no contexto da UI.
typealias App = PackageInfo

/**
 * @brief Singleton responsavel por interagir com o PackageManager do sistema,
 * fornecendo uma lista de aplicativos instalados de forma assincrona.
 * * Usado pelo Launcher para popular o App Drawer.
 */
object AppsManager {

    // Simula a instancia do Package Manager (API de baixo nivel)
    private val packageManager = PackageManager.getInstance() 
    
    // Lista mutavel que mantem o estado atual dos aplicativos instalados.
    private var installedApps: List<App> = emptyList()

    /**
     * @brief Interface de callback para notificar a UI sobre mudancas.
     */
    interface AppUpdateListener {
        fun onAppsLoaded(apps: List<App>)
        fun onAppRemoved(packageName: String)
        fun onAppAdded(app: App)
    }

    // Lista de componentes da UI (ex: App Drawer) que precisam ser notificados.
    private val listeners = mutableSetOf<AppUpdateListener>()

    /**
     * @brief Registra um componente para receber notificacoes sobre mudancas na lista de apps.
     */
    fun registerListener(listener: AppUpdateListener) {
        listeners.add(listener)
        // Envia a lista atual imediatamente, se ja estiver carregada
        if (installedApps.isNotEmpty()) {
            listener.onAppsLoaded(installedApps)
        }
    }

    /**
     * @brief Remove um componente da lista de notificadores.
     */
    fun unregisterListener(listener: AppUpdateListener) {
        listeners.remove(listener)
    }

    /**
     * @brief Inicia o processo de carregamento de todos os aplicativos instalados.
     * Deve ser chamada de forma assincrona para nao bloquear a UI.
     */
    fun loadInstalledAppsAsync() {
        Log.i("AppsManager", "Iniciando carregamento assincrono de apps.")
        
        // Simula o uso de Coroutines ou Thread Pool para trabalho em background
        AsyncExecutor.execute {
            val allPackages = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
            
            // Filtra e mapeia para a classe App (PackageInfo)
            val launchableApps = allPackages
                .filter { it.isLaunchable }
                .sortedBy { it.label } // Ordena pelo nome do aplicativo

            installedApps = launchableApps
            Log.d("AppsManager", "Carregamento concluido. Total de apps: ${installedApps.size}")
            
            // Notifica todos os listeners na thread principal (UI thread)
            listeners.forEach { listener -> 
                listener.onAppsLoaded(installedApps) 
            }
        }
    }

    /**
     * @brief Inicia um aplicativo pelo seu objeto PackageInfo.
     * @param app O aplicativo a ser iniciado.
     */
    fun launchApp(app: App) {
        try {
            packageManager.startActivity(app.launchIntent)
            Log.i("AppsManager", "Iniciando aplicativo: ${app.label}")
        } catch (e: Exception) {
            Log.e("AppsManager", "Falha ao iniciar ${app.label}", e)
            // Aqui, a UI deveria exibir um Toast ou notificacao de erro.
        }
    }
    
    // ======================================================================
    // Simula eventos do sistema (recebidos do kernel/Package Manager Service)
    // ======================================================================
    
    fun onPackageRemoved(packageName: String) {
        installedApps = installedApps.filter { it.name != packageName }
        listeners.forEach { it.onAppRemoved(packageName) }
        IconManager.invalidateIcon(packageName)
        Log.w("AppsManager", "Pacote removido: $packageName. Cache de icone invalidado.")
    }

    fun onPackageAdded(app: App) {
        installedApps = (installedApps + app).sortedBy { it.label }
        listeners.forEach { it.onAppAdded(app) }
        // Icone sera carregado sob demanda, mas o AppsManager so notifica.
        Log.i("AppsManager", "Pacote adicionado: ${app.name}.")
    }
}

// SIMULAÇÕES DE CLASSES DO FRAMEWORK ARCANOS
// Representacao simplificada das APIs de baixo nivel

class PackageManager {
    companion object {
        const val GET_ACTIVITIES = 1 shl 0
        fun getInstance() = PackageManager()
    }

    // Simula a obtencao de todos os pacotes instalados
    fun getInstalledPackages(flags: Int): List<PackageInfo> {
        // Retorna uma lista de apps simulados para fins de teste
        return listOf(
            PackageInfo("com.arcanos.sys.settings", 0).apply { label = "Settings"; isLaunchable = true; launchIntent = "SettingsIntent" },
            PackageInfo("com.arcanos.sys.saude", 1).apply { label = "Saude"; isLaunchable = true; launchIntent = "SaudeIntent" },
            PackageInfo("com.thirdparty.game.brick", 2).apply { label = "Brick Breaker"; isLaunchable = true; launchIntent = "BrickGameIntent" },
            PackageInfo("com.arcanos.kernel.service", 3).apply { label = "Kernel Service"; isLaunchable = false } // Nao lancavel
        )
    }

    fun startActivity(intent: String) {
        // Simula o envio de um intent para o SystemServer
        Log.v("PackageManager", "Intent de lancamento: $intent")
    }
}

class PackageInfo(val name: String, val iconResourceId: Int) {
    var label: String = name
    var isLaunchable: Boolean = false
    var launchIntent: String = ""
}

// Simula a classe de execucao assincrona
object AsyncExecutor {
    fun execute(runnable: () -> Unit) {
        // No Android, seria um CoroutineScope(Dispatchers.IO)
        // Aqui, simula a execucao imediata (apenas para compilacao)
        runnable() 
    }
}
