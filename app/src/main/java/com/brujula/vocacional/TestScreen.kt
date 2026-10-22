package com.brujula.vocacional

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/** Guarda el último resultado en el teléfono (solo las respuestas; los porcentajes se recalculan). */
private object LastResult {
  private fun prefs(c: Context) = c.getSharedPreferences("brujula", Context.MODE_PRIVATE)
  fun load(c: Context): List<Int>? = prefs(c).getString("respuestas", null)
    ?.split(',')?.mapNotNull { it.toIntOrNull() }?.takeIf { it.size == Test.questions.size }
  fun save(c: Context, answers: List<Int>) = prefs(c).edit().putString("respuestas", answers.joinToString(",")).apply()
}

private const val INTRO = -1
private val RESULT = Test.questions.size

@Composable
fun TestScreen(openCareer: (String) -> Unit, openTab: (Int) -> Unit) {
  val context = LocalContext.current
  var step by rememberSaveable { mutableIntStateOf(INTRO) }
  var answers by rememberSaveable { mutableStateOf(listOf<Int>()) }
  var last by remember { mutableStateOf(LastResult.load(context)) }
  val scope = rememberCoroutineScope()
  // Atrás del sistema: pregunta anterior; desde el resultado, vuelve al inicio del test.
  BackHandler(step != INTRO) { step = if (step == RESULT) INTRO else step - 1 }

  AnimatedContent(
    targetState = step,
    transitionSpec = {
      val dir = if (targetState > initialState) 1 else -1
      (slideInHorizontally(tween(280)) { dir * it / 4 } + fadeIn(tween(280))) togetherWith
        (slideOutHorizontally(tween(220)) { -dir * it / 4 } + fadeOut(tween(180)))
    },
    label = "test",
  ) { s ->
    when (s) {
      INTRO -> Intro(
        last = last,
        onStart = { answers = emptyList(); step = 0 },
        onSeeLast = { last?.let { answers = it; step = RESULT } },
        openTab = openTab,
      )
      RESULT -> Results(
        answers = answers,
        openCareer = openCareer,
        openCareers = { openTab(1) },
        onRepeat = { answers = emptyList(); step = 0 },
      )
      else -> QuestionPage(
        index = s,
        selected = answers.getOrNull(s),
        onPick = { option ->
          answers = answers.take(s) + option
          scope.launch {
            delay(300) // deja ver la opción marcada antes de pasar
            if (s == RESULT - 1) {
              LastResult.save(context, answers)
              last = answers
            }
            step = s + 1
          }
        },
        onBack = { step = s - 1 },
      )
    }
  }
}

// ───────────────────────── Inicio ─────────────────────────

/** Pestaña de carpeta: la mitad superior de la aguja acostada (lados inclinados). */
private object TabShape : Shape {
  override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
    val d = size.height * 0.6f
    return Outline.Generic(Path().apply {
      moveTo(0f, size.height); lineTo(d, 0f); lineTo(size.width - d, 0f); lineTo(size.width, size.height); close()
    })
  }
}

