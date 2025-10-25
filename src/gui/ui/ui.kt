package com.arcanos.launcher.gui.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDrag
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState

// Importamos o AppListLoader e AppModel do arquivo que criamos anteriormente
// import com.arcanos.launcher.gui.ui.AppListLoader
// import com.arcanos.launcher.gui.ui.AppModel
// ... (Assumimos que as classes AppModel e AppListLoader estão acessíveis)

// ======================================================================
// 1. COMPONENTE PRINCIPAL (LauncherScreen)
// ======================================================================

/**
 * @brief A tela principal do Launcher Arcanos.
 * Combina a Home Screen e o App Drawer deslizante.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LauncherScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    // Estado para controlar a visibilidade da Gaveta de Aplicativos (o BottomSheet)
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // Força o sheet a ser oculto ou expandido totalmente
    )

    // Estado para os aplicativos: Carregamento no início
    val appListState = remember { mutableStateListOf<AppModel>() }
    
    LaunchedEffect(key1 = Unit) {
        // Carrega a lista de aplicativos de forma assíncrona
        val apps = AppListLoader.getLauncherApps(context)
        appListState.addAll(apps.shuffled()) // Exibe em ordem aleatória (ou por nome)
    }

    // ------------------------------------------------------------------
    // ESTRUTURA BASE (Conteúdo da Home Screen)
    // ------------------------------------------------------------------
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent // O Launcher geralmente tem fundo transparente (ou wallpaper)
    ) { paddingValues ->
        
        // Simulação do Wallpaper/Fundo
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF222222)) // Fundo escuro Linux-like
                .pointerInput(Unit) {
                    // Detecta um deslize para cima para abrir a gaveta
                    detectVerticalDrag(
                        onVerticalDrag = { change, dragAmount ->
                            if (dragAmount < -50) { // Se arrastar para cima com força
                                coroutineScope.launch { sheetState.expand() }
                                change.consume()
                            }
                        }
                    )
                }
                .padding(paddingValues)
        ) {
            // 2. WIDGETS E ÍCONES DA HOME SCREEN (Simulação)
            HomeContent(modifier = Modifier.fillMaxSize())

            // 3. DOCK DE APLICATIVOS FAVORITOS (Fixa na parte inferior)
            AppDock(
                onAppIconClick = { appModel -> 
                    AppOpener.openApp(context, appModel.packageName)
                },
                // Simulação de 5 apps favoritos
                favoriteApps = appListState.take(5)
            )
        }
    }

    // ------------------------------------------------------------------
    // GAVETA DE APLICATIVOS (App Drawer) - A parte do "Linux"
    // ------------------------------------------------------------------
    if (sheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = { 
                coroutineScope.launch { sheetState.hide() }
            },
            sheetState = sheetState,
            dragHandle = { 
                BottomSheetDefaults.DragHandle() 
            },
            // Estilo minimalista e escuro
            containerColor = Color(0xFF1E1E1E), 
            contentColor = Color.White,
            modifier = Modifier.fillMaxHeight() // Ocupa a tela toda ao expandir
        ) {
            // Conteúdo da Gaveta de Aplicativos
            AppDrawerContent(appListState)
        }
    }
}

// ======================================================================
// 2. CONTEÚDO DA HOME SCREEN (Widgets, Dock, etc.)
// ======================================================================

/**
 * @brief Conteúdo da tela inicial (área acima da Dock).
 */
@Composable
private fun HomeContent(modifier: Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Simulação de um Widget de Busca (No topo)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xAA444444)) // Semi-transparente
        ) {
            Text(
                text = "Pesquisar Aplicativos ou Web...",
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(16.dp)
            )
        }
        
        // Simulação de Ícones na Home (Desktop)
        Spacer(Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Ícones fixos da área de trabalho
            DesktopIcon(label = "Pastas", icon = Icons.Default.Info)
            DesktopIcon(label = "Lixeira", icon = Icons.Default.Info)
        }
    }
}

/**
 * @brief Dock de Aplicativos (barra inferior fixa).
 */
@Composable
private fun AppDock(
    onAppIconClick: (AppModel) -> Unit,
    favoriteApps: List<AppModel>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .height(80.dp)
            .background(Color(0xBB1E1E1E)) // Barra escurecida, semi-transparente
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        favoriteApps.forEach { app ->
            AppItem(
                app = app, 
                onAppClick = onAppIconClick, 
                modifier = Modifier.weight(1f) // Distribui uniformemente
            )
        }
    }
}

// ======================================================================
// 3. CONTEÚDO DA GAVETA DE APLICATIVOS (App Drawer Content)
// ======================================================================

/**
 * @brief O conteúdo interno da Gaveta de Aplicativos (rolagem vertical e contínua).
 * Corresponde ao AppDrawer do código anterior, mas adaptado para o BottomSheet.
 */
@Composable
private fun AppDrawerContent(appList: List<AppModel>) {
    // Adiciona uma área de busca dentro da gaveta
    OutlinedTextField(
        value = "",
        onValueChange = { /* Lógica de busca */ },
        label = { Text("Filtrar Aplicativos (busca)") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )

    // Lista Vertical em Grade dos Aplicativos
    LazyVerticalGrid(
        columns = GridCells.Fixed(5), // 5 colunas estilo "grade"
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(appList, key = { it.packageName }) { app ->
            // Usamos o AppItem que simula o ícone e nome
            AppItem(
                app = app,
                onAppClick = { appModel ->
                    AppOpener.openApp(LocalContext.current, appModel.packageName)
                }
            )
        }
    }
}

// ======================================================================
// 4. ELEMENTOS REUTILIZÁVEIS (AppItem e DesktopIcon)
// ======================================================================

@Composable
private fun AppItem(
    app: AppModel,
    onAppClick: (AppModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable { onAppClick(app) }
            .padding(4.dp)
    ) {
        // Ícone do Aplicativo (Usando o ícone do AppModel real, se disponível)
        // OBS: Em Compose, seria usado um 'rememberDrawablePainter'
        Icon(
            imageVector = Icons.Default.Info, // Placeholder visual
            contentDescription = app.label,
            modifier = Modifier.size(40.dp),
            tint = if (app.isArcanosNative) Color.Cyan else MaterialTheme.colorScheme.onSurface
        )

        // Nome do Aplicativo
        Text(
            text = app.label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            maxLines = 1,
            color = Color.White
        )
    }
}

@Composable
private fun DesktopIcon(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(48.dp),
            tint = Color.White
        )
        Text(text = label, color = Color.White, fontSize = 12.sp)
    }
}
