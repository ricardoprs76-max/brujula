package com.brujula.vocacional

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/** Márgenes de toda la app: uno solo, igual a ambos lados. */
val Margin = 20.dp

// ───────────────────────── El motivo: la aguja de la brújula ─────────────────────────

/**
 * El único motivo de la app: la aguja de una brújula, un rombo de dos mitades.
 * En una caja alta apunta hacia arriba; en una caja ancha apunta a la derecha (viñetas).
 */
@Composable
fun Needle(modifier: Modifier, north: Color, south: Color = north.copy(alpha = 0.45f)) {
  Canvas(modifier) {
    val w = size.width
    val h = size.height
    val tall = h >= w
    val tip = if (tall) Offset(w / 2, 0f) else Offset(w, h / 2)
    val tail = if (tall) Offset(w / 2, h) else Offset(0f, h / 2)
    val a = if (tall) Offset(0f, h / 2) else Offset(w / 2, 0f)
    val b = if (tall) Offset(w, h / 2) else Offset(w / 2, h)
    drawPath(Path().apply { moveTo(tip.x, tip.y); lineTo(a.x, a.y); lineTo(b.x, b.y); close() }, north)
    drawPath(Path().apply { moveTo(tail.x, tail.y); lineTo(a.x, a.y); lineTo(b.x, b.y); close() }, south)
  }
}

/** La aguja acostada: forma de los botones (puntas a izquierda y derecha). */
object PointedShape : Shape {
  override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
    val d = size.height * 0.30f
    return Outline.Generic(Path().apply {
      moveTo(d, 0f); lineTo(size.width - d, 0f); lineTo(size.width, size.height / 2)
      lineTo(size.width - d, size.height); lineTo(d, size.height); lineTo(0f, size.height / 2); close()
    })
  }
}

/** Viñeta de lista: aguja pequeña apuntando al texto. */
@Composable
fun Bullet(color: Color, modifier: Modifier = Modifier) =
  Needle(modifier.size(width = 18.dp, height = 9.dp), north = color)

// ───────────────────────── Movimiento ─────────────────────────

/** Entrada escalonada: cada parte sube 20 dp y aparece, 70 ms después de la anterior. */
@Composable
fun Modifier.reveal(order: Int): Modifier {
  val reduce = LocalReduceMotion.current
  val progress = remember { Animatable(if (reduce) 1f else 0f) }
  LaunchedEffect(Unit) {
    if (!reduce) {
      delay(order * 70L)
      progress.animateTo(1f, tween(420, easing = FastOutSlowInEasing))
    }
  }
  val rise = with(LocalDensity.current) { 20.dp.toPx() }
  return graphicsLayer {
    alpha = progress.value
    translationY = (1f - progress.value) * rise
  }
}

/** Toque con respuesta: se hunde un poco al presionar. */
@Composable
fun Modifier.pressable(onClick: () -> Unit, role: Role = Role.Button): Modifier {
  val source = remember { MutableInteractionSource() }
  val pressed by source.collectIsPressedAsState()
  val scale by animateFloatAsState(if (pressed && !LocalReduceMotion.current) 0.97f else 1f, tween(120), label = "toque")
  return graphicsLayer { scaleX = scale; scaleY = scale }
    .clickable(interactionSource = source, indication = ripple(), role = role, onClick = onClick)
}

// ───────────────────────── Piezas ─────────────────────────

/** Botón con forma de aguja acostada. Dorado = la acción principal; tono de superficie = secundaria. */
@Composable
fun NeedleButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, primary: Boolean = true) {
  val bg = if (primary) P.accent else P.surfaceHigh
  val fg = if (primary) P.onAccent else P.ink
  Box(
    modifier.fillMaxWidth().height(60.dp).pressable(onClick).background(bg, PointedShape),
    contentAlignment = Alignment.Center,
  ) { Text(text, style = Type.body.copy(fontSize = 18.sp, fontWeight = FontWeight.SemiBold), color = fg) }
}

/** Título de pantalla: una línea o dos en mayúsculas grandes, y la frase que dice qué es. */
@Composable
fun ScreenTitle(title: String, sentence: String, modifier: Modifier = Modifier, color: Color = P.ink, sentenceColor: Color = P.ink) {
  Column(modifier.fillMaxWidth()) {
    Text(title.uppercase(), style = Type.display, color = color, modifier = Modifier.reveal(0))
    Spacer(Modifier.height(14.dp))
    Text(sentence, style = Type.body, color = sentenceColor, modifier = Modifier.reveal(1))
  }
}

/** Fila de lista con viñeta de aguja. */
@Composable
fun BulletLine(text: String, color: Color = P.accent, textColor: Color = P.ink) {
  Row(verticalAlignment = Alignment.Top) {
    Bullet(if (color == P.accent && !P.dark) P.ink else color, Modifier.padding(top = 9.dp))
    Spacer(Modifier.width(12.dp))
    Text(text, style = Type.body, color = textColor)
  }
}

/** El mensaje central de la app, en un campo de color entero. */
@Composable
fun HintNotVerdict(modifier: Modifier = Modifier) {
  Column(modifier.fillMaxWidth().background(P.field).padding(horizontal = Margin, vertical = 36.dp)) {
    Needle(Modifier.size(width = 14.dp, height = 36.dp), north = P.onField, south = P.onField.copy(alpha = 0.4f))
    Spacer(Modifier.height(16.dp))
    Text("Es una pista, no un veredicto", style = Type.head, color = P.onField)
    Spacer(Modifier.height(12.dp))
    Text(
      "Tus gustos van a cambiar, y está bien. Este test te da ideas para explorar; no decide por ti. " +
        "La mejor forma de saber si una carrera es para ti es hablar con personas que ya trabajan en ella " +
        "y preguntarles cómo es su día a día.",
      style = Type.body, color = P.onFieldMuted,
    )
  }
}

/** Preguntas para hacerle a alguien que ya trabaja en lo que te interesa. */
@Composable
fun AskAPro(modifier: Modifier = Modifier, count: Int = questionsForPros.size) {
  Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Text("Pregúntale a alguien que ya lo vive", style = Type.head, color = P.ink)
    Text(
      "Una tía, un vecino, el papá de una amiga… A la gente le suele gustar hablar de su trabajo. Prueba con estas preguntas:",
      style = Type.body, color = P.ink,
    )
    questionsForPros.take(count).forEach { BulletLine(it) }
  }
}

/** Barra de las fichas de detalle: solo el botón grande para volver. */
@Composable
fun BackBar(onBack: () -> Unit, tint: Color = P.ink) {
  Box(Modifier.fillMaxWidth().heightIn(min = 64.dp).padding(start = 4.dp), contentAlignment = Alignment.CenterStart) {
    IconButton(onClick = onBack, modifier = Modifier.size(56.dp)) {
      Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = tint)
    }
  }
}