@Composable
private fun Intro(last: List<Int>?, onStart: () -> Unit, onSeeLast: () -> Unit, openTab: (Int) -> Unit) {
  val p = P
  // Arriba, siempre verde y dorado (en modo oscuro el fondo ya es verde).
  val heroBg = if (p.dark) p.ground else p.field
  val heroInk = if (p.dark) p.accent else p.onField
  val heroText = if (p.dark) p.ink else p.onFieldMuted
  val front = if (p.dark) p.surface else p.ground
  val back = if (p.dark) Color(0xFF2C6040) else Color(0xFF2B5C3B)

  Column(Modifier.fillMaxSize().background(front).verticalScroll(rememberScrollState())) {
    Column(Modifier.fillMaxWidth().background(heroBg)) {
      // La aguja enorme, cortada por el borde derecho; se mece despacio.
      BoxWithConstraints(Modifier.fillMaxWidth().height(230.dp).clipToBounds()) {
        val sway = if (LocalReduceMotion.current) 0f else {
          rememberInfiniteTransition(label = "aguja").animateFloat(
            -3f, 3f, infiniteRepeatable(tween(5200), RepeatMode.Reverse), label = "vaivén",
          ).value
        }
        Needle(
          Modifier.offset(x = maxWidth - 95.dp, y = (-90).dp)
            .wrapContentSize(Alignment.TopStart, unbounded = true)
            .size(width = 110.dp, height = 400.dp)
            .graphicsLayer { rotationZ = 60f + sway },
          north = heroInk, south = heroInk.copy(alpha = 0.28f),
        )
        Text("Brújula", style = Type.body.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp), color = heroInk,
          modifier = Modifier.padding(start = Margin, top = 20.dp))
      }
      Column(Modifier.padding(horizontal = Margin).padding(bottom = 28.dp)) {
        ScreenTitle(
          "¿Qué podrías estudiar?",
          "Descubre qué áreas van contigo, cómo es de verdad el día a día de 21 carreras y qué trabajos se van a necesitar.",
          color = heroInk, sentenceColor = heroText,
        )
      }
      // Las carpetas: cada pestaña lleva a su sección; la del frente está abierta.
      FolderTab("Futuro", Alignment.CenterEnd, heroBg, back, heroText) { openTab(2) }
      FolderTab("Carreras", Alignment.Center, back, p.accent, p.onAccent) { openTab(1) }
      FolderTab("El test", Alignment.CenterStart, p.accent, front, p.ink, onClick = null)
    }

    Column(Modifier.padding(horizontal = Margin).padding(top = 12.dp, bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
      Text(
        "10 preguntas, unos 3 minutos. No hay respuestas buenas ni malas: escoge la que más se parezca a ti, no la que creas que “deberías” escoger.",
        style = Type.lead, color = p.ink, modifier = Modifier.reveal(2),
      )
      NeedleButton(if (last == null) "Empezar el test" else "Hacer el test otra vez", onStart, Modifier.reveal(3))

      if (last != null) {
        val top = Test.ranking(Test.score(last)).take(3)
        Column(
          Modifier.fillMaxWidth().reveal(4).pressable(onSeeLast).background(p.surface, RoundedCornerShape(4.dp)).padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          Text("Tu último resultado", style = Type.lead, color = p.ink)
          top.forEach { (area, pct) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
              Bullet(area.color)
              Spacer(Modifier.width(12.dp))
              Text(area.title, style = Type.body, color = p.ink, modifier = Modifier.weight(1f))
              Text("$pct %", style = Type.body.copy(fontWeight = FontWeight.SemiBold), color = p.ink)
            }
          }
          Text("Toca para verlo completo", style = Type.note, color = p.muted)
        }
      }
    }

    HintNotVerdict()

    Column(Modifier.padding(horizontal = Margin).padding(top = 40.dp, bottom = 40.dp), verticalArrangement = Arrangement.spacedBy(22.dp)) {
      Text("Las seis áreas que explora el test", style = Type.head, color = p.ink)
      Area.entries.forEach { area ->
        Row {
          Needle(Modifier.padding(top = 4.dp).size(width = 12.dp, height = 30.dp), north = area.color)
          Spacer(Modifier.width(16.dp))
          Column {
            Text(area.title, style = Type.body.copy(fontWeight = FontWeight.SemiBold, fontSize = 18.sp), color = p.ink)
            Text(area.blurb, style = Type.body, color = p.muted)
          }
        }
      }
    }
  }
}

/** Una carpeta: su pestaña sobre el color de la carpeta de atrás y una franja de su propio color. */
@Composable
private fun FolderTab(label: String, align: Alignment, behind: Color, color: Color, ink: Color, onClick: (() -> Unit)?) {
  Column(Modifier.fillMaxWidth().background(behind)) {
    Box(Modifier.fillMaxWidth().padding(horizontal = Margin), contentAlignment = align) {
      Box(
        Modifier.widthIn(min = 132.dp).height(46.dp)
          .then(if (onClick != null) Modifier.pressable(onClick) else Modifier)
          .background(color, TabShape).padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center,
      ) { Text(label, style = Type.body.copy(fontWeight = FontWeight.SemiBold), color = ink) }
    }
    Box(Modifier.fillMaxWidth().height(14.dp).background(color))
  }
}

// ───────────────────────── Preguntas ─────────────────────────

