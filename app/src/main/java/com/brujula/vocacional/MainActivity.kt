package com.brujula.vocacional

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    setContent { BrujulaTheme { App() } }
  }
}

private class Tab(val label: String, val icon: ImageVector)

private val tabs = listOf(
  Tab("Test", Icons.Filled.Face),
  Tab("Carreras", Icons.AutoMirrored.Filled.List),
  Tab("Futuro", Icons.Filled.Star),
)

/**
 * Navegación: tres pestañas abajo y, encima de cualquiera, una ficha de detalle
 * ("c:<id>" para una carrera).
 */
@Composable
fun App() {
  var tab by rememberSaveable { mutableIntStateOf(0) }
  var detail by rememberSaveable { mutableStateOf<String?>(null) }
  val saved = rememberSaveableStateHolder()

  BackHandler(detail != null) { detail = null }
  BackHandler(detail == null && tab != 0) { tab = 0 }

  Scaffold(
    containerColor = P.ground,
    bottomBar = {
      NavigationBar(containerColor = P.surface, tonalElevation = 0.dp) {
        tabs.forEachIndexed { i, t ->
          NavigationBarItem(
            selected = tab == i,
            onClick = { tab = i; detail = null },
            icon = { Icon(t.icon, contentDescription = null) },
            label = { Text(t.label, style = Type.note.copy(fontWeight = if (tab == i) FontWeight.Bold else FontWeight.Medium)) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = P.ink, selectedTextColor = P.ink, indicatorColor = P.surfaceHigh,
              unselectedIconColor = P.muted, unselectedTextColor = P.muted,
            ),
          )
        }
      }
    },
  ) { pad ->
    Box(Modifier.fillMaxSize().padding(pad).consumeWindowInsets(pad)) {
      AnimatedContent(
        targetState = Screen(tab, detail),
        transitionSpec = {
          val forward = targetState.detail != null && initialState.detail == null
          val back = targetState.detail == null && initialState.detail != null
          when {
            forward -> (slideInHorizontally(tween(300)) { it / 3 } + fadeIn(tween(300))) togetherWith fadeOut(tween(150))
            back -> fadeIn(tween(250)) togetherWith (slideOutHorizontally(tween(250)) { it / 3 } + fadeOut(tween(200)))
            else -> fadeIn(tween(220)) togetherWith fadeOut(tween(120))
          }
        },
        label = "pantalla",
      ) { s ->
        saved.SaveableStateProvider(s.detail ?: "tab${s.tab}") {
          val d = s.detail
          when {
            d != null && d.startsWith("c:") -> Careers.byId(d.drop(2))?.let { CareerDetail(it, onBack = { detail = null }) }
            s.tab == 0 -> TestScreen(openCareer = { detail = "c:$it" }, openTab = { tab = it })
            s.tab == 1 -> CareersScreen(openCareer = { detail = "c:$it" })
            else -> FutureScreen()
          }
        }
      }
    }
  }
}

private data class Screen(val tab: Int, val detail: String?)
