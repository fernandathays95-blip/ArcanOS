// src/gui/ui/Alert.kt
package com.arcanos.launcher.util

import arcanos.ui.Toast
import arcanos.ui.Dialog
import arcanos.util.Log

/**
 * @brief Utilitario Singleton para exibir notificacoes (toasts) e alertas 
 * (dialogos) na interface do usuario.
 * * NOTA: Esta classe é uma SIMULACAO. Em um projeto Android real, ela
 * usaria Contexts, FragmentManagers ou um sistema de Composables.
 */
object Alert {
    
    /**
     * @brief Exibe uma mensagem rapida na tela (Toast).
     * @param message A string da mensagem a ser exibida.
     */
    fun showToast(message: String) {
        // Simula a chamada a um componente de UI
        Toast.makeToast(message).show()
        Log.i("Alert", "TOAST: $message")
    }

    /**
     * @brief Exibe um dialogo de alerta com um titulo e uma mensagem.
     * @param title O titulo do alerta.
     * @param message A mensagem detalhada do alerta.
     */
    fun showAlertDialog(title: String, message: String) {
        // Simula a criacao e exibicao de um Dialog
        val dialog = Dialog.Builder()
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .create()
        
        dialog.show()
        Log.e("Alert", "ALERTA EXIBIDO: $title - $message")
    }
}

// ======================================================================
// SIMULAÇÕES DE CLASSES DE FRAMEWORK NECESSARIAS
// ======================================================================

// Simula a classe Toast do Android
object Toast {
    fun makeToast(text: String): ToastInstance = ToastInstance(text)
    class ToastInstance(val text: String) {
        fun show() { /* Simula a exibicao do toast */ }
    }
}

// Simula a classe Dialog do Android
object Dialog {
    class Builder {
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
}

