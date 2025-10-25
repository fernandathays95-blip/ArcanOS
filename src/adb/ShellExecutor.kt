package com.arcanos.launcher.adb

import arcanos.util.Log
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.StringBuilder

/**
 * @brief Objeto Singleton para executar comandos shell no ambiente de execução do Android.
 * Permite a execução de comandos como usuário comum (shell) ou com privilégios de root (su).
 */
object ShellExecutor {
    private const val TAG = "ArcanosShell"

    /**
     * @brief Executa um ou mais comandos no shell.
     *
     * @param commands Lista de strings de comandos a serem executados.
     * @param useRoot Se true, tenta executar os comandos usando 'su' (requer dispositivo root/privilégio).
     * @return Um objeto CommandResult contendo a saída, erro e status de sucesso.
     */
    fun execute(commands: List<String>, useRoot: Boolean = false): CommandResult {
        var process: Process? = null
        val stdout = StringBuilder()
        val stderr = StringBuilder()
        var exitCode = -1

        try {
            // Se for root, executa o shell como 'su'. Caso contrário, como 'sh'.
            val shell = if (useRoot) "su" else "sh"
            process = Runtime.getRuntime().exec(shell)

            // Escreve os comandos no InputStream do processo
            val os = DataOutputStream(process.outputStream)
            for (command in commands) {
                Log.d(TAG, "Executando [$shell]: $command")
                os.writeBytes(command + "\n")
            }
            os.writeBytes("exit\n") // Finaliza o shell após os comandos
            os.flush()
            os.close()

            // Lê a saída padrão (STDOUT)
            val stdoutReader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            while (stdoutReader.readLine().also { line = it } != null) {
                stdout.append(line).append("\n")
            }
            stdoutReader.close()

            // Lê a saída de erro (STDERR)
            val stderrReader = BufferedReader(InputStreamReader(process.errorStream))
            while (stderrReader.readLine().also { line = it } != null) {
                stderr.append(line).append("\n")
            }
            stderrReader.close()

            // Aguarda a finalização do processo e obtém o código de saída
            exitCode = process.waitFor()

        } catch (e: Exception) {
            Log.e(TAG, "Falha na execução do shell.", e)
            stderr.append("Exceção na execução: ${e.message}")
        } finally {
            process?.destroy()
        }

        val success = exitCode == 0
        
        if (!success) {
            Log.e(TAG, "Comandos falharam com Exit Code: $exitCode. Erro: $stderr")
        }
        
        return CommandResult(success, exitCode, stdout.toString().trim(), stderr.toString().trim())
    }
}

/**
 * @brief Estrutura de dados para o resultado da execução de um comando shell.
 */
data class CommandResult(
    val success: Boolean,
    val exitCode: Int,
    val output: String,
    val error: String
)

// ======================================================================
// 2. PackageManagerBridge.kt
// ======================================================================

/**
 * @brief Utilitário para facilitar a execução de comandos 'pm' (Package Manager)
 * via shell, frequentemente usados por um launcher.
 */
object PackageManagerBridge {
    private const val TAG = "PackageManagerBridge"

    /**
     * @brief Limpa os dados de um aplicativo (comando 'pm clear').
     * @param packageName O pacote do aplicativo.
     * @param useRoot Se true, tenta limpar com permissões de root (mais garantido).
     * @return true se o comando foi executado com sucesso (exit code 0).
     */
    fun clearAppData(packageName: String, useRoot: Boolean = false): Boolean {
        val command = "pm clear $packageName"
        val result = ShellExecutor.execute(listOf(command), useRoot)

        if (result.success) {
            Log.i(TAG, "Dados de $packageName limpos com sucesso.")
        } else {
            Log.e(TAG, "Falha ao limpar dados de $packageName. Erro: ${result.error}")
        }
        return result.success
    }

    /**
     * @brief Desinstala um aplicativo (comando 'pm uninstall').
     * @param packageName O pacote do aplicativo.
     * @param user O ID do usuário (geralmente 0 para o usuário primário).
     * @param useRoot Se true, tenta desinstalar com permissões de root.
     * @return true se o comando foi executado com sucesso.
     */
    fun uninstallApp(packageName: String, user: Int = 0, useRoot: Boolean = false): Boolean {
        val command = "pm uninstall --user $user $packageName"
        val result = ShellExecutor.execute(listOf(command), useRoot)

        if (result.success && result.output.contains("Success")) {
            Log.i(TAG, "$packageName desinstalado com sucesso.")
            return true
        } else {
            // O comando 'pm uninstall' retorna 0 mesmo que não tenha sucesso se não usar o --user 0
            // Usamos a saída de texto para confirmar.
            Log.e(TAG, "Falha ao desinstalar $packageName. Saída: ${result.output}. Erro: ${result.error}")
            return false
        }
    }
}
