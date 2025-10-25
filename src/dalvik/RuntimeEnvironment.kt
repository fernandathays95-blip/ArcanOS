package com.arcanos.launcher.dalvik

import arcanos.util.Log
import arcanos.util.Singleton
import java.lang.management.ManagementFactory

/**
 * @brief Objeto Singleton que interage com o ambiente de execução do Android (ART/Dalvik).
 * Fornece métricas de baixo nível e controle sobre o tempo de execução.
 */
@Singleton
object RuntimeEnvironment {
    private const val TAG = "ART_Runtime"
    
    // Referência direta ao objeto Runtime do Java
    private val runtime: Runtime = Runtime.getRuntime()

    /**
     * @brief Recupera a quantidade de memória livre atualmente disponível para a máquina virtual.
     * @return Memória livre em megabytes (MB).
     */
    fun getFreeMemoryMB(): Long {
        return runtime.freeMemory() / (1024 * 1024)
    }

    /**
     * @brief Recupera a memória total alocada (heap) para a máquina virtual.
     * @return Memória total em megabytes (MB).
     */
    fun getTotalMemoryMB(): Long {
        return runtime.totalMemory() / (1024 * 1024)
    }

    /**
     * @brief Tenta sugerir que a máquina virtual execute uma coleta de lixo (Garbage Collection).
     * Essa chamada é apenas uma *sugestão* e não garante a execução imediata.
     * (Deve ser usada com cautela, apenas em pontos otimizados do código).
     */
    fun suggestGarbageCollection() {
        Log.i(TAG, "Sugestão de Coleta de Lixo solicitada. Memória Livre antes: ${getFreeMemoryMB()}MB")
        runtime.gc()
        
        // Simulação de esperar um ciclo para ver o efeito (em produção, evitar sleep)
        Thread.sleep(50) 
        Log.i(TAG, "Memória Livre depois da sugestão de GC: ${getFreeMemoryMB()}MB")
    }

    /**
     * @brief Verifica se o ambiente de execução atual é o ART (padrão moderno) ou o Dalvik antigo.
     * @return O nome do ambiente de execução.
     */
    fun getRuntimeName(): String {
        return try {
            val vmName = System.getProperty("java.vm.name") ?: "Desconhecido"
            if (vmName.contains("ART", ignoreCase = true)) {
                "ART (Android Runtime)"
            } else if (vmName.contains("Dalvik", ignoreCase = true)) {
                "Dalvik (Obsoleto)"
            } else {
                vmName
            }
        } catch (e: Exception) {
            Log.e(TAG, "Falha ao obter o nome do Runtime.", e)
            "Desconhecido (Falha na Reflexão)"
        }
    }
}
