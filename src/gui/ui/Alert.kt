// src/gui/ui/Alert.kt
package com.arcanos.launcher.gui.ui

import android.content.Context
import arcanos.ui.Toast
import arcanos.ui.Dialog
import arcanos.util.Log

/**
 * @brief Utilitario Singleton para exibir notificacoes (toasts) e alertas 
 * (dialogos) na interface do usuario.
 *
 * NOTA: Esta classe é uma SIMULACAO. Em um projeto Android real, ela
 * usaria Contexts, FragmentManagers ou um sistema de Composables.
 */
object Alert {
    
    /**
     * @brief Exibe uma mensagem rapida na tela (Toast).
     * @param context O Context necessario para mostrar a UI.
     * @param message A string da mensagem a ser exibida.
     */
    fun showToast(context: Context, message: String) {
        // Simula a chamada a um componente de UI
        Toast.makeToast(context, message).show()
        Log.i("Alert", "TOAST: $message [via Context: ${context.javaClass.simpleName}]")
    }

    /**
     * @brief Exibe um dialogo de alerta com um titulo e uma mensagem.
     * @param context O Context necessario para mostrar a UI.
     * @param title O titulo do alerta.
     * @param message A mensagem detalhada do alerta.
     */
    fun showAlertDialog(context: Context, title: String, message: String) {
        // Simula a criacao e exibicao de um Dialog
        val dialog = Dialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .create()
        
        dialog.show()
        Log.e("Alert", "ALERTA EXIBIDO: $title - $message")
    }
}

// ======================================================================
// SIMULAÇÕES DE CLASSES DE FRAMEWORK NECESSARIAS (Em um mundo de mock)
// ======================================================================
// Nota: Em um projeto real, 'arcanos.ui.*' e 'arcanos.util.*' seriam 
// 'android.widget.Toast', 'android.app.AlertDialog', e 'android.util.Log'.

// Simula a classe Context (Apenas para fins de compilação/demonstração)
abstract class Context { 
    // Mocks de funcoes do Context
}

// Simula a classe Toast do Android
object Toast {
    fun makeToast(context: Context, text: String): ToastInstance = ToastInstance(text)
    class ToastInstance(val text: String) {
        fun show() { /* Simula a exibicao do toast */ }
    }
}

// Simula a classe Dialog do Android
object Dialog {
    class Builder(context: Context) { // O Construtor precisa do Context
        private var title: String = ""
        private var message: String = ""
        private var positiveAction: (() -> Unit)? = null

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }
        
        fun setMessage(message: String): Builder {
            this.message = message
            return this
        }
        
        fun setPositiveButton(text: String, action: (() -> Unit)?): Builder {
            this.positiveAction = action
            return this
        }

        fun create(): DialogInstance {
            return DialogInstance(title, message, positiveAction)
        }
    }

    class DialogInstance(
        val title: String, 
        val message: String, 
        val positiveAction: (() -> Unit)?
    ) {
        fun show() { /* Simula a exibicao do dialogo */ }
    }
}

// Simula a classe de log
object Log {
    fun i(tag: String, message: String) { println("I/$tag: $message") }
    fun e(tag: String, message: String) { println("E/$tag: $message") }
    fun w(tag: String, message: String) { println("W/$tag: $message") }
}
