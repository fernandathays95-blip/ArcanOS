package com.arcanos.launcher.linux

import arcanos.util.Log
import arcanos.util.Singleton
import java.io.File
import java.io.IOException

/**
 * @brief Objeto Singleton que fornece uma interface de baixo nível
 * para interagir com o Kernel Linux através dos sistemas de arquivos sysfs e procfs.
 * Requer permissões de sistema (platform_apis/root) para escrita.
 */
@Singleton
object KernelInterface {
    private const val TAG = "LinuxKernel"

    // Caminhos comuns para interação com o Kernel (sysfs e procfs)
    // Estes caminhos são padrões em distribuições Linux e Android.
    private const val CPU_GOVERNOR_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor"
    private const val MEMINFO_PATH = "/proc/meminfo"
    private const val KERNEL_VERSION_PATH = "/proc/version"

    /**
     * @brief Lê o conteúdo de um arquivo de status do Kernel (sysfs ou procfs).
     * @param path O caminho para o arquivo (ex: /proc/meminfo).
     * @return O conteúdo do arquivo como String, ou null se houver erro.
     */
    private fun readKernelFile(path: String): String? {
        val file = File(path)
        if (!file.exists() || !file.canRead()) {
            // Em um sistema real, o logcat mostraria esta mensagem se a permissão estivesse errada.
            Log.e(TAG, "Falha ao ler o arquivo do Kernel. Caminho não acessível: $path")
            return null
        }
        
        return try {
            file.readText().trim()
        } catch (e: IOException) {
            Log.e(TAG, "Erro de I/O ao ler o arquivo $path", e)
            null
        }
    }

    /**
     * @brief Escreve um novo valor em um arquivo de controle do Kernel (sysfs).
     * Requer permissões elevadas (platform_apis/root) para escrita.
     *
     * @param path O caminho para o arquivo de controle (ex: /sys/...).
     * @param value O valor a ser escrito (ex: "performance").
     * @return true se a escrita foi bem-sucedida.
     */
    fun writeKernelControl(path: String, value: String): Boolean {
        val file = File(path)
        if (!file.exists() || !file.canWrite()) {
            // A falha aqui geralmente indica falta de privilégios de root/sistema.
            Log.e(TAG, "Falha ao escrever no arquivo do Kernel. Permissão negada ou arquivo não existe: $path")
            return false
        }
        
        return try {
            file.writeText(value)
            Log.i(TAG, "Valor '$value' escrito com sucesso em $path")
            true
        } catch (e: IOException) {
            Log.e(TAG, "Erro de I/O ao escrever no arquivo $path", e)
            false
        }
    }

    // =================================================================
    // Funções de Ação Específicas do Sistema Arcanos
    // =================================================================

    /**
     * @brief Obtém o governador de frequência da CPU atualmente ativo.
     * @return O nome do governador (ex: "interactive", "powersave", "performance").
     */
    fun getCurrentCpuGovernor(): String {
        return readKernelFile(CPU_GOVERNOR_PATH) ?: "unknown"
    }

    /**
     * @brief Tenta definir o governador da CPU para o modo de desempenho máximo.
     * Requer permissões de root/sistema.
     * @return true se a operação foi bem-sucedida.
     */
    fun setCpuGovernorToPerformance(): Boolean {
        Log.w(TAG, "Tentando definir o governador para 'performance'...")
        // Em um sistema real, você usaria o ShellExecutor com root para garantir a escrita.
        // Aqui, simulamos a tentativa de escrita direta.
        return writeKernelControl(CPU_GOVERNOR_PATH, "performance")
    }

    /**
     * @brief Obtém a string de versão completa do Kernel Linux.
     * @return A versão do Kernel (primeira parte da string em /proc/version).
     */
    fun getKernelVersion(): String {
        val fullInfo = readKernelFile(KERNEL_VERSION_PATH) ?: "Versão do Kernel Indisponível."
        // Retorna a primeira palavra, que geralmente é a versão (ex: "5.15.0-android13-9-g3032d8494f6c")
        return fullInfo.split(' ')[0]
    }
}
