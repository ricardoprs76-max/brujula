package com.brujula.vocacional

import android.provider.Settings
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

/**
 * Colores de Brújula: el verde botella y el dorado de una brújula de latón sobre un mapa.
 * Un fondo, una tinta, un acento (el dorado, en la acción y en el motivo de la aguja) y un "campo"
 * de contraste para los bloques que deben destacar.
 */
@Immutable
data class Palette(
  val ground: Color,      // fondo de la pantalla
  val surface: Color,     // un paso de tono sobre el fondo (bloques, opciones)
  val surfaceHigh: Color, // dos pasos
  val ink: Color,         // todo el texto que se lee
  val muted: Color,       // notas secundarias (contraste ≥ 4.5:1)
  val accent: Color,      // dorado: botón principal, aguja, opción elegida
  val onAccent: Color,
  val field: Color,       // campo de contraste fuerte (un bloque por pantalla, como mucho dos)
  val onField: Color,
  val onFieldMuted: Color,
  val dark: Boolean,
)

private val LightPalette = Palette(
  ground = Color(0xFFF7F2E4),
  surface = Color(0xFFECE5D0),
  surfaceHigh = Color(0xFFE1D8BE),
  ink = Color(0xFF10301D),
  muted = Color(0xFF4C5A4E),
  accent = Color(0xFFF2CC55),
  onAccent = Color(0xFF10301D),
  field = Color(0xFF133621),
  onField = Color(0xFFF4D96C),
  onFieldMuted = Color(0xFFE6DDB5),
  dark = false,
)

private val DarkPalette = Palette(
  ground = Color(0xFF133621),
  surface = Color(0xFF1B4529),
  surfaceHigh = Color(0xFF235233),
  ink = Color(0xFFF2E8C4),
  muted = Color(0xFFBDB283),
  accent = Color(0xFFF4D96C),
  onAccent = Color(0xFF133621),
  field = Color(0xFFF4D96C),
  onField = Color(0xFF133621),
  onFieldMuted = Color(0xFF2E4A2F),
  dark = true,
)

val LocalPalette = staticCompositionLocalOf { LightPalette }
val LocalReduceMotion = staticCompositionLocalOf { false }

/** Atajo: `P.ink`, `P.accent`… */
val P: Palette @Composable @ReadOnlyComposable get() = LocalPalette.current

/** Jost, guardada dentro de la app (funciona sin internet). Una sola familia para todo. */
private fun jost(weight: Int) = Font(
  R.font.jost, FontWeight(weight),
  variationSettings = FontVariation.Settings(FontVariation.weight(weight)),
)

val Jost = FontFamily(jost(400), jost(500), jost(600), jost(700))

/**
 * Cinco tamaños y nada más: display (aguja grande), cabecera, entrada, texto y nota.
 * Display en mayúsculas, apretado; el texto nunca baja de 16 sp.
 */
object Type {
  val display = TextStyle(fontFamily = Jost, fontWeight = FontWeight.Bold, fontSize = 46.sp, lineHeight = 44.sp, letterSpacing = (-0.03).em)
  val head = TextStyle(fontFamily = Jost, fontWeight = FontWeight.Bold, fontSize = 30.sp, lineHeight = 33.sp, letterSpacing = (-0.02).em)
  val lead = TextStyle(fontFamily = Jost, fontWeight = FontWeight.Medium, fontSize = 22.sp, lineHeight = 29.sp)
  val body = TextStyle(fontFamily = Jost, fontWeight = FontWeight.Normal, fontSize = 17.sp, lineHeight = 26.sp)
  val note = TextStyle(fontFamily = Jost, fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 22.sp)
}

@Composable
fun BrujulaTheme(content: @Composable () -> Unit) {
  val p = if (isSystemInDarkTheme()) DarkPalette else LightPalette
  val context = LocalContext.current
  // Si la persona apagó las animaciones del sistema, la app tampoco mueve nada.
  val reduce = remember {
    Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f
  }
  val scheme = if (p.dark) {
    darkColorScheme(
      primary = p.accent, onPrimary = p.onAccent, background = p.ground, onBackground = p.ink,
      surface = p.ground, onSurface = p.ink, onSurfaceVariant = p.muted, surfaceContainer = p.surface,
      secondaryContainer = p.surfaceHigh, onSecondaryContainer = p.ink, outline = p.muted,
    )
  } else {
    lightColorScheme(
      primary = p.ink, onPrimary = p.ground, background = p.ground, onBackground = p.ink,
      surface = p.ground, onSurface = p.ink, onSurfaceVariant = p.muted, surfaceContainer = p.surface,
      secondaryContainer = p.surfaceHigh, onSecondaryContainer = p.ink, outline = p.muted,
    )
  }
  val base = Typography()
  val typography = Typography(
    displaySmall = Type.display, headlineMedium = Type.head, headlineSmall = Type.head,
    titleLarge = Type.lead, titleMedium = Type.body.copy(fontWeight = FontWeight.Medium),
    bodyLarge = Type.body, bodyMedium = Type.body, bodySmall = Type.note,
    labelLarge = base.labelLarge.copy(fontFamily = Jost, fontSize = 15.sp, fontWeight = FontWeight.Medium),
    labelMedium = base.labelMedium.copy(fontFamily = Jost, fontSize = 14.sp, fontWeight = FontWeight.Medium),
    labelSmall = base.labelSmall.copy(fontFamily = Jost, fontSize = 13.sp),
  )
  CompositionLocalProvider(LocalPalette provides p, LocalReduceMotion provides reduce) {
    MaterialTheme(colorScheme = scheme, typography = typography, content = content)
  }
}

/** Color de un área ajustado al modo claro u oscuro (es un color de datos, no de adorno). */
val Area.color: Color @Composable @ReadOnlyComposable get() = if (LocalPalette.current.dark) dark else light
