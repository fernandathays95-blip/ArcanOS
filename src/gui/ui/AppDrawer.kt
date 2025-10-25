package com.arcanos.launcher.gui.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.arcanos.launcher.data.AppModel
import com.arcanos.launcher.data.AppListLoader // Simulação de utilitário
import com.arcanos.launcher.gui.ui.AppOpener // Importa a função de abertura

/**
 * @brief Componente de UI (Jetpack Compose) para o App Drawer (Gaveta de Aplicativos).
 * Ele exibe todos os aplicativos em uma grade vertical e contínua.
 *
 * Ele integra a lógica de:
 * 1. Carregamento da lista de aplicativos (`AppListLoader`).
 * 2. Lançamento seguro de aplicativos (`AppOpener.openApp`).
 * 3. Verificação de segurança (`Antivirus.checkAndBlockIfMalicious`).
 */
@Composable
fun AppDrawer(
    modifier: Modifier = Modifier,
    // Opcionalmente, você pode passar um estado para controlar a visibilidade da gaveta
) {
    val context = LocalContext.current
    
    // 1. Carregamento Assíncrono da Lista de Aplicativos
    // Normalmente, o carregamento de apps seria feito em um ViewModel e exposto como State.
    // Aqui, simulamos o carregamento direto:
    val appListState = remember { mutableStateListOf<AppModel>() }
    
    LaunchedEffect(key1 = Unit) {
        // Carrega a lista de aplicativos uma vez.
        appListState.addAll(AppListLoader.getLauncherApps(context))
    }

    // 2. Layout da Gaveta de Aplicativos (Grade Vertical e Rolável)
    LazyVerticalGrid(
        columns = GridCells.Fixed(5), // 5 colunas para ícones
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp) // Espaço para a área de deslizar
    ) {
        items(appListState, key = { it.packageName }) { app ->
            AppItem(
                app = app,
                onAppClick = { appModel ->
                    // Lógica de abertura segura: Antivírus + Try/Catch
                    AppOpener.openApp(context, appModel.packageName)
                }
            )
        }
    }
}

/**
 * @brief Componente Composable para um único ícone de aplicativo.
 */
@Composable
private fun AppItem(
    app: AppModel,
    onAppClick: (AppModel) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAppClick(app) }
            .padding(4.dp)
    ) {
        // Ícone do Aplicativo (SIMULAÇÃO: em um App Drawer real, você usaria 
        // a biblioteca Coil/Glide para carregar o Drawable do PackageManager)
        Box(
            modifier = Modifier
                .size(56.dp)
                .padding(bottom = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            // Em uma implementação real, o ícone seria carregado aqui.
            // Text(text = "App", fontSize = 10.sp) 
            // Usamos um Icon simulado.
            Icon(
                // Placeholder, substitua pelo carregamento do ícone real: 
                // painter = rememberDrawablePainter(app.iconDrawable),
                imageVector = Icons.Default.Favorite, // Placeholder visual
                contentDescription = app.label,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        // Nome do Aplicativo
        Text(
            text = app.label,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1
        )
    }
}


// ======================================================================
// SIMULAÇÕES DE CLASSES DE FRAMEWORK E UTILITÁRIOS NECESSÁRIOS
// ======================================================================

data class AppModel(
    val label: String,
    val packageName: String,
    // val iconDrawable: Drawable? // Em um projeto real, você precisaria disso
)

object AppListLoader {
    /**
     * @brief Simula o carregamento dos aplicativos do PackageManager.
     */
    fun getLauncherApps(context: Context): List<AppModel> {
        // Em um projeto real, isso queryIntentActivities(Intent.CATEGORY_LAUNCHER)
        // para obter a lista real.
        return listOf(
            AppModel("Configurações", "com.android.settings"),
            AppModel("Câmera", "com.android.camera"),
            AppModel("Telefone", "com.android.dialer"),
            AppModel("Mensagens", "com.android.messaging"),
            AppModel("Galeria", "com.android.gallery"),
            AppModel("Malware Miner", "com.malware.miner"), // Para teste do Antivirus
            AppModel("Navegador", "com.android.browser"),
            AppModel("Calculadora", "com.android.calculator"),
            AppModel("Relógio", "com.android.deskclock"),
            AppModel("Email", "com.android.email"),
            AppModel("Mapas", "com.google.android.apps.maps"),
            AppModel("Play Store", "com.android.vending"),
            AppModel("YouTube", "com.google.android.youtube"),
            AppModel("Launcher Arcanos", context.packageName), // O próprio launcher
            // ... mais aplicativos
        ).shuffled() // Embaralha para parecer mais natural
    }
}

// O AppOpener.openApp deve ser definido em outro arquivo
object AppOpener {
    fun openApp(context: Context, packageName: String) {
        // Implementação real usaria AppOpener.kt (com segurança e Antivirus)
        // Simulando a integração:
        if (Antivirus.checkAndBlockIfMalicious(context, packageName)) {
            val intent: Intent? = context.packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                 try {
                     context.startActivity(intent)
                 } catch (e: Exception) {
                      // ... tratativa de erro (usando AppCodeErrors.handleError)
                 }
            } else {
                 // ... abrir na Play Store
            }
        }
    }
}