@Composable
private fun QuestionPage(index: Int, selected: Int?, onPick: (Int) -> Unit, onBack: () -> Unit) {
  val p = P
  val question = Test.questions[index]
  val options = remember(index) { Test.shuffled(index) }

  Column(Modifier.fillMaxSize().background(p.ground).verticalScroll(rememberScrollState()).padding(horizontal = Margin, vertical = 8.dp)) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      IconButton(onClick = onBack, modifier = Modifier.offset(x = (-12).dp).size(56.dp)) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = if (index == 0) "Salir del test" else "Pregunta anterior", tint = p.ink)
      }
      Text("Pregunta ${index + 1} de ${Test.questions.size}", style = Type.note, color = p.muted)
    }
    // Progreso: diez agujas; las respondidas en tinta, la actual en verde lleno.
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
      repeat(Test.questions.size) { i ->
        val c = when {
          i < index -> p.ink.copy(alpha = 0.55f)
          i == index -> if (p.dark) p.accent else p.field
          else -> p.surfaceHigh
        }
        Needle(Modifier.size(width = 10.dp, height = 26.dp), north = c, south = c.copy(alpha = c.alpha * 0.5f))
      }
    }
    Spacer(Modifier.height(24.dp))
    Text(question.text, style = Type.lead.copy(fontSize = 24.sp, lineHeight = 31.sp), color = p.ink, modifier = Modifier.reveal(0))
    Spacer(Modifier.height(24.dp))
    options.forEachIndexed { n, option ->
      val original = question.options.indexOf(option)
      val isSelected = selected == original
      val bg by animateColorAsState(if (isSelected) p.accent else p.surface, tween(180), label = "opción")
      Row(
        Modifier.fillMaxWidth().padding(bottom = 10.dp).reveal(n + 1)
          .heightIn(min = 68.dp)
          .pressable({ onPick(original) }, Role.RadioButton)
          .background(bg, RoundedCornerShape(4.dp))
          .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        if (isSelected) {
          Bullet(p.onAccent)
          Spacer(Modifier.width(14.dp))
        }
        Text(
          option.text, style = Type.body.copy(fontSize = 18.sp, lineHeight = 26.sp),
          fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
          color = if (isSelected) p.onAccent else p.ink,
        )
      }
    }
    Spacer(Modifier.height(8.dp))
  }
}

// ───────────────────────── Resultado ─────────────────────────

private val shortNames = mapOf(
  Area.TECH to "Tecnología", Area.SALUD to "Salud", Area.CREA to "Creatividad",
  Area.SOC to "Personas", Area.NEG to "Negocios", Area.CIEN to "Ciencia",
)

