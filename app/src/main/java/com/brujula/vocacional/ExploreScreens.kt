package com.brujula.vocacional

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// ───────────────────────── Carreras ─────────────────────────

/** Carreras agrupadas por su área principal (cada una aparece una sola vez). */
private val groups = Area.entries.map { a -> a to Careers.all.filter { it.area == a } }

@Composable
fun CareersScreen(openCareer: (String) -> Unit) {
  val p = P
  val list = rememberLazyListState()
  val scope = rememberCoroutineScope()
  // Índice de la cabecera de cada área dentro de la lista (título = 0, enlaces = 1).
  val headerIndex = buildList {
    var i = 2
    groups.forEach { (_, cs) -> add(i); i += 1 + cs.size }
  }

  LazyColumn(Modifier.fillMaxSize().background(p.ground), state = list, contentPadding = PaddingValues(bottom = 40.dp)) {
    item {
      ScreenTitle(
        "Carreras",
        "Lo que imaginas frente a lo que realmente haces. Toca una carrera para ver su día a día, con lo bueno y con lo difícil.",
        Modifier.padding(horizontal = Margin).padding(top = 28.dp, bottom = 12.dp),
      )
    }
    stickyHeader {
      // Enlaces de texto para saltar a cada área.
      LazyRow(
        Modifier.fillMaxWidth().background(p.ground).padding(vertical = 6.dp),
        contentPadding = PaddingValues(horizontal = Margin - 8.dp),
      ) {
        items(groups.size) { g ->
          val area = groups[g].first
          Row(
            Modifier.heightIn(min = 48.dp).pressable({ scope.launch { list.animateScrollToItem(headerIndex[g]) } })
              .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Bullet(area.color)
            Spacer(Modifier.width(8.dp))
            Text(area.title, style = Type.body.copy(fontWeight = FontWeight.Medium), color = p.ink)
          }
        }
      }
    }
    groups.forEach { (area, careers) ->
      item(key = "h-${area.name}") {
        Row(
          Modifier.fillMaxWidth().padding(horizontal = Margin).padding(top = 44.dp, bottom = 8.dp),
          verticalAlignment = Alignment.Bottom,
        ) {
          Text(area.title, style = Type.head, color = p.ink, modifier = Modifier.weight(1f))
          Text("${careers.size}", style = Type.body, color = p.muted)
        }
      }
      items(careers, key = { it.id }) { c ->
        Row(
          Modifier.fillMaxWidth().pressable({ openCareer(c.id) }).padding(horizontal = Margin, vertical = 14.dp),
          verticalAlignment = Alignment.Top,
        ) {
          Needle(Modifier.padding(top = 3.dp).size(width = 12.dp, height = 30.dp), north = area.color)
          Spacer(Modifier.width(16.dp))
          Column(Modifier.weight(1f)) {
            Text(c.name, style = Type.body.copy(fontWeight = FontWeight.SemiBold, fontSize = 18.sp), color = p.ink)
            Text(c.demand.label, style = Type.note, color = p.muted)
          }
        }
      }
    }
    item {
      Text(
        "Hay muchas más carreras, técnicas, tecnológicas y universitarias. Esto es solo un punto de partida.",
        style = Type.note, color = p.muted,
        modifier = Modifier.padding(horizontal = Margin).padding(top = 32.dp),
      )
    }
  }
}

@Composable
fun CareerDetail(c: Career, onBack: () -> Unit) {
  val p = P
  Column(Modifier.fillMaxSize().background(p.ground).verticalScroll(rememberScrollState())) {
    BackBar(onBack)
    Column(Modifier.padding(horizontal = Margin), verticalArrangement = Arrangement.spacedBy(24.dp)) {
      // La placa: el color del área con la aguja grande, cortada por el borde.
      BoxWithConstraints(Modifier.fillMaxWidth().aspectRatio(16f / 9f).background(c.area.color).clipToBounds().reveal(0)) {
        Needle(
          Modifier.offset(x = maxWidth - maxHeight * 0.41f, y = -maxHeight * 0.39f)
            .wrapContentSize(Alignment.TopStart, unbounded = true)
            .size(width = maxHeight * 0.48f, height = maxHeight * 1.74f)
            .graphicsLayer { rotationZ = 60f },
          north = p.ground, south = p.ground.copy(alpha = 0.35f),
        )
      }
      Column(Modifier.reveal(1)) {
        Text(c.name, style = Type.head, color = p.ink)
        Spacer(Modifier.height(8.dp))
        Text(c.areas.joinToString(" · ") { it.title }, style = Type.body, color = p.muted)
      }

      // Lo que imaginas: tono suave.
      Column(Modifier.fillMaxWidth().reveal(2).background(p.surface, RoundedCornerShape(4.dp)).padding(22.dp)) {
        Text("Lo que imaginas", style = Type.lead, color = p.ink)
        Spacer(Modifier.height(8.dp))
        Text("“${c.imagine}”", style = Type.body.copy(fontSize = 18.sp, lineHeight = 27.sp), color = p.ink)
      }
      // Lo que realmente haces: el campo de contraste fuerte.
      Column(Modifier.fillMaxWidth().reveal(3).background(p.field, RoundedCornerShape(4.dp)).padding(22.dp)) {
        Text("Lo que realmente haces", style = Type.lead.copy(fontWeight = FontWeight.Bold), color = p.onField)
        Spacer(Modifier.height(8.dp))
        Text(c.reality, style = Type.body.copy(fontSize = 18.sp, lineHeight = 28.sp), color = p.onFieldMuted)
      }

      Column(Modifier.reveal(4)) {
        Text("Hacia el futuro", style = Type.head, color = p.ink)
        Spacer(Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
          Bullet(if (p.dark) p.accent else p.ink)
          Spacer(Modifier.width(10.dp))
          Text(c.demand.label, style = Type.body.copy(fontWeight = FontWeight.SemiBold), color = p.ink)
        }
        Spacer(Modifier.height(8.dp))
        Text(c.future, style = Type.body, color = p.ink)
        Spacer(Modifier.height(12.dp))
        Text(
          "Las tendencias cambian. Para datos actualizados de empleo y salarios en Colombia, busca el Observatorio Laboral para la Educación (OLE) del Ministerio de Educación.",
          style = Type.note, color = p.muted,
        )
      }
      AskAPro(count = 4)
      Spacer(Modifier.height(16.dp))
    }
  }
}

