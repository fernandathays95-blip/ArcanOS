// src/gui/ui/AppName.kt
package com.arcanos.launcher.ui

// Importacoes simuladas do Jetpack Compose para criar UI declarativa
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

// Simula a classe base de UI do Compose
annotation class Composable 
class Modifier 
data class TextStyle(val fontSize: TextUnit, val color: String, val textAlign: TextAlign)
object Color {
    val White = "#FFFFFF" 
}

/**
 * @brief Componente Composable da UI para exibir o nome de um aplicativo.
 *
 * Este componente e otimizado para a exibicao de nomes curtos de aplicativos
 * em uma grade (Grid) ou lista de aplicativos, lidando com nomes longos
 * truncando-os e garantindo que o texto esteja centralizado.
 *
 * @param appName O nome do aplicativo a ser exibido.
 * @param modifier Modificador opcional para customizar o layout e aparencia.
 * @param maxLines O numero maximo de linhas que o nome pode ocupar (default: 1 ou 2).
 */
@Composable
fun AppName(
    appName: String,
    modifier: Modifier = Modifier(),
    maxLines: Int = 2
) {
    // Implementacao simulada do componente Text do Compose
    Text(
        text = appName,
        modifier = modifier,
        style = TextStyle(
            fontSize = 12.sp, // Tamanho ideal para um nome de app no launcher
            color = Color.White, 
            textAlign = TextAlign.Center 
        ),
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis // Trunca o texto com "..." se exceder o limite
    )
}

// ======================================================================
// SIMULAÇÕES DE CLASSES MINIMAIS DO JETPACK COMPOSE PARA COMPILACAO
// ======================================================================

// Simula o Composable Text do Compose
@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier(),
    style: TextStyle,
    maxLines: Int,
    overflow: TextOverflow
) {
    // Logica de renderizacao de texto real (em Compose)
    // Para esta simulacao, podemos apenas logar o que seria renderizado
    // Log.d("AppName", "Renderizando nome: $text (Max linhas: $maxLines)")
}

// Classes de utilidade do Compose
data class TextOverflow(val name: String) {
    companion object {
        val Ellipsis = TextOverflow("Ellipsis")
    }
}

data class TextAlign(val name: String) {
    companion object {
        val Center = TextAlign("Center")
    }
}

// Extensao simulada para TextUnit (para 12.sp)
val Int.sp: TextUnit 
    get() = TextUnit(this.toFloat()) 