@Composable
private fun Results(answers: List<Int>, openCareer: (String) -> Unit, openCareers: () -> Unit, onRepeat: () -> Unit) {
  val p = P
  val ranking = remember(answers) { Test.ranking(Test.score(answers)) }
  var chosen by rememberSaveable { mutableStateOf(ranking.first().first) }
  val pct = ranking.first { it.first == chosen }.second
  val place = ranking.indexOfFirst { it.first == chosen }

  Column(Modifier.fillMaxSize().background(p.ground).verticalScroll(rememberScrollState())) {
    Column(Modifier.padding(horizontal = Margin).padding(top = 28.dp)) {
      ScreenTitle("Tus pistas", "La aguja señala el área con la que más conectaste hoy. Toca otra área para ver hacia dónde más podrías ir.")
      Spacer(Modifier.height(24.dp))
      CompassDial(chosen, onChoose = { chosen = it }, modifier = Modifier.reveal(2))
      Spacer(Modifier.height(28.dp))

      Text(
        when (place) { 0 -> "Tu área más afín"; 1 -> "Tu segunda área"; 2 -> "Tu tercera área"; else -> "Puesto ${place + 1} de 6" },
        style = Type.note, color = p.muted,
      )
      Row(verticalAlignment = Alignment.Bottom) {
        Text(chosen.title, style = Type.head, color = p.ink, modifier = Modifier.weight(1f))
        Spacer(Modifier.width(12.dp))
        Text("$pct %", style = Type.display, color = chosen.color)
      }
      Spacer(Modifier.height(10.dp))
      Text(chosen.blurb, style = Type.body, color = p.ink)
      Spacer(Modifier.height(20.dp))
      Text("Carreras para explorar", style = Type.lead, color = p.ink)
      Spacer(Modifier.height(4.dp))
      Careers.forArea(chosen).forEach { c ->
        Row(
          Modifier.fillMaxWidth().heightIn(min = 52.dp).pressable({ openCareer(c.id) }).padding(vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Bullet(c.area.color)
          Spacer(Modifier.width(14.dp))
          Text(c.name, style = Type.body.copy(fontWeight = FontWeight.Medium, fontSize = 18.sp), color = p.ink, modifier = Modifier.weight(1f))
          Text("Ver", style = Type.note, color = p.muted)
        }
      }

      Spacer(Modifier.height(36.dp))
      Text("Las seis áreas", style = Type.head, color = p.ink)
      Spacer(Modifier.height(12.dp))
      ranking.forEachIndexed { i, (area, value) -> RankRow(area, value, area == chosen, i) { chosen = area } }
      Spacer(Modifier.height(12.dp))
      Text(
        "El porcentaje muestra qué tanto elegiste respuestas de esa área: 100 % sería escogerla en todas las preguntas. Es normal tener afinidad con varias.",
        style = Type.note, color = p.muted,
      )
      Spacer(Modifier.height(36.dp))
    }

    HintNotVerdict()

    Column(Modifier.padding(horizontal = Margin).padding(top = 36.dp, bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
      AskAPro()
      Spacer(Modifier.height(16.dp))
      NeedleButton("Ver todas las carreras", openCareers)
      NeedleButton("Repetir el test", onRepeat, primary = false)
    }
  }
}

@Composable
private fun RankRow(area: Area, value: Int, selected: Boolean, order: Int, onClick: () -> Unit) {
  val p = P
  val reduce = LocalReduceMotion.current
  var target by remember { mutableFloatStateOf(if (reduce) value / 100f else 0f) }
  LaunchedEffect(value) { if (!reduce) delay(200L + order * 90L); target = value / 100f }
  val bar by animateFloatAsState(target, tween(if (reduce) 0 else 800), label = "barra")
  Column(
    Modifier.fillMaxWidth()
      .pressable(onClick, Role.Tab)
      .background(if (selected) p.surface else Color.Transparent, RoundedCornerShape(4.dp))
      .padding(horizontal = 12.dp, vertical = 12.dp),
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Text(area.title, style = Type.body.copy(fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal),
        color = p.ink, modifier = Modifier.weight(1f))
      Text("$value %", style = Type.body.copy(fontWeight = FontWeight.SemiBold), color = p.ink)
    }
    Spacer(Modifier.height(8.dp))
    Box(Modifier.fillMaxWidth().height(8.dp).background(p.surfaceHigh)) {
      Box(Modifier.fillMaxWidth(bar).fillMaxHeight().background(area.color))
    }
  }
}

/**
 * La brújula del resultado: seis áreas alrededor y una aguja que gira con un resorte hasta el área elegida.
 * Es lo único "vivo" de la app: tocar un área mueve la aguja.
 */
@Composable
private fun CompassDial(chosen: Area, onChoose: (Area) -> Unit, modifier: Modifier = Modifier) {
  val p = P
  val reduce = LocalReduceMotion.current
  val angle = remember { Animatable(if (reduce) chosen.ordinal * 60f else 0f) }
  LaunchedEffect(chosen) {
    // Gira por el camino más corto.
    var delta = (chosen.ordinal * 60f - angle.value) % 360f
    if (delta > 180f) delta -= 360f
    if (delta < -180f) delta += 360f
    if (reduce) angle.snapTo(angle.value + delta)
    else {
      if (angle.value == 0f) delay(350)
      angle.animateTo(angle.value + delta, spring(dampingRatio = 0.42f, stiffness = Spring.StiffnessVeryLow))
    }
  }

  BoxWithConstraints(modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
    val side = min(maxWidth.value, 340f).dp
    val chosenColor = chosen.color
    val dotIdle = p.surfaceHigh
    Box(Modifier.size(side), contentAlignment = Alignment.Center) {
      // Esfera y una marca por área.
      Canvas(Modifier.fillMaxSize()) {
        val r = size.minDimension / 2
        drawCircle(p.surface, r)
        Area.entries.forEach { a ->
          val rad = Math.toRadians((a.ordinal * 60 - 90).toDouble())
          val c = Offset(center.x + (r * 0.88f * cos(rad)).toFloat(), center.y + (r * 0.88f * sin(rad)).toFloat())
          drawCircle(if (a == chosen) chosenColor else dotIdle, r * 0.05f, c)
        }
      }
      // Nombres cortos, tocables.
      Area.entries.forEach { a ->
        val rad = Math.toRadians((a.ordinal * 60 - 90).toDouble())
        val rr = side.value / 2 * 0.64f
        Text(
          shortNames.getValue(a),
          style = Type.note.copy(fontWeight = if (a == chosen) FontWeight.Bold else FontWeight.Medium),
          color = p.ink, textAlign = TextAlign.Center,
          modifier = Modifier
            .offset(x = (rr * cos(rad)).toFloat().dp, y = (rr * sin(rad)).toFloat().dp)
            .pressable({ onChoose(a) }, Role.Tab)
            .padding(horizontal = 8.dp, vertical = 10.dp),
        )
      }
      // La aguja.
      Needle(
        Modifier.size(width = side * 0.11f, height = side * 0.42f).graphicsLayer { rotationZ = angle.value },
        north = chosenColor, south = p.ink.copy(alpha = 0.3f),
      )
      Box(Modifier.size(side * 0.05f).background(p.ink, CircleShape))
    }
  }
}
