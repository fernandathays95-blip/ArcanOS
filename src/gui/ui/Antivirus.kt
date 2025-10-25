package com.arcanos.launcher.gui.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import arcanos.util.Log
import arcanos.util.Singleton
import com.arcanos.launcher.gui.ui.Alert
import com.arcanos.launcher.gui.ui.AppCodeErrors

/**
 * @brief Singleton que implementa a lógica de "Antivirus Rápido" para um launcher.
 * * Ele é responsável por verificar um aplicativo antes de seu lançamento.
 * Por restrições de segurança do Android (sem ROOT), ele NÃO pode encerrar
 * um aplicativo em primeiro plano. Em vez disso, ele BLOQUEIA o lançamento
 * e notifica o usuário, oferecendo a opção de ir para a tela de desinstalação.
 */
@Singleton
object Antivirus {

    private const val TAG = "Antivirus"

    // 1. Banco de dados de pacotes maliciosos (SIMULAÇÃO DE DADOS REAIS)
    private val MALICIOUS_APPS = setOf(
        "com.malware.miner",
        "com.risco.spyware",
        "com.adware.intrusivo",
        "com.example.suspect" // Exemplo de aplicativo malicioso
    )
    
    /**
     * @brief Ponto principal de verificação. Deve ser chamado antes de iniciar um aplicativo.
     * @param context O Context necessário para o manipulador de erros e alertas.
     * @param packageName O nome do pacote do aplicativo que o usuário está tentando abrir.
     * @return true se o aplicativo for seguro e puder continuar o lançamento, false se for bloqueado.
     */
    fun checkAndBlockIfMalicious(context: Context, packageName: String): Boolean {
        // Se o aplicativo for o próprio launcher, ele é seguro.
        if (packageName == context.packageName) {
            return true
        }

        Log.i(TAG, "Iniciando verificação ultrarrápida para: $packageName")
        
        if (MALICIOUS_APPS.contains(packageName)) {
            Log.e(TAG, "APLICATIVO MALICIOSO DETECTADO: $packageName. Bloqueando lançamento.")
            
            // 1. Exibir Alerta do Sistema para o Usuário
            showBlockAlert(context, packageName)

            // 2. O lançamento é BLOQUEADO.
            return false 
        }
        
        Log.i(TAG, "Aplicativo '$packageName' é seguro. Lançamento permitido.")
        return true
    }

    /**
     * @brief Exibe um alerta de bloqueio e oferece a opção de desinstalar.
     */
    private fun showBlockAlert(context: Context, packageName: String) {
        Alert.showAlertDialogWithAction(
            context = context,
            title = "⚠ Bloqueio de Segurança",
            message = "O aplicativo '$packageName' foi detectado como uma ameaça de segurança (vírus) e seu lançamento foi BLOQUEADO. Por favor, remova-o.",
            positiveButtonText = "Desinstalar",
            positiveAction = {
                // Redireciona o usuário para a tela de desinstalação
                openAppDetailsSettings(context, packageName)
            },
            negativeButtonText = "Entendido",
            negativeAction = null
        )
    }

    /**
     * @brief Abre a tela de informações do aplicativo nas Configurações, onde o usuário
     * pode desinstalar ou forçar o encerramento.
     */
    private fun openAppDetailsSettings(context: Context, packageName: String) {
        AppCodeErrors.runCatchingWithAlert(
            context = context,
            friendlyMessage = "Não foi possível abrir as configurações do aplicativo.",
            block = {
                val intent = Intent(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                        // Action para abrir a tela de detalhes de um pacote
                        android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    } else {
                        // Action alternativa para APIs mais antigas
                        Intent.ACTION_VIEW
                    }
                ).apply {
                    data = Uri.fromParts("package", packageName, null)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        )
    }
}

// ======================================================================
// SIMULAÇÕES DE CLASSES DE FRAMEWORK E UTILITÁRIOS NECESSÁRIOS
// ======================================================================

// Essas classes (Alert, AppCodeErrors, Log) são mantidas como objetos
// que devem ser implementados em outros arquivos do seu projeto.
// @Singleton
// object Alert { fun showAlertDialogWithAction(...) { ... } }
// object AppCodeErrors { fun runCatchingWithAlert(...) { ... } }
// object Log { fun i(...) { ... } fun e(...) { ... } }
// object Singleton // Simulação de anotação
