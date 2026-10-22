package com.brujula.vocacional

import androidx.compose.ui.graphics.Color

/** Las seis áreas que puntúa el test. Cada una tiene un color claro y uno para el modo oscuro. */
enum class Area(
  val title: String,
  val blurb: String,
  val light: Color,
  val dark: Color,
) {
  TECH("Tecnología y datos",
    "Te llama entender cómo funcionan las cosas por dentro, resolver acertijos y construir herramientas.",
    Color(0xFF2F64B0), Color(0xFF9EC1F2)),
  SALUD("Salud y bienestar",
    "Te mueve cuidar a otras personas y ver que alguien está mejor gracias a ti.",
    Color(0xFF1E7F62), Color(0xFF8DDBBE)),
  CREA("Creatividad y diseño",
    "Necesitas expresarte: crear, imaginar cómo se ve algo y hacerlo realidad.",
    Color(0xFFB8336F), Color(0xFFF5A7C8)),
  SOC("Personas y sociedad",
    "Te interesa por qué la gente piensa y actúa como actúa, y cómo vivir mejor juntos.",
    Color(0xFFC0601C), Color(0xFFF6B47C)),
  NEG("Negocios y liderazgo",
    "Te gusta organizar, decidir, convencer y lograr que las ideas se vuelvan proyectos.",
    Color(0xFFA8322E), Color(0xFFF49C94)),
  CIEN("Ciencia y ambiente",
    "La curiosidad te gana: quieres saber el porqué de todo y cuidar el planeta.",
    Color(0xFF4F7F1E), Color(0xFFB9DC8C)),
}

/** Una respuesta suma 2 puntos a su área principal y, a veces, 1 punto a una segunda área. */
class Option(val text: String, val area: Area, val extra: Area? = null)

class Question(val text: String, val options: List<Option>)

object Test {
  /** Máximo de puntos posibles por área: 2 en cada pregunta. */
  const val MAX_POINTS = 20

