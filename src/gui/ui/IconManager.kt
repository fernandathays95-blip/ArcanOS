// src/gui/ui/IconManager.kt
package com.arcanos.launcher.ui

// Importacoes simuladas de frameworks de UI/OS do ArcanOS
import arcanos.graphics.Bitmap
import arcanos.resources.ResourceManager
import arcanos.app.PackageInfo
import arcanos.util.LruCache
import arcanos.util.Log

/**
 * @brief Singleton responsavel por carregar, armazenar em cache e fornecer
 * os icones dos aplicativos do sistema ArcanOS.
 * * Implementa um cache LRU (Least Recently Used) para evitar recargas constantes
 * de recursos, otimizando o desempenho do Launcher.
 */
object IconManager {

    // Tamanho maximo do cache: 100 icones (um bom default para um launcher)
    private const val MAX_CACHE_SIZE = 100 
    
    // O cache que armazena pares de PackageName -> Bitmap
    private val iconCache = LruCache<String, Bitmap>(MAX_CACHE_SIZE)
    
    // Simula a injecao de dependencias do ResourceManager
    private val resourceManager = ResourceManager.getInstance() 

    /**
     * @brief Obtem o icone para um determinado pacote.
     * Tenta carregar do cache primeiro; se falhar, carrega da fonte do pacote.
     * * @param packageInfo O objeto PackageInfo contendo metadados do aplicativo.
     * @return Bitmap O icone do aplicativo. Retorna um icone padrao se falhar.
     */
    fun getIcon(packageInfo: PackageInfo): Bitmap {
        val packageName = packageInfo.name

        // 1. Tentar carregar do cache
        iconCache[packageName]?.let { cachedIcon ->
            Log.v("IconManager", "Icone '$packageName' obtido do cache.")
            return cachedIcon
        }

        // 2. Carregar o icone do recurso
        val loadedIcon = loadIconFromPackage(packageInfo)
        
        // 3. Adicionar ao cache
        iconCache.put(packageName, loadedIcon)

        return loadedIcon
    }

    /**
     * @brief Limpa o icone de um pacote especifico do cache.
     * Usado quando um aplicativo e atualizado ou desinstalado.
     * * @param packageName O nome do pacote a ser invalidado.
     */
    fun invalidateIcon(packageName: String) {
        iconCache.remove(packageName)
        Log.i("IconManager", "Cache de icone invalidado para '$packageName'.")
    }

    /**
     * @brief Carrega o icone do aplicativo a partir de seus recursos.
     * (Funcao de simulacao de I/O de baixo nivel)
     */
    private fun loadIconFromPackage(packageInfo: PackageInfo): Bitmap {
        // Logica real: 
        // 1. Abre o arquivo APK/APPX do pacote.
        // 2. Le o caminho do icone definido no Manifest.
        // 3. Decodifica o recurso de imagem (PNG/Vector Drawable) para Bitmap.
        
        val resourceId = packageInfo.iconResourceId
        
        return try {
            resourceManager.loadBitmap(resourceId)
        } catch (e: Exception) {
            Log.e("IconManager", "Falha ao carregar icone de ${packageInfo.name}. Usando padrao.", e)
            resourceManager.getDefaultAppIcon()
        }
    }
}

// SIMULAÇÕES DE CLASSES DO FRAMEWORK ARCANOS
// Estas classes seriam as APIs do ArcanOS, possivelmente implementadas em C++ com binding JNI/Kotlin

class PackageInfo(val name: String, val iconResourceId: Int)
class ResourceManager {
    companion object {
        fun getInstance() = ResourceManager()
    }
    fun loadBitmap(resourceId: Int): Bitmap {
        // Simula o carregamento (retorna um Bitmap placeholder)
        return Bitmap(resourceId) 
    }
    fun getDefaultAppIcon(): Bitmap {
        // Simula o icone padrao do sistema
        return Bitmap(0) 
    }
}

// Nota: O LruCache real do Android/Java seria usado, mas aqui e apenas uma representacao.
// O Bitmap tambem e uma classe de UI nativa simulada.
