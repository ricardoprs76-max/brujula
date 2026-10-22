package com.brujula.vocacional

/** Qué tanto se necesitará la carrera, en palabras simples. */
enum class Demand(val label: String) {
  ALTA("Demanda alta"),
  CRECE("Demanda en crecimiento"),
  ESTABLE("Demanda estable"),
  CAMBIA("Demanda cambiante"),
  COMPETIDA("Mucha competencia"),
}

class Career(
  val id: String,
  val name: String,
  val area: Area,
  val also: List<Area> = emptyList(),
  val imagine: String,
  val reality: String,
  val demand: Demand,
  val future: String,
) {
  val areas get() = listOf(area) + also
}

object Careers {
  val all = listOf(
    // ── Tecnología y datos ──
    Career("sistemas", "Ingeniería de sistemas / software", Area.TECH,
      imagine = "Programar apps geniales toda la noche, como en las películas de hackers, y crear la próxima gran red social.",
      reality = "Pasas buena parte del día leyendo código que escribió otra persona y buscando por qué algo falla (a veces durante horas, por un detalle mínimo). Hay reuniones para entender qué quiere el cliente, documentación que escribir y pruebas que repetir. Los primeros semestres tienen cálculo, lógica y álgebra exigentes. Y nunca terminas de estudiar: las herramientas cambian cada pocos años.",
      demand = Demand.ALTA,
      future = "Sigue entre las áreas con más ofertas de empleo. La IA ya escribe parte del código sencillo, así que se valora cada vez más a quien entiende el problema completo, diseña sistemas y sabe comunicarse, no solo a quien teclea código."),
    Career("datos", "Ciencia de datos y estadística", Area.TECH, listOf(Area.CIEN),
      imagine = "Predecir el futuro con gráficas bonitas y modelos de inteligencia artificial.",
      reality = "La mayor parte del tiempo se va en limpiar datos: tablas con errores, celdas vacías, el mismo nombre escrito de diez formas. Luego toca explicar los resultados a personas que no saben estadística y, a veces, decir “los datos no muestran lo que esperabas”. Hay mucha estadística y programación, y pocos momentos de “¡eureka!”.",
      demand = Demand.ALTA,
      future = "Muy solicitada en bancos, salud, comercio, gobierno y agro. Lo repetitivo se automatiza; lo más valioso es hacer buenas preguntas y explicar los datos con honestidad."),
    Career("mecatronica", "Ingeniería mecatrónica", Area.TECH, listOf(Area.CIEN),
      imagine = "Construir robots como los de las películas.",
      reality = "Mezcla mecánica, electrónica y programación, así que estudias mucho de todo: física, cálculo, circuitos. En el trabajo es común estar en una planta industrial con casco y ruido, haciendo mantenimiento, revisando un sensor que falla o ajustando una línea de producción desde muy temprano. Los robots existen, pero suelen ser brazos que repiten la misma tarea todo el día.",
      demand = Demand.CRECE,
      future = "La automatización de fábricas, bodegas y cultivos necesita estas personas. Las oportunidades dependen mucho de la industria de cada ciudad."),

    // ── Salud y bienestar ──
    Career("medicina", "Medicina", Area.SALUD, listOf(Area.CIEN),
      imagine = "Salvar vidas en urgencias, como en Grey’s Anatomy, con la bata impecable.",
      reality = "Son unos 6 o 7 años de carrera, más un año de servicio social en muchos casos (a veces lejos de casa), y si quieres especializarte, varios años más y exámenes muy competidos. El día a día incluye turnos de noche, muchas historias clínicas en el computador, consultas cortas y trámites con las EPS. También hay momentos tristes: dar malas noticias y ver pacientes que no mejoran. Exige memoria y mucha resistencia física y emocional.",
      demand = Demand.ALTA,
      future = "Siempre se necesitan médicos, sobre todo fuera de las ciudades grandes y en medicina familiar, geriatría y salud mental. Las condiciones para recién graduados pueden ser duras: vale la pena preguntarle a médicos jóvenes."),
    Career("enfermeria", "Enfermería", Area.SALUD, listOf(Area.SOC),
      imagine = "Acompañar y consolar a los pacientes con una sonrisa.",
      reality = "Sí acompañas, pero también haces turnos de 12 horas, de noche y en festivos; das medicamentos sin margen de error; bañas, cambias y mueves pacientes; curas heridas y registras todo en formatos. Es físicamente cansado y emocionalmente intenso, y muchas veces no se reconoce como merece.",
      demand = Demand.ALTA,
      future = "Faltan profesionales de enfermería en muchos países, y a muchas enfermeras colombianas las contratan en el exterior. Con más personas mayores, la demanda va a crecer."),
    Career("fisioterapia", "Fisioterapia", Area.SALUD,
      imagine = "Trabajar con deportistas de alto rendimiento en un equipo de fútbol.",
      reality = "La mayoría trabaja en clínicas, consultorios o a domicilio, con personas mayores, pacientes con dolor de espalda o que se recuperan de una cirugía. Repites ejercicios con mucha paciencia porque el avance es lento, y llenas bastantes registros. Los cupos en equipos deportivos son pocos y muy peleados.",
      demand = Demand.CRECE,
      future = "Crece con el envejecimiento de la población. La atención en casa y la rehabilitación por videollamada son tendencias fuertes."),
    Career("psicologia", "Psicología", Area.SALUD, listOf(Area.SOC),
      imagine = "Escuchar a las personas en un consultorio acogedor y ayudarlas a sentirse mejor.",
      reality = "La consulta es solo un camino: muchas psicólogas trabajan en recursos humanos haciendo entrevistas y pruebas, en colegios o en investigación. La carrera tiene bastante estadística y metodología. Escuchar historias difíciles todo el día cansa, y tú también necesitas terapia y supervisión. Tener consulta propia toma años de formación y de construir confianza.",
      demand = Demand.CRECE,
      future = "La salud mental es cada vez más prioritaria, sobre todo en jóvenes y personas mayores. Hay muchas personas graduadas, así que especializarse en algo concreto marca la diferencia."),

    // ── Creatividad y diseño ──
    Career("grafico", "Diseño gráfico", Area.CREA, listOf(Area.NEG),
      imagine = "Pasar el día creando ilustraciones y logos con total libertad.",
      reality = "Trabajas para clientes que piden “hazme el logo más grande” o cambian de opinión cinco veces. Mucho es repetitivo: adaptar la misma pieza a veinte tamaños de redes, seguir manuales de marca, entregar a última hora. Es común trabajar por proyectos o de forma independiente, así que también toca cobrar, negociar y buscar clientes.",
      demand = Demand.CAMBIA,
      future = "La IA hace rápido las piezas sencillas, así que bajan los encargos básicos. Crecen el diseño de experiencias digitales (UX/UI), el diseño de marca con estrategia y la animación de gráficos."),
    Career("arquitectura", "Arquitectura", Area.CREA, listOf(Area.TECH),
      imagine = "Diseñar edificios increíbles que salgan en las revistas.",
      reality = "La carrera es famosa por las noches sin dormir haciendo maquetas. Ya trabajando, mucho tiempo se va en planos técnicos, normas, licencias, presupuestos, reuniones con ingenieros y visitas a obra con polvo y ruido. Diseñar “tu” edificio llega después de años, y pocas veces.",
      demand = Demand.CAMBIA,
      future = "Depende mucho de cómo vaya la construcción. Crecen la arquitectura sostenible, la renovación de edificios existentes y el modelado digital (BIM)."),
    Career("animacion", "Animación y medios digitales", Area.CREA, listOf(Area.TECH),
      imagine = "Hacer películas como las de Pixar o videos que se vuelvan virales.",
      reality = "Un segundo de animación puede tomar días. En un estudio sueles trabajar en una parte pequeña (solo la luz, o solo mover un personaje) y repetir ajustes según los comentarios de otra persona. Hay mucha competencia, contratos por proyecto y temporadas de trabajo muy intenso. Un buen portafolio pesa más que el diploma.",
      demand = Demand.COMPETIDA,
      future = "Videojuegos, publicidad, educación virtual y redes necesitan contenido, pero las herramientas cambian rápido con la IA. Quien combina creatividad con habilidades técnicas tiene más opciones."),

    // ── Personas y sociedad ──
    Career("docencia", "Licenciatura (ser profe)", Area.SOC,
      imagine = "Inspirar a los estudiantes como el profe de tu película favorita.",
      reality = "Además de dar clase, calificas montones de trabajos (muchas veces en casa, de noche), planeas, llenas formatos, hablas con padres de familia y manejas grupos de 35 o 40 estudiantes que no siempre quieren estar ahí. Hay días muy bonitos y días agotadores.",
      demand = Demand.ESTABLE,
      future = "Siempre se necesitan docentes, y faltan sobre todo en matemáticas, ciencias e inglés, y en zonas rurales. Crecen la educación virtual y la formación para adultos."),
    Career("derecho", "Derecho", Area.SOC, listOf(Area.NEG),
      imagine = "Ganar juicios con discursos brillantes, como en las series.",
      reality = "Casi todo es leer y escribir: expedientes larguísimos, contratos, leyes que cambian y plazos que no se pueden perder. Las audiencias son una parte pequeña y muchas se aplazan. Los primeros años suelen ser de mucho trabajo y sueldo modesto, y hay muchísimas personas graduadas.",
      demand = Demand.COMPETIDA,
      future = "Hay mucha competencia, pero crecen áreas nuevas: derecho digital, protección de datos, derecho ambiental y propiedad intelectual."),
    Career("social", "Trabajo social", Area.SOC, listOf(Area.SALUD),
      imagine = "Ayudar directamente a las personas y transformar comunidades.",
      reality = "Sí ayudas, pero dentro de instituciones con poco presupuesto y mucho papeleo: visitas a hogares, informes, seguimiento de casos y coordinación con entidades que tardan. Ves situaciones duras (violencia, pobreza) que no siempre puedes resolver, así que necesitas límites emocionales claros.",
      demand = Demand.ESTABLE,
      future = "Es necesaria en salud, protección de la niñez, empresas y cuidado de personas mayores. Los sueldos suelen ser modestos."),
    Career("comunicacion", "Comunicación social y periodismo", Area.SOC, listOf(Area.CREA),
      imagine = "Ser presentadora o influencer y viajar haciendo reportajes.",
      reality = "La mayoría trabaja en la comunicación de empresas o entidades: escribir boletines, manejar redes, responder mensajes, organizar eventos y medir resultados. El periodismo tiene horarios impredecibles y verificar datos toma tiempo; los medios tienen pocos puestos. Presentar en televisión es para muy pocas personas.",
      demand = Demand.COMPETIDA,
      future = "Se valoran la comunicación digital, la verificación de información contra las noticias falsas, el análisis de audiencias y explicar ciencia y salud con claridad."),

    // ── Negocios y liderazgo ──
    Career("administracion", "Administración de empresas", Area.NEG, listOf(Area.SOC),
      imagine = "Ser la jefa, tomar grandes decisiones y tener mi propia empresa.",
      reality = "Los primeros empleos suelen ser de analista o asistente: hojas de cálculo, reportes, cuadrar cifras, seguir procesos y responder correos. Dirigir llega con los años y significa resolver conflictos entre personas, a veces despedir a alguien y responder cuando las metas no se cumplen. Emprender es posible, pero muchos negocios cierran antes de despegar.",
      demand = Demand.COMPETIDA,
      future = "Es amplia y abre muchas puertas, pero hay muchas personas graduadas. Destaca quien suma análisis de datos, idiomas, finanzas o sostenibilidad."),
    Career("mercadeo", "Mercadeo digital", Area.NEG, listOf(Area.CREA),
      imagine = "Crear campañas virales y trabajar con influencers.",
      reality = "Gran parte es medir: revisar tableros llenos de números, ajustar anuncios, probar dos versiones de un texto para ver cuál funciona y hacer reportes cada semana. Las metas de ventas presionan, y lo que funcionaba el mes pasado deja de servir cuando cambia el algoritmo.",
      demand = Demand.ALTA,
      future = "Todas las empresas venden en línea. La IA genera textos e imágenes, así que el valor está en la estrategia, el análisis y entender a las personas."),
    Career("economia", "Economía", Area.NEG, listOf(Area.SOC, Area.TECH),
      imagine = "Saber cómo hacerse rica o predecir las crisis.",
      reality = "Es una carrera muy matemática y estadística. El día a día es armar bases de datos, correr modelos, leer informes y escribir documentos largos para bancos, entidades del gobierno o consultoras. Las predicciones fallan seguido, y hay que explicar por qué.",
      demand = Demand.ESTABLE,
      future = "Se busca a quien sabe programar y analizar datos, y a quien entiende temas como cambio climático, empleo y políticas públicas."),
    Career("contaduria", "Contaduría pública", Area.NEG, listOf(Area.TECH),
      imagine = "Sumar y restar todo el día con una calculadora.",
      reality = "Hoy el software hace las sumas. Tu trabajo es entender normas de impuestos que cambian seguido, comprobar que las cifras de una empresa sean reales, detectar errores o fraudes y asesorar decisiones. Sí hay temporadas muy pesadas (cierres de año, declaraciones de renta) y trabajo de detalle repetitivo. Además, firmas con responsabilidad legal.",
      demand = Demand.ESTABLE,
      future = "Tiene buena empleabilidad. Lo mecánico se automatiza; crecen la auditoría, el análisis financiero, los impuestos y la contabilidad ambiental."),

    // ── Ciencia y ambiente ──
    Career("biologia", "Biología", Area.CIEN,
      imagine = "Explorar la selva descubriendo especies nuevas, como en un documental.",
      reality = "El trabajo de campo existe (con mosquitos, lluvia y días de caminata), pero es corto comparado con las semanas de laboratorio identificando muestras, analizando datos y escribiendo informes y propuestas para conseguir financiación. En investigación el empleo suele ser inestable y pide maestría o doctorado.",
      demand = Demand.CAMBIA,
      future = "Colombia es uno de los países con más biodiversidad del mundo: hay oportunidades en conservación, estudios ambientales, biotecnología y agro. Los empleos estables suelen pedir posgrado o combinar biología con datos."),
    Career("ambiental", "Ingeniería ambiental", Area.CIEN, listOf(Area.TECH),
      imagine = "Salvar el planeta plantando árboles y limpiando océanos.",
      reality = "Mucho del trabajo es técnico y de normas: tomar muestras de agua o aire, hacer estudios de impacto ambiental, tramitar permisos, diseñar plantas de tratamiento y revisar que una empresa cumpla la ley. A veces trabajas para la misma empresa que contamina, ayudándola a reducir el daño, y eso exige negociar mucho.",
      demand = Demand.CRECE,
      future = "Crece con el cambio a energías limpias, la gestión del agua y los residuos, y las exigencias de sostenibilidad a las empresas."),
    Career("quimica", "Química y química farmacéutica", Area.CIEN, listOf(Area.SALUD),
      imagine = "Mezclar líquidos de colores y descubrir un medicamento milagroso.",
      reality = "Lo cotidiano es el control de calidad: repetir análisis siguiendo protocolos estrictos, calibrar equipos, registrar cada dato y trabajar con bata, gafas, guantes y normas de seguridad. Crear un medicamento nuevo toma muchos años y equipos grandes de personas.",
      demand = Demand.ESTABLE,
      future = "Estable en farmacéuticas, alimentos, cosméticos y control ambiental. Crecen la química verde y los materiales sostenibles."),
  )

  fun byId(id: String) = all.firstOrNull { it.id == id }

  /** Carreras de un área: primero las que son de esa área, luego las que también se relacionan con ella. */
  fun forArea(area: Area) = all.filter { it.area == area } + all.filter { area in it.also }
}
