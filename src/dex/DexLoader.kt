package com.arcanos.launcher.dex

import android.content.Context
import arcanos.util.Log
import arcanos.util.Singleton
import dalvik.system.DexClassLoader
import java.io.File
import java.lang.reflect.Method

/**
 * @brief Objeto Singleton responsável pelo carregamento dinâmico de código DEX.
 * Permite que o sistema Arcanos carregue e execute módulos/plugins externos.
 */
@Singleton
object DexLoader {
    private const val TAG = "ArcanosDexLoader"

    // Cache para class loaders já carregados
    private val loadedClassLoaders = mutableMapOf<String, DexClassLoader>()

    /**
     * @brief Carrega um arquivo DEX (módulo) dinamicamente e tenta instanciar uma classe.
     *
     * @param context O Contexto da aplicação.
     * @param dexFile O arquivo DEX ou APK que contém o código a ser carregado.
     * @param className O nome totalmente qualificado da classe (ex: com.arcanos.plugin.MeuPlugin).
     * @return Uma instância do objeto carregado, ou null em caso de falha.
     */
    fun loadAndInstantiateClass(context: Context, dexFile: File, className: String): Any? {
        if (!dexFile.exists() || !dexFile.canRead()) {
            Log.e(TAG, "Arquivo DEX não encontrado ou não pode ser lido: ${dexFile.absolutePath}")
            return null
        }

        val key = dexFile.absolutePath
        
        // Usa o cache se o loader já estiver pronto
        val classLoader = loadedClassLoaders.getOrPut(key) {
            Log.i(TAG, "Criando novo DexClassLoader para: $key")
            
            // O diretório de otimização é necessário para o DexClassLoader
            val optimizedDir = context.getDir("dex_opt", Context.MODE_PRIVATE)
            
            DexClassLoader(
                dexFile.absolutePath, 
                optimizedDir.absolutePath, 
                null, 
                context.classLoader // Usamos o ClassLoader padrão como pai
            )
        }

        try {
            // Tenta carregar a classe a partir do ClassLoader
            val loadedClass = classLoader.loadClass(className)
            Log.i(TAG, "Classe carregada com sucesso: $className")

            // Tenta instanciar o objeto (supõe que há um construtor sem argumentos)
            return loadedClass.newInstance()
        } catch (e: ClassNotFoundException) {
            Log.e(TAG, "Classe não encontrada no arquivo DEX: $className", e)
        } catch (e: InstantiationException) {
            Log.e(TAG, "Falha ao instanciar a classe: $className. Verifique o construtor.", e)
        } catch (e: Exception) {
            Log.e(TAG, "Erro desconhecido ao carregar ou instanciar classe DEX.", e)
        }

        return null
    }

    /**
     * @brief Tenta executar um método específico de um objeto carregado dinamicamente.
     * (Exemplo de reflexão sobre o código carregado)
     */
    fun executeMethod(loadedObject: Any, methodName: String, vararg args: Any): Any? {
        try {
            val clazz = loadedObject.javaClass
            
            // Simulação simples: buscar o primeiro método com o nome, ignorando os tipos de args
            val method: Method = clazz.methods.firstOrNull { it.name == methodName } 
                ?: throw NoSuchMethodException("Método '$methodName' não encontrado em ${clazz.name}")
            
            Log.d(TAG, "Executando método '$methodName' dinamicamente.")
            return method.invoke(loadedObject, *args)

        } catch (e: Exception) {
            Log.e(TAG, "Falha na execução dinâmica do método '$methodName'.", e)
            return null
        }
    }
}
