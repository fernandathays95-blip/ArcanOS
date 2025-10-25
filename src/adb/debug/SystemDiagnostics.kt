package com.arcanos.launcher.adb.debug

import arcanos.util.Log
import com.arcanos.launcher.adb.ShellExecutor
import com.arcanos.launcher.adb.CommandResult

/**
 * @brief Objeto Singleton que fornece ferramentas de diagnóstico e depuração do sistema
 * Arcanos, utilizando comandos ADB Shell de baixo nível.
 */
object SystemDiagnostics {
    private const val TAG = "SystemDiagnostics"

    /**
     * @brief Captura o logcat (logs do sistema) com um limite de linhas e filtro.
     *
     * @param maxLines O número máximo de linhas a serem capturadas.
     * @param filterTag Filtra logs por uma TAG específica (ex: "ArcanosCore").
     * @return O log capturado como uma string única.
     */
    fun captureLogcat(maxLines: Int = 100, filterTag: String? = null): String {
        val command = if (filterTag.isNullOrBlank()) {
            "logcat -d -t $maxLines" // Dump e limite de linhas
        } else {
            "logcat -d -t $maxLines -s $filterTag:*" // Dump, limite e filtro por TAG
        }
        
        Log.d(TAG, "Executando comando logcat: $command")
        
        // O logcat geralmente não requer root, rodamos com shell padrão
        val result = ShellExecutor.execute(listOf(command), useRoot = false)

        return if (result.success) {
            result.output
        } else {
            "Falha ao capturar logcat (Exit Code: ${result.exitCode}): ${result.error}"
        }
    }

    /**
     * @brief Executa o comando 'dumpsys' em um serviço específico do Android.
     * Dumpsys fornece informações detalhadas sobre serviços como 'activity', 'window', etc.
     *
     * @param serviceName O nome do serviço (ex: "activity", "power", "meminfo").
     * @param useRoot Se deve tentar usar permissões de root (necessário para alguns serviços).
     * @return O resultado do dumpsys.
     */
    fun getDumpSystemService(serviceName: String, useRoot: Boolean = false): String {
        val command = "dumpsys $serviceName"
        Log.d(TAG, "Executando dumpsys: $command (Root: $useRoot)")

        val result = ShellExecutor.execute(listOf(command), useRoot)

        if (result.success) {
            return result.output
        } else {
            return "Falha ao executar dumpsys $serviceName (Exit Code: ${result.exitCode}). Erro: ${result.error}"
        }
    }

    /**
     * @brief Gera um relatório de bugs (bugreport) completo (simulação).
     * Nota: O bugreport é um arquivo grande e geralmente requer permissão do sistema.
     * @return CommandResult da execução do comando 'bugreport'.
     */
    fun generateBugReport(): CommandResult {
        // O comando exato e as permissões necessárias variam por versão do Android.
        // Simulamos o comando moderno para gerar um arquivo zip.
        val command = "bugreport /data/local/tmp/arcanos_bugreport.zip"
        Log.w(TAG, "Tentando gerar bugreport completo (requer permissão do sistema/root): $command")
        
        // Bugreport pode requerer root em algumas situações
        return ShellExecutor.execute(listOf(command), useRoot = true)
    }
}