// ───────────────────────── Perfiles del futuro ─────────────────────────

@Composable
fun FutureScreen() {
  val p = P
  var open by rememberSaveable { mutableStateOf<String?>(null) }

  LazyColumn(
    Modifier.fillMaxSize().background(p.ground),
    contentPadding = PaddingValues(start = Margin, end = Margin, top = 28.dp, bottom = 40.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    item {
      ScreenTitle(
        "Perfiles del futuro",
        "Trabajos que se van a necesitar cada vez más. Muchos no tienen una carrera con ese nombre exacto: se llega desde varias. Toca uno para abrirlo.",
        Modifier.padding(bottom = 20.dp),
      )
    }
    items(Future.all, key = { it.id }) { prof ->
      ProfileTile(prof, open == prof.id) { open = if (open == prof.id) null else prof.id }
    }
    item {
      Text(
        "Las habilidades se aprenden: nadie llega sabiéndolo todo. Lo importante es la curiosidad y practicar.",
        style = Type.note, color = p.muted, modifier = Modifier.padding(top = 20.dp),
      )
    }
  }
}

/** Un perfil como baldosa: cerrada en tono de superficie; abierta, en dorado con todo su contenido. */
@Composable
private fun ProfileTile(prof: Profile, isOpen: Boolean, onToggle: () -> Unit) {
  val p = P
  val bg by animateColorAsState(if (isOpen) p.accent else p.surface, tween(220), label = "baldosa")
  val ink = if (isOpen) p.onAccent else p.ink
  val turn by animateFloatAsState(if (isOpen) 90f else 0f, tween(220), label = "más")

  Column(
    Modifier.fillMaxWidth()
      .background(bg, RoundedCornerShape(4.dp))
      .animateContentSize(tween(260)),
  ) {
    Row(
      Modifier.fillMaxWidth().heightIn(min = 72.dp)
        .pressable(onToggle)
        .semantics { stateDescription = if (isOpen) "abierto" else "cerrado" }
        .padding(horizontal = 22.dp, vertical = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Column(Modifier.weight(1f)) {
        Text(prof.name, style = Type.lead, color = ink)
        if (!isOpen) Text(prof.tagline, style = Type.note, color = p.muted)
      }
      Spacer(Modifier.width(16.dp))
      // Más / menos hecho con dos barras: la vertical gira y se acuesta.
      Box(Modifier.size(16.dp), contentAlignment = Alignment.Center) {
        Box(Modifier.size(width = 16.dp, height = 3.dp).background(ink))
        Box(Modifier.size(width = 3.dp, height = 16.dp).graphicsLayer { rotationZ = turn }.background(ink))
      }
    }
    if (isOpen) {
      Column(Modifier.padding(start = 22.dp, end = 22.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(prof.tagline, style = Type.body.copy(fontWeight = FontWeight.Medium), color = ink)
        Part("Qué hace en la práctica", ink)
        prof.practice.forEach { BulletLine(it, color = ink, textColor = ink) }
        Part("Lo menos glamuroso", ink)
        Text(prof.hard, style = Type.body, color = ink)
        Part("Habilidades que necesita", ink)
        Text(prof.skills.joinToString(" · "), style = Type.body, color = ink)
        Part("Caminos para llegar", ink)
        Text(prof.paths, style = Type.body, color = ink)
      }
    }
  }
}

@Composable
private fun Part(title: String, ink: Color) {
  Text(title, style = Type.body.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp), color = ink, modifier = Modifier.padding(top = 6.dp))
}