  val questions = listOf(
    Question("Es sábado y no tienes planes. ¿Qué te provoca hacer?", listOf(
      Option("Trastear una app, un juego o aprender un truco nuevo en el computador", Area.TECH),
      Option("Entrenar, cocinar algo rico y sano o acompañar a alguien que lo necesita", Area.SALUD),
      Option("Dibujar, editar un video, armar un outfit o redecorar tu cuarto", Area.CREA),
      Option("Ver a tus amigas y terminar hablando de la vida por horas", Area.SOC),
      Option("Montar un mini negocio: pulseras, postres, lo que se venda", Area.NEG),
      Option("Ver un documental sobre animales, el espacio o el cuerpo humano", Area.CIEN),
    )),
    Question("En un trabajo en grupo, tú casi siempre…", listOf(
      Option("Organizas el plan y repartes las tareas", Area.NEG, Area.SOC),
      Option("Haces que la presentación quede hermosa", Area.CREA),
      Option("Buscas los datos y revisas que todo cuadre", Area.TECH, Area.CIEN),
      Option("Te aseguras de que nadie quede por fuera y de que no haya peleas", Area.SOC, Area.SALUD),
      Option("Investigas el tema a fondo, más de lo que pidieron", Area.CIEN),
      Option("Cuidas al grupo: traes snacks y avisas cuándo hace falta una pausa", Area.SALUD),
    )),
    Question("¿En qué clase se te pasa el tiempo volando?", listOf(
      Option("Matemáticas o informática", Area.TECH),
      Option("Biología o química", Area.CIEN, Area.SALUD),
      Option("Artes, música o diseño", Area.CREA),
      Option("Sociales, filosofía o lenguaje", Area.SOC),
      Option("Educación física", Area.SALUD),
      Option("Ninguna en especial: me encanta cuando hacemos la feria del colegio y vendemos cosas", Area.NEG),
    )),
    Question("Una amiga te escribe a las 11 p. m. con un problema. ¿Qué haces?", listOf(
      Option("La escucho y le ayudo a ver la situación desde otro lado", Area.SOC, Area.SALUD),
      Option("Le armo un plan con pasos concretos para resolverlo", Area.NEG, Area.TECH),
      Option("Si es sobre cómo se siente o su salud, le propongo buscar ayuda juntas", Area.SALUD),
      Option("Le mando un dibujo o un meme hecho por mí para subirle el ánimo", Area.CREA),
      Option("Busco información confiable sobre lo que le pasa", Area.CIEN),
      Option("Si le hackearon una cuenta o se le dañó el celular, ¡soy su persona!", Area.TECH),
    )),
    Question("¿Qué video te quedarías viendo hasta el final?", listOf(
      Option("Cómo se hizo un videojuego o cómo funciona la inteligencia artificial", Area.TECH),
      Option("Un día en la vida de una médica o una fisioterapeuta", Area.SALUD),
      Option("Un antes y después de un diseño, una animación o un maquillaje artístico", Area.CREA),
      Option("La historia de alguien que cambió su barrio o su comunidad", Area.SOC),
      Option("Cómo una chica creó su propia marca desde cero", Area.NEG),
      Option("Experimentos raros o una expedición a la selva", Area.CIEN),
    )),
    Question("Si pudieras arreglar un problema del mundo, ¿cuál escogerías?", listOf(
      Option("Que internet sea seguro y nadie robe tus datos", Area.TECH),
      Option("Que todas las personas tengan salud, también salud mental", Area.SALUD),
      Option("Que los lugares y las cosas sean más bonitos y fáciles de usar", Area.CREA),
      Option("Que haya menos desigualdad y más educación para todos", Area.SOC),
      Option("Que haya más empleo y empresas que traten bien a su gente", Area.NEG),
      Option("El cambio climático y la pérdida de especies", Area.CIEN),
    )),
    Question("¿Qué frase te describe mejor?", listOf(
      Option("“Me gusta entender cómo funcionan las cosas por dentro”", Area.TECH, Area.CIEN),
      Option("“Me siento bien cuando alguien está mejor gracias a mí”", Area.SALUD, Area.SOC),
      Option("“Si no creo algo, me aburro”", Area.CREA),
      Option("“Me pregunto mucho por qué la gente hace lo que hace”", Area.SOC),
      Option("“Me gusta decidir y que las cosas pasen”", Area.NEG),
      Option("“No me conformo con la primera respuesta”", Area.CIEN),
    )),
    Question("Imagínate trabajando. ¿Dónde te sientes más a gusto?", listOf(
      Option("Con mi computador, en casa o en una oficina tranquila", Area.TECH),
      Option("En un hospital, un consultorio o un centro deportivo", Area.SALUD),
      Option("En un estudio lleno de colores, cámaras o materiales", Area.CREA),
      Option("En un colegio, una comunidad o una fundación", Area.SOC),
      Option("En reuniones, negociando y moviéndome de un lado a otro", Area.NEG),
      Option("En un laboratorio o en el campo, con botas y libreta", Area.CIEN),
    )),
    Question("¿Qué reto te emociona más?", listOf(
      Option("Crear una app que use inteligencia artificial", Area.TECH),
      Option("Ayudar a alguien a recuperarse de una lesión", Area.SALUD),
      Option("Diseñar la imagen de una marca o de un videojuego", Area.CREA),
      Option("Organizar una campaña para que los jóvenes se informen y participen", Area.SOC, Area.NEG),
      Option("Convencer a inversionistas de apostar por tu idea", Area.NEG),
      Option("Descubrir qué contamina un río y cómo limpiarlo", Area.CIEN),
    )),
    Question("Cuando algo no te sale, ¿qué te hace seguir intentando?", listOf(
      Option("El acertijo: no descanso hasta que funcione", Area.TECH),
      Option("Saber que alguien cuenta conmigo", Area.SALUD),
      Option("Que quede como lo imaginé", Area.CREA),
      Option("Que es importante para otras personas", Area.SOC),
      Option("Llegar a la meta", Area.NEG),
      Option("La curiosidad: necesito saber la respuesta", Area.CIEN),
    )),
  )

  /** Orden de opciones fijo pero mezclado por pregunta, para que las áreas no salgan siempre en el mismo lugar. */
  fun shuffled(index: Int): List<Option> = questions[index].options.shuffled(kotlin.random.Random(index * 7 + 3))

  /** Puntos por área según las respuestas (índice de opción dentro de la lista original). */
  fun score(answers: List<Int>): Map<Area, Int> {
    val points = Area.entries.associateWith { 0 }.toMutableMap()
    answers.forEachIndexed { q, o ->
      val opt = questions[q].options[o]
      points[opt.area] = points.getValue(opt.area) + 2
      opt.extra?.let { points[it] = points.getValue(it) + 1 }
    }
    return points
  }

  /** Porcentaje de afinidad (0–100) de cada área, de mayor a menor. */
  fun ranking(points: Map<Area, Int>): List<Pair<Area, Int>> =
    points.map { (a, p) -> a to (p * 100 / MAX_POINTS).coerceAtMost(100) }
      .sortedWith(compareByDescending<Pair<Area, Int>> { it.second }.thenBy { it.first.ordinal })
}

/** Preguntas para hacerle a una persona que ya trabaja en lo que te interesa. */
val questionsForPros = listOf(
  "¿Cómo es un martes normal en tu trabajo, de principio a fin?",
  "¿Qué parte de tu trabajo es repetitiva o aburrida?",
  "¿Qué fue lo más difícil de la carrera?",
  "¿Cómo son el horario y el sueldo cuando uno apenas empieza?",
  "¿Volverías a estudiar lo mismo? ¿Por qué?",
  "¿Qué te hubiera gustado saber a mis 15 años?",
)
