package com.arcanos.launcher.adb_connection

import arcanos.util.Log
import arcanos.util.Singleton
import com.arcanos.launcher.adb.ShellExecutor
import com.arcanos.launcher.adb.CommandResult
import com.arcanos.launcher.adb.debug.SystemDiagnostics // Para verificar propriedades do sistema

/**
 * @brief Gerenciador para controlar o serviço Android Debug Bridge (ADB) diretamente no dispositivo.
 * Permite ligar/desligar a depuração, e alternar entre modos USB e TCP/IP.
 */
@Singleton
object AdbConnectionManager {
    private const val TAG = "AdbConnManager"

    // Propriedade do sistema que controla a porta ADB sobre TCP (padrão 5555)
    private const val ADB_TCP_PROP = "service.adb.tcp.port"
    private const val ADBD_SERVICE = "adbd"

    /**
     * @brief Ativa a depuração ADB sobre TCP/IP (Wi-Fi) na porta especificada.
     * Requer permissão de root ou privilégios de plataforma.
     *
     * @param port A porta TCP a ser usada (5555 é o padrão). Use -1 para voltar ao modo USB.
     * @return true se o comando de configuração for enviado com sucesso.
     */
    fun setAdbTcpPort(port: Int): Boolean {
        val portValue = if (port < 0) "" else port.toString()
        
        // 1. Comando para definir a propriedade do sistema para a porta
        // setprop <key> <value>
        val setPropCommand = "setprop $ADB_TCP_PROP $portValue"
        
        // 2. Comando para reiniciar o daemon adbd para aplicar a mudança
        // stop/start são comandos shell de baixo nível para serviços
        val restartAdbdCommands = listOf(
            setPropCommand,
            "stop $ADBD_SERVICE",
            "start $ADBD_SERVICE"
        )
        
        Log.w(TAG, "Tentando alternar o modo ADB. Porta alvo: $port")
        
        // setprop e reiniciar serviços requerem root ou privilégios de plataforma
        val result = ShellExecutor.execute(restartAdbdCommands, useRoot = true)

        if (result.success) {
            Log.i(TAG, "Modo ADB alternado com sucesso. Verifique se o daemon iniciou.")
            return true
        } else {
            Log.e(TAG, "Falha ao alternar o modo ADB. Erro: ${result.error}")
            return false
        }
    }

    /**
     * @brief Alterna o ADB de volta para o modo USB (chamando setAdbTcpPort com -1).
     * @return true se o comando foi enviado com sucesso.
     */
    fun switchToUsbMode(): Boolean {
        return setAdbTcpPort(-1)
    }

    /**
     * @brief Verifica o estado atual da porta TCP/IP do ADB.
     * Requer o SystemDiagnostics para obter propriedades.
     * @return A porta configurada, ou -1 se estiver no modo USB (propriedade vazia).
     */
    fun getCurrentAdbPort(): Int {
        // Usa o comando getprop, que pode ser executado via SystemDiagnostics (que usa ShellExecutor)
        val result = SystemDiagnostics.getDumpSystemService("getprop $ADB_TCP_PROP", useRoot = false)

        return try {
            val portString = result.trim()
            if (portString.isBlank() || portString == "''") {
                -1 // Propriedade vazia, então está no modo USB ou desativado
            } else {
                portString.toInt()
            }
        } catch (e: NumberFormatException) {
            Log.e(TAG, "Valor inválido para $ADB_TCP_PROP: $result", e)
            -2 // Código de erro interno para valor inválido
        }
    }
}
