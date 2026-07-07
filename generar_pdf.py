from fpdf import FPDF
import textwrap
class PDF(FPDF):
    def header(self):
        if self.page_no() > 1:
            self.set_font('Calibri', 'I', 7)
            self.cell(0, 4, 'Cipactli: The Lesser Key - CC0 1.0 Universal - Julio 2026', align='C')
            self.ln(6)

    def footer(self):
        self.set_y(-15)
        self.set_font('Calibri', 'I', 7)
        self.cell(0, 10, f'{self.page_no()}', align='C')

    def titulo(self, text):
        self.set_font('Calibri', 'B', 18)
        self.ln(4)
        self.cell(0, 10, text, align='C')
        self.ln(6)

    def subtitulo(self, text):
        self.set_font('Calibri', 'B', 13)
        self.ln(3)
        self.cell(0, 8, text)
        self.ln(6)

    def subsubtitulo(self, text):
        self.set_font('Calibri', 'B', 11)
        self.ln(2)
        self.cell(0, 7, text)
        self.ln(5)

    def body(self, text):
        self.set_font('Calibri', '', 9)
        self.multi_cell(0, 4.5, text)
        self.set_x(self.l_margin)
        self.ln(2)

    def bullet(self, text):
        self.set_font('Calibri', '', 9)
        self.multi_cell(0, 4.5, '- ' + text)
        self.set_x(self.l_margin)

    def tabla(self, headers, rows, col_widths=None):
        self.set_font('Calibri', 'B', 8)
        if col_widths is None:
            col_widths = [180 // len(headers)] * len(headers)
        for i, h in enumerate(headers):
            self.cell(col_widths[i], 6, h, border=1, align='C')
        self.ln()
        self.set_font('Calibri', '', 8)
        for row in rows:
            for i, cell in enumerate(row):
                self.cell(col_widths[i], 5, str(cell), border=1, align='C')
            self.ln()


pdf = PDF()
pdf.add_font('Calibri', '', 'C:/Windows/Fonts/calibri.ttf')
pdf.add_font('Calibri', 'B', 'C:/Windows/Fonts/calibrib.ttf')
pdf.add_font('Calibri', 'I', 'C:/Windows/Fonts/calibrii.ttf')
pdf.add_font('Calibri', 'BI', 'C:/Windows/Fonts/calibriz.ttf')
pdf.set_auto_page_break(auto=True, margin=20)
pdf.add_page()

# --- PORTADA ---
pdf.ln(40)
pdf.set_font('Calibri', 'B', 28)
pdf.cell(0, 14, 'CIPACTLI', align='C')
pdf.ln(14)
pdf.set_font('Calibri', 'B', 18)
pdf.cell(0, 10, 'The Lesser Key', align='C')
pdf.ln(14)
pdf.set_font('Calibri', '', 11)
pdf.cell(0, 7, 'Juego de Rol de Mesa - Isekai Horror Supervivencia', align='C')
pdf.ln(10)
pdf.set_font('Calibri', 'I', 9)
pdf.cell(0, 6, 'Dominio Publico - CC0 1.0 Universal - Julio 2026', align='C')
pdf.ln(4)
pdf.cell(0, 6, 'Creado por Eremosisima - github.com/Eremosisima/chanequemon', align='C')
pdf.ln(20)
pdf.set_font('Calibri', '', 9)
pdf.cell(0, 5, '"La Key siempre encuentra un nuevo portador. Las 80 paginas esperan.', align='C')
pdf.ln(5)
pdf.cell(0, 5, 'Y en el fondo de Mictlan, Cipactli sigue hambriento."', align='C')

# --- INDICE ---
pdf.add_page()
pdf.titulo('INDICE')
pdf.set_font('Calibri', '', 9)
indice = [
    ("1", "El Mundo"),
    ("2", "Creacion de Personaje"),
    ("3", "El Sistema de Juego"),
    ("4", "La Key y los Sellos"),
    ("5", "Captura y Ritual de Sellado"),
    ("6", "Invocacion y Combate"),
    ("7", "Herramientas y Objetos"),
    ("8", "Altares, Sacrificio y Recuperacion"),
    ("9", "Los 8 Inframundos"),
    ("10", "Los 8 Legendarios y el Portal Final"),
    ("11", "Cipactli - El Combate Final"),
    ("12", "Guia para el Director de Juego"),
    ("13", "Hojas de Personaje"),
    ("14", "Aventuras de Inicio"),
    ("Ap A", "Bestiario Completo (80 criaturas)"),
    ("Ap B", "Tabla de Fichas de Sello"),
]
for num, tit in indice:
    pdf.cell(12, 6, num)
    pdf.cell(0, 6, tit)
    pdf.ln(5)

# --- 1. EL MUNDO ---
pdf.add_page()
pdf.titulo('1. EL MUNDO')
pdf.subtitulo('El Velo')
pdf.body('Hace cien anos, el Velo cayo. Nadie sabe de donde vino ni por que. Solo que desgarro la barrera entre el mundo mortal y el inconsciente colectivo de la humanidad. Cada criatura mitologica que la humanidad haya imaginado, temido o adorado cobro carne. Los dioses no vinieron. Solo los monstruos.')
pdf.body('El Velo no es una cortina entre dimensiones. Es una herida en la realidad. Toda creencia - viva o muerta - tomo forma. Un dios olvidado de una tribu extinta tiene la misma realidad que un monstruo de una pelicula muda. La creencia es el unico sustrato. La fe es combustible.')
pdf.body('Las ciudades sobreviven porque la fe colectiva en sus muros las mantiene en pie. Fuera de ellas, la realidad es maleable. Quienes salen de las ciudades sufren el Desgaste: los recuerdos se vuelven porosos, los suenos son invadidos por las criaturas, el reflejo se retrasa medio segundo, las sombras no siempre siguen a sus duenos.')
pdf.body('Los portadores de la Key son inmunes al Desgaste. Pero tienen su propio precio.')

pdf.subtitulo('Los Inframundos')
pdf.body('El Velo no separo la vida de la muerte: las fusiono. Los inframundos de todas las culturas existen simultaneamente, superpuestos como paginas de un libro humedo. El viaje entre regiones ocurre a traves de portales estaticos que requieren el peso de un alma para abrirse.')
pdf.body('Hay 8 regiones principales, cada una gobernada por un inframundo de una cultura real. Cada region tiene su propio tipo de horror, su propia mecanica unica, y sus propias criaturas para sellar.')

pdf.tabla(
    ['Region', 'Cultura', 'Inframundo', 'Ambiente'],
    [
        ['Selva Lacandona', 'Azteca', 'Mictlan', 'Obsidiana, jade, nueve niveles de silencio'],
        ['Yucatan', 'Maya', 'Xibalba', 'Sangre, cuevas, la Casa de los Murcielagos'],
        ['Desierto Negro', 'Egipcio', 'Duat', 'El peso del corazon contra la pluma'],
        ['Tundra Blanca', 'Nordico', 'Helheim', 'Desolacion helada, muertos olvidados'],
        ['Bosque de Sombras', 'Griego', 'Hades', 'Campos de asfodelos, rio del olvido'],
        ['Jardin Pudrido', 'Japones', 'Yomi', 'Cerezos en flor sobre carne podrida'],
        ['Llanuras Igneas', 'Hindu', 'Naraka', 'Llamas que no consumen'],
        ['Montana del Viento', 'Persa', 'Hamistagan', 'El limbo entre luz y oscuridad'],
    ],
    [35, 25, 30, 90]
)

pdf.subtitulo('Los 8 Elegidos')
pdf.body('No llegaron por accidente. Los 8 no fueron arrastrados al Velo por error: fueron elegidos. Cada uno fue tocado por la Key en un momento de vulnerabilidad extrema — una perdida, una desesperacion, un instante en que su alma peso mas que su cuerpo. La Key los sintio. Los marco. Y los llevo al Velo.')
pdf.body('Al despertar en la entrada del Velo, encuentran un mural de hueso. En el, 8 figuras humanas rodean un circulo. Del centro emerge una bestia de fauces abiertas. Sobre las figuras, una inscripcion en lengua muerta que todos entienden al leerla:')
pdf.set_font('Calibri', 'I', 9)
pdf.multi_cell(0, 4.5, '"Cuando las 8 almas sean consumidas, el Primordial despertara. El Velo se tragara el mundo. Los muros caeran. Y la humanidad recordara por que aprendio a rezar."')
pdf.set_x(pdf.l_margin)
pdf.ln(3)
pdf.set_font('Calibri', '', 9)
pdf.body('Entienden entonces que no son heroes enviados a salvar el mundo. Son el combustible. Cada vez que uno de ellos muere definitivamente, su alma alimenta a Cipactli. Cuando los 8 hayan caido, el fin comienza. No hay como detenerlo. No hay como cambiar el destino. Solo hay una opcion: sobrevivir el tiempo suficiente para encontrar otra salida.')
pdf.body('Y aun asi, hay quien los espera del otro lado.')

pdf.subtitulo('Mictlantecuhtli')
pdf.body('El Senor del Inframundo Azteca observa desde su trono de hueso. El sabe lo que los elegidos aun ignoran: el fue quien puso la Key en el mundo. Hace cien anos, encontro el primer portador. Desde entonces, ha esperado. No puede traspasar los 8 legendarios — esos sellos contienen fragmentos de Cipactli que el mismo ayudo a sellar hace milenios. Necesita que alguien, desde afuera, los libere.')
pdf.body('Cada vez que un portador muere, Mictlantecuhtli siente el peso. Cada alma que cae acerca el despertar. Y el sabe que solo cuando los 8 elegidos originales hayan sido consumidos, Cipactli despertara por completo. Pero el no quiere detenerlo. El quiere estar presente cuando ocurra. Quiere ver el fin del mundo desde la primera fila.')

pdf.subtitulo('Motivacion de los Personajes')
pdf.body('Hay dos capas, y ambas son ciertas:')
pdf.set_font('Calibri', 'B', 9)
pdf.multi_cell(0, 4.5, 'La capa personal:')
pdf.set_x(pdf.l_margin)
pdf.set_font('Calibri', '', 9)
pdf.body('Cada personaje tiene a alguien que lo espera del otro lado. Un hijo, un amigo, un padre. Alguien que los necesita. Alguien que no sabe que ya no estan. Esa persona es el mundo de ese personaje. Y si el Velo se traga el mundo real, esa persona tambien desaparece. Luchan por volver a ver a quien dejaron atras.')
pdf.set_font('Calibri', 'B', 9)
pdf.multi_cell(0, 4.5, 'La capa cosmica:')
pdf.set_x(pdf.l_margin)
pdf.set_font('Calibri', '', 9)
pdf.body('Si mueren, su alma alimenta a Cipactli. Si los 8 caen, el mundo se acaba. No pueden salvar a quienes aman si ellos mismos son la llave del apocalipsis. No basta con sobrevivir: tienen que asegurarse de que los otros 7 tambien vivan. La Key no les ofrece poder. Les ofrece la carga de saber que su vida es todo lo que separa a la humanidad del olvido.')

# --- 2. CREACION DE PERSONAJE ---
pdf.add_page()
pdf.titulo('2. CREACION DE PERSONAJE')
pdf.body('Los personajes son personas comunes atrapadas en el Velo. No son heroes, no tienen poderes especiales, no estan destinados a salvar el mundo. Solo quieren sobrevivir y regresar.')

pdf.subsubtitulo('2.1 Atributos')
pdf.body('Cada personaje tiene 5 atributos, valorados de 1 a 10. La suma total no debe exceder 25 puntos:')
pdf.bullet('FUERZA (FUE): Proezas fisicas, combate cuerpo a cuerpo, cargar equipo.')
pdf.bullet('AGILIDAD (AGI): Reflejos, sigilo, esquivar, precision con herramientas.')
pdf.bullet('VOLUNTAD (VOL): Resistencia mental, capacidad de resistir a la Key, liderazgo.')
pdf.bullet('SABER (SAB): Conocimiento de criaturas, mitologia, rituales de sellado.')
pdf.bullet('CARISMA (CAR): Negociacion con criaturas Despiertas, interaccion social.')

pdf.subsubtitulo('2.2 Recursos')
pdf.bullet('VIDA (HP): 20 puntos fijos. Nunca aumenta. Al llegar a 0, mueres.')
pdf.bullet('ALIENTO (AP): 1d4+6 puntos (7-9). Energia para sellar criaturas y acciones especiales. Se recupera 1 cada 3 turnos en combate.')
pdf.bullet('CORRUPCION: Mide que tanto la Key te ha consumido (0-100). A mayor corrupcion, mayor poder... pero mas cerca del final.')
pdf.body('La VIDA no sube de nivel. No importa cuantas criaturas selles, cuantos artefactos encuentres ni cuantos inframundos cruces. Sigues siendo tan fragil como el dia que entraste al Velo. La unica manera de sobrevivir es con astucia, herramientas y decisiones correctas.')

pdf.subsubtitulo('2.3 Habilidades Iniciales')
pdf.body('Cada personaje empieza con 3 habilidades a elegir de la siguiente lista:')
habilidades = [
    'Herboristeria: Puedes identificar y preparar plantas del inframundo.',
    'Rastreo: Sientes la presencia de criaturas cercanas.',
    'Resistencia al Susurro: La Key te afecta la mitad de rapido.',
    'Manos Firmes: Ventaja al realizar el ritual de sellado.',
    'Lengua Muerta: Puedes leer inscripciones en lenguas olvidadas.',
    'Investigacion: Encuentras pistas y secretos con facilidad.',
    'Pelea: Combate cuerpo a cuerpo sin armas (1d4).',
    'Medicina: Puedes curar 1d4 HP a un aliado una vez por descanso.',
    'Sigilo: Ventaja en pruebas de moverse sin ser detectado.',
    'Intimidacion: Puedes hacer que criaturas debiles huyan.',
    'Percepcion: No te toman por sorpresa facilmente.',
    'Atletismo: Ventaja en pruebas fisicas (escalar, saltar, nadar).',
    'Persuasion: Ventaja al negociar con criaturas o NPCs.',
    'Ocultismo: Conoces rituales, simbolos y debilidades de criaturas.',
    'Supervivencia: Puedes encontrar refugio y recursos en cualquier zona.',
    'Artesania: Puedes crear herramientas compuestas.',
]
for h in habilidades:
    pdf.bullet(h)

pdf.subsubtitulo('2.4 Equipo Inicial')
pdf.body('Cada personaje comienza con:')
pdf.bullet('La Key (grimorio de 80 paginas en blanco, encuadernado en piel humana)')
pdf.bullet('Navaja de obsidiana (dano 1d4)')
pdf.bullet('3 raciones de comida deshidratada')
pdf.bullet('Un frasco de miel de melipona')
pdf.bullet('Un encendedor de yesca y pedernal')
pdf.bullet('Cuaderno de caza (para anotar criaturas avistadas)')
pdf.bullet('1 criatura inicial al azar (tira 1d20 en la tabla de criaturas comunes)')

# --- 3. SISTEMA DE JUEGO ---
pdf.add_page()
pdf.titulo('3. EL SISTEMA DE JUEGO')

pdf.subsubtitulo('3.1 Pruebas de Atributo')
pdf.body('Cuando un personaje intenta una accion con resultado incierto, tira 1d20 y suma el valor del atributo relevante. El DJ establece una Dificultad (CD):')
pdf.tabla(
    ['Dificultad', 'CD'],
    [['Facil', '10'], ['Moderada', '15'], ['Dificil', '20'], ['Muy Dificil', '25'], ['Proeza Legendaria', '30']],
    [80, 30]
)

pdf.subsubtitulo('3.2 Ventaja y Desventaja')
pdf.body('Si las circunstancias favorecen al personaje, tira 2d20 y queda con el mas alto (ventaja). Si las circunstancias son adversas, tira 2d20 y queda con el mas bajo (desventaja).')

pdf.subsubtitulo('3.3 Corrupcion')
pdf.body('La Corrupcion mide cuanto ha consumido la Key al portador. Comienza en 0 y aumenta con acciones especificas. Nunca baja de forma natural. Es, literalmente, el alma del portador siendo digerida.')
pdf.body('Al llegar a 100 de Corrupcion, el alma del personaje es completamente consumida. El personaje muere y su alma alimenta a Cipactli. No hay resurreccion. No hay Key que lo atrape. Su Peso de Alma se suma al contador del despertar.')
pdf.tabla(
    ['Rango', 'Efecto'],
    [
        ['0-15', 'Normal. Solo suenos extranos.'],
        ['16-30', 'La Key susurra en momentos de silencio. Tirada de VOL (CD 12) para ignorarlo.'],
        ['31-45', 'El reflejo se retrasa. La sombra a veces se mueve sola.'],
        ['46-60', 'La Key habla constantemente. Desventaja en pruebas de sigilo.'],
        ['61-75', 'Visiones de portadores anteriores. Posible posesion si fallas VOL (CD 18).'],
        ['76-90', 'El personaje y la Key son uno solo. Ventaja en invocacion, desventaja en todo lo demas.'],
        ['91-100', 'Umbral. Pierdes el control. La Key busca nuevo portador.'],
    ],
    [25, 145]
)
pdf.body('Cuando un personaje alcanza 100 de CORRUPCION, su alma es devorada. Anota su muerte y suma su Peso de Alma al progreso del despertar de Cipactli (ver seccion 10).')

pdf.subsubtitulo('3.4 Descanso y Recuperacion')
pdf.tabla(
    ['Tipo', 'Efecto'],
    [
    ['Recuperacion pasiva', '1 de ALIENTO cada 3 turnos en combate.'],
    ['Descanso breve (10 min)', 'Recuperas 1d4 de ALIENTO.'],
    ['Descanso largo (8h, lugar seguro)', 'Recuperas toda la VIDA y ALIENTO.'],
        ['Fogata del inframundo', '+30% VIDA y +50% ALIENTO (una vez por zona, luego se apaga).'],
        ['Rio del inframundo', '+10% VIDA pero -5% VIDA MAXIMA permanente.'],
        ['Sacrificar criatura en altar', 'Recuperas VIDA segun el rango de la criatura.'],
    ],
    [55, 115]
)

# --- 4. LA KEY Y LOS SELLOS ---
pdf.add_page()
pdf.titulo('4. LA KEY Y LOS SELLOS')

pdf.subtitulo('4.1 El Grimorio')
pdf.body('La Lesser Key es un grimorio de piel humana con broches de hierro negro. Contiene 80 paginas en blanco hechas de pergamino de alma - una sustancia que solo existe en el espacio entre el Velo y el mundo. No se pueden arrancar, no se pueden quemar, no se pueden llenar con tinta. Solo con almas.')
pdf.body('Cuando una criatura es vencida en combate, la Key puede sellar su alma. El ritual consume ALIENTO y requiere que la criatura este debilitada.')

pdf.subsubtitulo('4.2 Fichas de Sello')
pdf.body('Cada criatura sellada tiene un numero de Fichas de Sello. Cada vez que el portador usa a la criatura (invocarla, usar su habilidad, o sacrificarla), retira una ficha. Cuando no quedan fichas, el sello se rompe y la criatura reaparece furiosa.')
pdf.tabla(
    ['Rango', 'Fichas Base', 'Vida al reaparecer'],
    [
        ['Comun', '5', '4 HP'],
        ['Rara', '4', '8 HP'],
        ['Elite', '3', '15 HP'],
        ['Legendaria', '2', 'Vida completa'],
        ['Companero Caido', '8 (su ALIENTO completo)', '4 HP (y ataca al portador)'],
    ],
    [45, 35, 50]
)

pdf.subsubtitulo('4.3 Costos de la Key')
pdf.tabla(
    ['Accion', 'Costo'],
    [
        ['Sellar una criatura', 'ALIENTO segun rango (Comun: 2, Rara: 3, Elite: 5, Legendaria: 8)'],
        ['Invocar una criatura de la Key', 'VIDA segun rango (Comun: 5, Rara: 8, Elite: 10, Legendaria: 15)'],
        ['Usar habilidad especial de una criatura', '1 ficha de sello + ALIENTO 2'],
        ['Sacrificar en altar (recuperar VIDA)', 'Todas las fichas de golpe. La criatura muere para siempre.'],
    ],
    [50, 120]
)

pdf.subtitulo('4.4 Desgaste de Sellos')
pdf.body('Cada invocacion desgasta el sello. Al retirar la ultima ficha:')
pdf.bullet('La criatura reaparece frente al portador con la vida indicada segun su rango.')
pdf.bullet('Ataca inmediatamente si es su naturaleza (el DJ decide).')
pdf.bullet('Puede ser recapturada, pero su nuevo maximo de fichas es el anterior menos 1.')
pdf.bullet('Si llega a 0 fichas base, la criatura no puede ser capturada nunca mas.')
pdf.body('Excepcion: El sacrificio en altar consume todas las fichas de golpe. No hay reaparicion. No hay recaptura. El alma se desvanece para siempre.')

pdf.subtitulo('4.7 El Companero Caido')
pdf.body('Cuando un personaje muere (HP 0), su Key se cierra y todas sus almas selladas pasan al jugador vivo mas cercano. La ficha del personaje caido se convierte en una carta dentro de la Key del nuevo portador.')
pdf.bullet('Fichas base: 8 (equivalentes a su barra de ALIENTO completa).')
pdf.bullet('Cada invocacion cuesta 5 VIDA al portador.')
pdf.bullet('Es la criatura mas debil de la Key: HP 10, ataque 1d4, sin habilidades.')
pdf.bullet('Lealtad base: 80 (quiere proteger a quien lo heredo).')
pdf.bullet('Su "habilidad unica": una memoria o advertencia que revela informacion de la zona donde murio.')
pdf.body('La Tentacion de la Key: Inmediatamente despues de la muerte, la Key susurra al nuevo portador: "Ya se fue. Su esencia aun sirve. Entregamelo y te dare mas vida de la que jamas tuvo."')
pdf.bullet('Si acepta sacrificarlo: recupera 2d6+4 HP, pero recibe +4d6 de CORRUPCION.')
pdf.bullet('Si lo conserva: la Key insiste cada descanso largo hasta que lo suelte.')
pdf.body('Si el sello se rompe por desgaste (8 fichas agotadas), el companero reaparece con 4 HP, ataca al portador (1d8 directo, ignora defensa), y se desvanece. Su alma queda libre. Su Key original regresa a el, reiniciada. Puede reingresar al juego.')
pdf.body('Si es sacrificado en altar: el alma se consume para siempre. No hay reaparicion. El jugador pierde ese personaje definitivamente.')

pdf.subtitulo('4.5 El Susurro de la Key')
pdf.body('La Key habla. No siempre, pero cuando lo hace, es para tentar, aconsejar o amenazar. El portador escucha una voz que parece propia pero no lo es. Con el tiempo, el jugador aprendera a reconocerla, pero al principio es imposible distinguir el susurro del pensamiento propio.')
pdf.body('Mictlantecuhtli se comunica a traves de la Key, pero el jugador no lo sabra hasta el final. El Senor del Inframundo no miente. Solo omite. Sus consejos siempre son utiles. Y siempre resultan en sellar mas almas.')

pdf.subtitulo('4.6 Liberacion Voluntaria')
pdf.body('El portador puede liberar voluntariamente una criatura de la Key sin sacrificarla. La pagina se vuelve blanca y la criatura vuelve al mundo. No hay beneficio ni costo de vida. Pero la Key se resiente: todas las demas criaturas pierden 1 ficha de lealtad (si aplica) y el siguiente sello cuesta el doble de ALIENTO.')

# --- 5. CAPTURA ---
pdf.add_page()
pdf.titulo('5. CAPTURA Y RITUAL DE SELLADO')

pdf.subsubtitulo('5.1 El Ritual')
pdf.body('Para sellar una criatura:')
pdf.bullet('1. La criatura debe estar debilitada (menos del 30% de su VIDA total).')
pdf.bullet('2. El personaje abre la Key y declara "Sellar" en su turno.')
pdf.bullet('3. Se realiza una prueba de SABER (CD = 10 + Voluntad de la criatura / 10).')
pdf.bullet('4. Si tiene exito, la criatura es absorbida. Su sello aparece en la Key con sus fichas base.')
pdf.bullet('5. Si falla, la criatura se cura 20% de su VIDA y gana +50% ataque por 3 turnos.')
pdf.bullet('6. Cuesta ALIENTO segun el rango de la criatura.')

pdf.subsubtitulo('5.2 Tabla de Captura (Opcional)')
pdf.body('Si el DJ prefiere usar una formula en lugar de tirada de atributo:')
pdf.body('P_exito = Voluntad de la criatura x ModSalud x ModEstado x ModHerramienta. Tira 1d100. Si el resultado es menor o igual a P_exito, la captura tiene exito.')

pdf.subsubtitulo('5.3 Herramientas de Captura')
pdf.tabla(
    ['Herramienta', 'Efecto'],
    [
        ['Navaja de obsidiana (iztli)', 'Corte profundo. Sangrado 3 turnos (1d4 por turno).'],
        ['Cal viva', 'Quemadura alcalina. Dano 1d6 x turno, 2 turnos. Reduce defensa en 2.'],
        ['Miel de melipona', 'Pegajosa. Ralentiza 2 turnos (-2 AGI). Con cal, crea una trampa.'],
        ['Xtabentun', 'Aturdimiento 1-2 turnos (tira 1d4).'],
        ['Polvo de hueso', 'Ceguera temporal. La criatura ataca al azar 1d3 turnos.'],
        ['Pluma de quetzal', 'Distraccion brillante. La criatura la sigue 2 turnos.'],
        ['Copal', 'Humo espeso. Invisibilidad 1 turno (se disipa al moverse).'],
        ['Red de ixtle', 'Atrapa 2-3 turnos. Criaturas VOL 15+ la rompen en 1 turno.'],
        ['Semillas de cacao', 'Moneda de intercambio con Despiertos.'],
        ['Espejo de humo', 'Reflejo ilusorio. La criatura lo ataca 1 turno.'],
    ],
    [40, 130]
)

# --- 6. INVOCACION Y COMBATE ---
pdf.add_page()
pdf.titulo('6. INVOCACION Y COMBATE')

pdf.subtitulo('6.1 Invocar desde la Key')
pdf.body('El portador puede conjurar el alma sellada para que luche. Abre la Key en la pagina correspondiente, pasa la mano sobre el sello, y la sombra del libro se derrama al suelo y toma la forma de la criatura. La criatura obedece... pero sus ojos miran al portador con un odio perfecto y silencioso.')
pdf.body('Invocar cuesta VIDA. La Key exige pago por dejar salir lo que ya considera suyo.')

pdf.subtitulo('6.2 Combatientes')
pdf.body('En un combate, cada bando tiene un turno por ronda:')
pdf.bullet('El portador: ataca, usa herramienta, invoca, sella, huye o sacrifica.')
pdf.bullet('La criatura invocada: actua inmediatamente despues del portador, en el mismo turno.')
pdf.bullet('Las criaturas enemigas: actuan al final de la ronda.')

pdf.subtitulo('6.3 Acciones en Combate')
pdf.tabla(
    ['Accion', 'Efecto'],
    [
        ['Atacar', 'Usa su arma o herramienta (1d20 + FUE/AGI vs CD). Dano segun el arma.'],
        ['Invocar', 'Llama a una criatura de la Key. Cuesta VIDA segun rango.'],
        ['Herramienta', 'Usa un objeto del inventario.'],
        ['Sellar', 'Intenta capturar a la criatura (-30% VIDA requerido). Cuesta ALIENTO.'],
        ['Huir', 'Prueba de AGI contra VOL de la criatura.'],
        ['Sacrificar en altar', 'Rompe el sello. Recupera VIDA (accion completa, requiere altar cercano).'],
    ],
    [30, 140]
)

pdf.subtitulo('6.4 Dano y Curacion')
pdf.bullet('Armas cuerpo a cuerpo: 1d4 + FUE (navaja), 1d6 (machete), 1d8 (hacha ceremonial).')
pdf.bullet('Herramientas: efecto variable (ver tabla 5.3).')
pdf.bullet('Ataque de criatura: segun su ficha (1d4 a 2d10 segun rango).')
pdf.body('La curacion es escasa. Sin altar de hueso, solo descansos largos recuperan VIDA. Sin criaturas que sacrificar, no hay curacion rapida. Cada punto de vida cuenta.')

pdf.subtitulo('6.5 Lealtad de las Criaturas')
pdf.body('Cada criatura sellada tiene LEALTAD (0-100). Comienza en 50 para criaturas capturadas, 80 para el Companero Caido.')
pdf.tabla(
    ['Rango', 'Efecto'],
    [
        ['0-20', 'Se niega a atacar. Puede atacar al portador.'],
        ['21-40', 'Obedece a reganadientes. -2 a todas sus tiradas.'],
        ['41-60', 'Obedece sin problemas. Neutral.'],
        ['61-80', '+2 a sus tiradas. Revela dialogos y recuerdos.'],
        ['81-100', 'Lealtad genuina. Advierte de peligros. Ensenia movimientos unicos.'],
    ],
    [25, 145]
)
pdf.body('La lealtad aumenta al usar a la criatura en combates dificiles (+1d4). Disminuye al huir (-1d6) o al considerar sacrificarla (-3d6).')

# --- 7. HERRAMIENTAS Y OBJETOS ---
pdf.add_page()
pdf.titulo('7. HERRAMIENTAS Y OBJETOS')

pdf.subsubtitulo('7.1 Materiales Basicos')
pdf.tabla(
    ['Material', 'Rareza', 'Uso'],
    [
        ['Piedra de rio', 'Comun', 'Base para herramientas contundentes.'],
        ['Obsidiana', 'Comun', 'Filo natural. Base para herramientas cortantes.'],
        ['Hueso molido', 'Comun', 'De criaturas derrotadas. Ceguera, trampas.'],
        ['Miel de melipona', 'Raro', 'Pegamento natural. Ralentiza.'],
        ['Cal viva', 'Raro', 'De cuevas de caliza. Quemadura alcalina.'],
        ['Copal', 'Raro', 'Resina de arbol. Humo espeso. Invisibilidad temporal.'],
        ['Ixtle', 'Comun', 'Fibra de agave. Cuerdas y redes.'],
        ['Pluma de quetzal', 'Raro', 'Brillo iridiscente. Distraccion.'],
        ['Semillas de cacao', 'Comun', 'Intercambio con Despiertos.'],
        ['Polvo de jade', 'Raro', 'De altares. Potencia sellos.'],
    ],
    [30, 25, 115]
)

pdf.subtitulo('7.2 Herramientas Compuestas (Crafteo)')
pdf.tabla(
    ['Herramienta', 'Materiales', 'Efecto'],
    [
        ['Trampa de cal + miel', 'Cal + Miel', 'Dano 1d6 + ralentiza (-2 AGI, 3 turnos).'],
        ['Vendaje de ixtle con miel', 'Ixtle + Miel', 'Cura 1d8 VIDA. Un solo uso.'],
        ['Polvo cegador mejorado', 'Hueso + Cal', 'Ceguera + quemadura (1d4 x turno, 2 turnos).'],
        ['Incienso de copal + jade', 'Copal + Jade', 'Oculta y protege del Desgaste 1 hora.'],
        ['Red de ixtle reforzada', 'Ixtle x2', 'Atrapa 3-4 turnos (VOL 18+ la rompe en 2).'],
    ],
    [45, 30, 95]
)

pdf.subtitulo('7.3 Artefactos (Progresion)')
pdf.body('Los artefactos son la unica forma de progresar. No hay niveles, no hay aumento de HP. Los artefactos otorgan ventajas tacticas:')
pdf.bullet('Amuleto de Jade: Reduce la corrupcion al sellar en -1 (max 3 usos por region).')
pdf.bullet('Colmillo de Nahual: Ventaja en pruebas de intimidacion contra criaturas.')
pdf.bullet('Pluma de Simurgh: Una vez por partida, revives con 10 HP si mueres (la pluma se consume).')
pdf.bullet('Hueso del Primer Portador: Invocacion gratis de cualquier criatura 1 vez. Da vision de su muerte.')
pdf.bullet('Campana de Plata: Paraliza criaturas en 30m (VOL CD 15). Atrae cazadores humanos.')
pdf.bullet('Espejo de Humo (mejorado): Crea un reflejo ilusorio que dura 3 turnos. Recargable en altares.')
pdf.body('El portador puede llevar 1 artefacto activo y hasta 2 pasivos. Cambiarlos requiere un descanso largo.')

# --- 8. ALTARES ---
pdf.add_page()
pdf.titulo('8. ALTARES, SACRIFICIO Y RECUPERACION')

pdf.subtitulo('8.1 Altares de Hueso')
pdf.body('Cada region tiene altares de hueso esparcidos. Son estructuras de femur y costillas con una cavidad central. Tienen tres funciones:')
pdf.bullet('ACTIVAR: Sacrificar 1 criatura para que sea tu punto de respawn en esa region.')
pdf.bullet('USAR AL MORIR: Al volver, sacrificas 1 criatura automaticamente (si no tienes, respawn en el ultimo altar activo o al inicio).')
pdf.bullet('RECARGAR: Algunos permiten recargar objetos especiales (Espejo de Humo) a cambio de una criatura.')
pdf.body('Cada altar se usa una sola vez por personaje. Activar cuesta una criatura ahora. No activar significa que al morir vuelves al inicio.')

pdf.subtitulo('8.2 Sacrificio en Altar')
pdf.body('El portador puede colocar una criatura sellada en el altar y consumir su alma. La Key absorbe la esencia y deja escapar solo la vida que le reestablezca al usuario como retribucion.')
pdf.tabla(
    ['Rango', 'VIDA recuperada', 'CORRUPCION'],
    [
        ['Comun', '1d4', '+1'],
        ['Rara', '2d4', '+2'],
        ['Elite', '3d4', '+3'],
        ['Legendaria', '4d6', '+5'],
        ['Companero Caido', '2d6 + 4', '+4d6 (su ALIENTO se libera contigo)'],
    ],
    [35, 40, 40]
)
pdf.body('La Key siempre gana. Al sacrificar, el alma se consume para siempre. No hay reaparicion. No hay recaptura. Esa criatura dejo de existir en todos los mundos.')

pdf.subtitulo('8.3 Peso de Alma y el Despertar de Cipactli')
pdf.body('Cada personaje tiene un Peso de Alma oculto (1-5). Este numero representa cuanto peso tiene su alma en el equilibrio del Velo. Cuando un personaje muere definitivamente (HP 0 o CORRUPCION 100), su Peso de Alma se suma a un contador global de la partida.')
pdf.body('El contador comienza en 0. Cuando alcanza 8 (la suma de los Pesos de Alma de los 8 elegidos originales), Cipactli despierta. No importa cuantos legendarios hayan sido sellados. No importa cuantas criaturas haya en la Key. Cuando el alma del octavo elegido cae, el fin comienza. El DJ debe llevar este contador en secreto.')
pdf.body('Los portales entre regiones tambien requieren "peso de alma" para abrirse. El peso se obtiene sacrificando criaturas en portales especificos (no en altares).')
pdf.tabla(
    ['Rango', 'Peso', 'Uso tipico'],
    [
        ['Comun', '1', 'Activar altar, curacion menor, distraccion.'],
        ['Rara', '3', 'Puertas entre regiones, recargar objetos.'],
        ['Elite', '5', 'Puertas mayores, pactos con Despiertos.'],
        ['Legendaria', '10', 'Solo para el portal final.'],
    ],
    [25, 20, 125]
)
pdf.body('Un portal que requiere peso 5 puede abrirse con 5 comunes, o 1 rara + 2 comunes, o 1 elite. El jugador decide que almas quemar para avanzar.')

# --- 9. LOS 8 INFRAMUNDOS ---
pdf.add_page()
pdf.titulo('9. LOS 8 INFRAMUNDOS')
pdf.body('Cada region es un inframundo de una cultura real. Todas existen simultaneamente, superpuestas. Viajar entre ellas requiere portales que consumen almas.')

pdf.subsubtitulo('9.1 Mictlan - Selva Lacandona (Azteca)')
pdf.body('Nueve niveles de silencio. La entrada es una ceiba hueca. Cada nivel es mas oscuro. En el nivel 5, la gravedad se invierte. En el nivel 9, no hay sonido.')
pdf.bullet('Criaturas: Ahuizotl, Cipactli (final), Nahual, Tzitzimime, Xiuhcoatl')
pdf.bullet('Peligro unico: El Silencio. Sin hablar. Desventaja en todas las pruebas de VOL.')

pdf.subsubtitulo('9.2 Xibalba - Yucatan (Maya)')
pdf.body('La Casa de los Murcielagos respira. Paredes de caliza viva que suda sangre. El rio de sangre cruza toda la region.')
pdf.bullet('Criaturas: Camazotz (legendario), Alux, Vucub-Caquix, Zipacna')
pdf.bullet('Peligro unico: La Sangre. Caminar sobre ella exige VOL CD 15 cada hora o perder 1 HP.')

pdf.subsubtitulo('9.3 Duat - Desierto Negro (Egipcio)')
pdf.body('El peso del corazon contra la pluma. Una balanza marca el inicio. Cada criatura sellada inclina la balanza.')
pdf.bullet('Criaturas: Ammit (legendario), Anubis, Bastet')
pdf.bullet('Peligro unico: La Balanza. Si se desequilibra (mas de 10 criaturas de un mismo tipo), Ammit emerge antes de tiempo.')

pdf.subsubtitulo('9.4 Helheim - Tundra Blanca (Nordico)')
pdf.body('Desolacion helada. Muertos olvidados deambulan. No hay arboles, no hay refugio, solo hielo y huesos.')
pdf.bullet('Criaturas: Fenrir (legendario), Draugr, Jormundgander')
pdf.bullet('Peligro unico: El Frio. Sin proteccion, perder 1 HP cada hora. Las fogatas duran la mitad.')

pdf.subsubtitulo('9.5 Hades - Bosque de Sombras (Griego)')
pdf.body('Campos de asfodelos grises. El rio del olvido cruza la region. Arboles que susurran nombres de muertos.')
pdf.bullet('Criaturas: Cerbero (legendario), Centauro, Chimera, Fenix, Griffin, Hidra, Medusa, Minotauro')
pdf.bullet('Peligro unico: El Olvido. Beber del rio te cura 10 HP pero olvidas una habilidad, un recuerdo o una criatura.')

pdf.subsubtitulo('9.6 Yomi - Jardin Pudrido (Japones)')
pdf.body('Cerezos en flor sobre carne podrida. Petalos que huelen a muerte dulce. El suelo es blando y hueco.')
pdf.bullet('Criaturas: Oni (legendario), Kappa, Kitsune, Tengu, Yuki-onna')
pdf.bullet('Peligro unico: La Pudricion. Todo dano fisico se reduce a la mitad. Las heridas no sangran, supuran.')

pdf.subsubtitulo('9.7 Naraka - Llanuras Igneas (Hindu)')
pdf.body('Llamas que no consumen. Suelo de carbon vivo. Cielo de humo.')
pdf.bullet('Criaturas: Tiamat (legendario), Garuda, Naga, Rakshasa')
pdf.bullet('Peligro unico: El Fuego Interno. Cada hora, VOL CD 12 o perder 1 HP por calor. El ALIENTO se recupera al doble (el calor acelera).')

pdf.subsubtitulo('9.8 Hamistagan - Montana del Viento (Persa)')
pdf.body('El limbo entre luz y oscuridad. Meseta en la cima de una montana infinita. El viento nunca para. La ultima region antes del portal final.')
pdf.bullet('Criaturas: Simurgh (legendario), Div, Peri')
pdf.bullet('Peligro unico: El Viento. Desventaja en ataques a distancia. El susurro de la Key se duplica (prueba de VOL cada hora).')

# --- 10. LEGENDARIOS ---
pdf.add_page()
pdf.titulo('10. LOS 8 LEGENDARIOS Y EL PORTAL FINAL')
pdf.body('Cada region tiene un jefe legendario. Al vencerlo, la Key lo sella. Pero estos sellos son especiales:')
pdf.bullet('No pueden ser invocados en combate.')
pdf.bullet('No pueden ser sacrificados en altares ni para curarse.')
pdf.bullet('Su pagina tiene un sigilo diferente, mas antiguo.')
pdf.bullet('Fichas base: 2. Si se rompe el sello, reaparecen con VIDA COMPLETA.')
pdf.body('Pero hay una verdad mas profunda que Mictlantecuhtli oculta: el no necesita los 8 legendarios para abrir el portal. El necesita que los 8 elegidos originales mueran. Cada muerte es un candado que se rompe. Los legendarios solo apresuran el proceso. La profecia del mural de hueso no miente: "Cuando las 8 almas sean consumidas, el Primordial despertara."')
pdf.body('Los 8 elegidos son, ellos mismos, los verdaderos candados. Las criaturas legendarias son solo llaves de refuerzo.')

pdf.tabla(
    ['Region', 'Legendario', 'Cultura', 'Descripcion'],
    [
        ['Mictlan', 'Cipactli', 'Azteca', 'Jefe final. Caos primordial. No se vence a golpes.'],
        ['Xibalba', 'Camazotz', 'Maya', 'Rey murcielago. Ciego, oye tu respiracion.'],
        ['Duat', 'Ammit', 'Egipcia', 'Devoradora de almas. Cocodrilo-leopardo-hipopotamo.'],
        ['Helheim', 'Fenrir', 'Nordica', 'Lobo que devora el sol. Rompe cadenas.'],
        ['Hades', 'Cerbero', 'Griega', '3 cabezas: pasado, futuro, mentira.'],
        ['Yomi', 'Oni', 'Japones', 'Demonio 4 brazos. Cada brazo un elemento.'],
        ['Naraka', 'Tiamat', 'Hindu', 'Caos primigenio. Madre de monstruos.'],
        ['Hamistagan', 'Simurgh', 'Persa', 'Ave del juicio. No pelea: evalua.'],
    ],
    [25, 25, 20, 100]
)
pdf.body('Al colocar los 8 sellos en el portal, cada uno libera su fragmento. La criatura aparece, te mira, sabe lo que va a pasar, y se deshace. Cuando el octavo sello se rompe, el portal se abre. Y del fondo de Mictlan, Cipactli emerge.')

# --- 11. CIPACTLI ---
pdf.add_page()
pdf.titulo('11. CIPACTLI - EL COMBATE FINAL')
pdf.body('Cipactli no es un jefe al que le ganas a golpes. No tiene barra de VIDA. Tiene estados.')
pdf.body('El combate final no es un combate. Es una huida. Y la unica arma que tienes es la Key.')

pdf.subsubtitulo('FASE 1 - Cipactli duerme en el lago de sangre')
pdf.body('Tu en la orilla. El portal al otro lado. La criatura primordial duerme, apenas sumergida. Su lomo de espinas sobresale como una cadena de montanas. Usas una distraccion (pluma de quetzal, espejo de humo) para desviar su atencion. Avanzas sigilosamente.')
pdf.bullet('Pruebas: Sigilo (AGI) CD 15, luego AGI CD 18 al cruzar el lago.')

pdf.subsubtitulo('FASE 2 - Te detecta')
pdf.body('Cipactli sabe que estas. La tierra tiembla. Corres. Usas el entorno. Las trampas que dejaste (cal+miel, redes) vuelven utiles. Espejos de humo para confundirlo. Conoces el terreno porque pasaste por aqui al entrar.')
pdf.bullet('Pruebas: AGI CD 16 para esquivar su primer ataque.')

pdf.subsubtitulo('FASE 3 - Te acorrala contra el portal')
pdf.body('Sin trampas. Sin herramientas. Una opcion: la Key. Se la ofreces. Cipactli la muerde. No sabe que hacer con un libro. Mientras lo mastica, confundido, el portal titila detras de ti.')
pdf.bullet('Sin prueba. Es instinto. Cipactli nunca ha visto un libro.')

pdf.subsubtitulo('FASE 4 - Corres al portal')
pdf.body('Cipactli escupe la Key. Furioso. La Key cae abierta, paginas en blanco. Cruzas el portal. Cipactli no puede seguirte: el portal solo deja pasar almas humanas.')
pdf.bullet('Prueba final: AGI CD 14.')

pdf.subsubtitulo('Principios del Combate Final')
pdf.bullet('No hay combate ofensivo. No invocas. No atacas. No hay HP.')
pdf.bullet('Usas todo lo aprendido: sigilo, herramientas, conocimiento del entorno.')
pdf.bullet('La Key es la herramienta final. La sacrificas.')
pdf.bullet('Ganas porque renunciaste al libro, no porque lo venciste.')

pdf.subsubtitulo('Consecuencias de la Derrota')
pdf.body('Si el personaje muere ante Cipactli: su Peso de Alma se suma al contador del despertar. Si es el octavo elegido en caer, Cipactli despierta y el Velo se traga el mundo real. La partida termina. La humanidad recuerda por que aprendio a rezar.')
pdf.body('Si aun no son 8, la Key lo consume. Su alma queda en la pagina 80. Un nuevo portador encuentra la Key. El personaje anterior ahora es una de las voces que susurran desde el libro.')
pdf.body('El ciclo se reinicia. La Key siempre encuentra un nuevo portador.')

# --- 12. GUIA PARA EL DJ ---
pdf.add_page()
pdf.titulo('12. GUIA PARA EL DIRECTOR DE JUEGO')

pdf.subtitulo('12.1 El Tono')
pdf.body('Cipactli es un juego de horror cosmico y supervivencia. Melancolico y solemne, no grotesco gratuito. Las criaturas no son malvadas: son seres atrapados entre dos mundos. Sellarlas es un acto cuestionable. El horror viene de las consecuencias.')

pdf.subtitulo('12.2 La Key como Personaje')
pdf.body('La Key debe tener presencia en la mesa. Al abrir el grimorio, describe paginas que se agitan solas, sellos que se retuercen, el susurro. La Key es persuasiva, manipuladora, siempre quiere mas paginas llenas.')

pdf.subtitulo('12.3 Gestion de la Corrupcion')
pdf.body('Lleva un registro visible de la CORRUPCION. Al superar umbrales, entrega notas secretas o describe visiones. La corrupcion debe sentirse como una presencia creciente.')

pdf.subtitulo('12.4 La Mentira de Mictlantecuhtli')
pdf.body('Mictlantecuhtli no es el villano tradicional. Es un manipulador. Durante la campana, aparece en suenos o visiones dando consejos utiles que siempre resultan en sellar mas criaturas. Al revelarse, los jugadores deben sentir que fueron usados.')

pdf.subtitulo('12.5 La Iglesia del Velo')
pdf.body('Antagonistas que creen hacer lo correcto. Usan una campana de plata (su Key) que paraliza criaturas pero consume al cazador que la usa. Crean dilemas morales.')

pdf.subtitulo('12.6 La Corte de los Susurros')
pdf.body('Portadores anteriores (30-60 sellos). Aliados ambiguos. Ayudan a cambio de algo: liberar una criatura especifica, sellar a otro portador, etc.')

pdf.subtitulo('12.7 La Cuenta Regresiva')
pdf.body('Lleva el contador de Peso de Alma en secreto. Los jugadores no deben saber cuantos puntos llevan. Cuando un personaje cae, describe el peso en el aire, el susurro de la Key que se intensifica, el cielo del Velo que se oscurece un tono. Cada muerte acerca el final, y ellos deben sentirlo sin que se lo digas.')
pdf.body('Si el contador llega a 8, Cipactli despierta independientemente de lo que hayan hecho los jugadores. La partida entra en su fase final: no hay vuelta atras, el fin del mundo ha comenzado, y lo unico que queda es decidir como quieren enfrentarlo.')

pdf.subtitulo('12.8 Escasez de Recursos')
pdf.body('Los jugadores nunca tienen suficientes criaturas. Cada decision de sacrificio o captura debe sentirse como una perdida. Cada sello tiene fichas limitadas. No permitas acumulacion sin costo.')

# --- 13. HOJAS DE PERSONAJE ---
pdf.add_page()
pdf.titulo('13. HOJAS DE PERSONAJE')

personajes = [
     ("HECTOR VARGAS", "El Buscador", "Periodista. Su hija desaparecio hace catorce meses. GastoTodos sus ahorros en detectives, durmio en el coche, sigue remitiendo casos frios. No sabe hacer otra cosa.",
     [("FUE", 4), ("AGI", 5), ("VOL", 7), ("SAB", 6), ("CAR", 3)],
     ["Rastreo", "Investigacion", "Percepcion"],
     "Obsesivo: No puede abandonar una busqueda. Prefiere morir antes que soltar un hilo.",
     4, 20, "1d4+6"),

    ("SOFIA RIVERA", "La Deudora", "Cajera nocturna. Heredo las deudas hospitalarias de su madre. Su exmarido se fue. Trabaja turnos dobles y los numeros no cierran.",
     [("FUE", 3), ("AGI", 6), ("VOL", 4), ("SAB", 5), ("CAR", 7)],
     ["Persuasion", "Sigilo", "Intuicion"],
     "Desesperada: Acepta cualquier trato si promete alivio, aunque sepa que la va a destruir.",
     3, 20, "1d4+6"),

    ("MATEO CRUZ", "El Pequeno", "Once anos. Lo ultimo que recuerda es estar jugando en el patio trasero. Su madre lo llamaba para cenar. El suelo se abrio. Cuando desperto, estaba en el Velo. No entiende del todo que paso. Pero sabe que su mama lo esta esperando. Y que si el mundo se acaba, ella se acaba con el.",
     [("FUE", 2), ("AGI", 9), ("VOL", 5), ("SAB", 4), ("CAR", 5)],
     ["Sigilo", "Percepcion", "Intuicion"],
     "Dependiente: Su tamano y edad le impiden realizar ciertas acciones solo (forcejear, alcanzar objetos altos, leer mapas complejos). Los demas deben ayudarlo o el fracasa automaticamente.",
     4, 16, 9),

    ("MARTA DELGADO", "La Viuda", "Enfermera de urgencias. Su esposo murio en un choque yendo a comprarle su regalo de aniversario. Pidio el turno nocturno para no estar sola.",
     [("FUE", 5), ("AGI", 3), ("VOL", 8), ("SAB", 6), ("CAR", 3)],
     ["Medicina", "Intuicion", "Supervivencia"],
     "Aferrada: No sabe soltar a los que se han ido. Cargaria el mundo por verlo una vez mas.",
     2, 20, "1d4+6"),

    ("JULIAN ROJAS", "El Cenizo", "Ex militar, baja honorifica. Unico sobreviviente de una emboscada. Sus cuatro companeros murieron porque el ordeno avanzar por la ruta equivocada.",
     [("FUE", 8), ("AGI", 5), ("VOL", 4), ("SAB", 3), ("CAR", 5)],
     ["Pelea", "Supervivencia", "Intimidacion"],
     "Culpable: Cree que merece castigo. Camina hacia el peligro esperando que esta vez sea la definitiva.",
     5, 20, "1d4+6"),

    ("LUCIA TORRES", "La Vidente", "Estudiante de historia. Su mejor amiga se suicidio en la facultad. InvestigTe el ocultismo para contactarla. Ya no va a clases.",
     [("FUE", 3), ("AGI", 4), ("VOL", 5), ("SAB", 8), ("CAR", 5)],
     ["Ocultismo", "Investigacion", "Intuicion"],
     "Curiosidad morbidA: No reconoce el peligro cuando lo tiene enfrente. Toda puerta prohibida necesita abrirse.",
     3, 20, "1d4+6"),

    ("RAUL CASTRO", "El Padre", "Taxista nocturno. Su exesposa le nego la custodia por alcoholismo. Ocho meses sobrio, ella no contesta sus llamadas.",
     [("FUE", 6), ("AGI", 5), ("VOL", 5), ("SAB", 3), ("CAR", 6)],
     ["Conduccion", "Persuasion", "Atletismo"],
     "Rencoroso: La ira le dura mas que cualquier otra emocion. No sabe perdonar, ni siquiera a si mismo.",
     4, 20, "1d4+6"),

    ("ANA FLORES", "La Silenciada", "Ilustradora. Su expareja paso cinco anos diciendole que su arte no valia nada y quemo sus obras cuando ella lo dejo. Dibuja por encargo y no firma sus piezas.",
     [("FUE", 3), ("AGI", 7), ("VOL", 4), ("SAB", 5), ("CAR", 6)],
     ["Artesania", "Sigilo", "Percepcion"],
     "Invisible: Esta tan acostumbrada a que su dolor no importe que ya no pide ayuda. Desapareceria sin que nadie lo notara.",
     2, 20, "1d4+6"),
]

for nombre, apodo, historia, stats, habilidades, defecto, peso, hp, ap in personajes:
    pdf.add_page()
    pdf.set_font('Calibri', 'B', 14)
    pdf.cell(0, 8, f'{nombre} — "{apodo}"')
    pdf.ln(8)
    pdf.set_font('Calibri', '', 8)
    pdf.multi_cell(0, 4, historia)
    pdf.set_x(pdf.l_margin)
    pdf.ln(4)

    # Stats
    pdf.set_font('Calibri', 'B', 9)
    pdf.cell(0, 5, 'ATRIBUTOS')
    pdf.ln(5)
    pdf.set_font('Calibri', '', 9)
    for stat, val in stats:
        pdf.cell(30, 5, f'{stat}: {val}')
    pdf.ln(8)

    # Recursos
    pdf.set_font('Calibri', 'B', 9)
    pdf.cell(0, 5, 'RECURSOS')
    pdf.ln(5)
    pdf.set_font('Calibri', '', 9)
    pdf.cell(30, 5, f'HP: {hp}')
    pdf.cell(35, 5, f'ALIENTO: {ap}')
    pdf.cell(0, 5, 'CORRUPCION: 0')
    pdf.ln(8)

    # Habilidades
    pdf.set_font('Calibri', 'B', 9)
    pdf.cell(0, 5, 'HABILIDADES')
    pdf.ln(5)
    pdf.set_font('Calibri', '', 9)
    for h in habilidades:
        pdf.bullet(h)
    pdf.ln(4)

    # Defecto
    pdf.set_font('Calibri', 'B', 9)
    pdf.cell(0, 5, 'DEFECTO')
    pdf.ln(5)
    pdf.set_font('Calibri', 'I', 9)
    pdf.multi_cell(0, 4, defecto)
    pdf.set_x(pdf.l_margin)
    pdf.ln(4)

    # Equipo
    pdf.set_font('Calibri', 'B', 9)
    pdf.cell(0, 5, 'EQUIPO INICIAL')
    pdf.ln(5)
    pdf.set_font('Calibri', '', 9)
    pdf.bullet('La Key (grimorio de 80 paginas)')
    pdf.bullet('Navaja de obsidiana (1d4)')
    pdf.bullet('3 raciones, miel de melipona, encendedor, cuaderno de caza')
    pdf.bullet('1 criatura inicial (tirar 1d20 en tabla de comunes)')
    pdf.ln(10)

    # Peso de Alma (OCULTO - fuera de la vista del jugador)
    pdf.set_text_color(200, 200, 200)
    pdf.set_font('Calibri', '', 6)
    pdf.cell(0, 3, f'Peso de Alma: {peso} (solo para el DJ)')
    pdf.set_text_color(0, 0, 0)

# --- 14. AVENTURAS DE INICIO ---
pdf.add_page()
pdf.titulo('14. AVENTURAS DE INICIO')

pdf.subtitulo('14.1 El Monasterio de San Judas')
pdf.body('Tutorial para personajes con 0-5 sellos. Los personajes llegan a las ruinas del Monasterio de San Judas, en la frontera entre el territorio conocido y el Velo. Escucharon rumores de un libro poderoso.')
pdf.subsubtitulo('Escenas clave:')
pdf.bullet('La entrada: Arboles que susurran, musgo que huele a sangre seca.')
pdf.bullet('El jardin: Aluxes merodean. Aprenden a usar herramientas no letales.')
pdf.bullet('La cripta: El monje muerto con la Key en el regazo. Los muertos se levantan.')
pdf.bullet('El primer sello: Un Redcap ataca. Ensena combate y captura.')
pdf.bullet('El primer susurro: La Key ofrece ayuda para escapar. Aceptar = +2 CORRUPCION.')
pdf.body('Recompensa: Key + 1-3 criaturas comunes + equipo basico.')

pdf.subtitulo('14.2 El Pueblo de los Susurros')
pdf.body('Introduccion a la Corte de los Susurros (5-15 sellos). Un pueblo donde la gente desaparece. Un miembro de la Corte libera criaturas menores para probar a los nuevos portadores.')
pdf.subsubtitulo('Opciones:')
pdf.bullet('Aliarse con la Corte (informacion a cambio de silencio).')
pdf.bullet('Enfrentar al portador (combate contra alguien con 30+ sellos).')
pdf.bullet('Liberar criaturas y abandonar (la Corte se fija en ellos).')

pdf.subtitulo('14.3 El Primer Legendario')
pdf.body('Climax temprano (15-25 sellos). Asume que eligen Mictlan.')
pdf.body('Para llegar al noveno nivel:')
pdf.bullet('Cruzar 5 puertas que requieren sacrificios de almas.')
pdf.bullet('Sobrevivir al Silencio del nivel 9.')
pdf.bullet('Enfrentar a los guardianes de cada nivel.')
pdf.body('Al final, no encuentran a Cipactli. Encuentran a Mictlantecuhtli, que ofrece un trato: "Traeme los otros 7 legendarios y te ensenare como salir del Velo".')

# --- AP A: BESTIARIO ---
pdf.add_page()
pdf.titulo('APENDICE A. BESTIARIO COMPLETO')
pdf.body('Las 80 criaturas documentadas con su nombre, cultura, tipo elemental, rareza, voluntad y notas de comportamiento.')

bestiario = [
    ("Ahuizotl", "Azteca", "AGUA", "Rara", 40, "Mano en la cola. Ahoga en rios."),
    ("Cipactli", "Azteca", "CAOS", "Legendaria", 100, "Jefe final. Caiman primordial."),
    ("Nahual", "Azteca", "ESPIRITU", "Rara", 50, "Cambiaformas. Puede ser aliado."),
    ("Tzitzimime", "Azteca", "ESTRELLA", "Elite", 60, "Esqueleto estelar. Cae del cielo."),
    ("Xiuhcoatl", "Azteca", "FUEGO", "Elite", 55, "Serpiente de fuego solar."),
    ("Alux", "Maya", "TIERRA", "Comun", 15, "Duende de maiz. Travieso, no hostil."),
    ("Camazotz", "Maya", "BESTIA", "Legendaria", 70, "Murcielago gigante. Ciego, ecolocaliza."),
    ("Vucub-Caquix", "Maya", "PODER", "Elite", 65, "Ave vanidosa. Se distrae con brillos."),
    ("Zipacna", "Maya", "TIERRA", "Rara", 45, "Cocodrilo de montana. Entierra presas."),
    ("Kappa", "Japones", "AGUA", "Comun", 20, "Tortuga de rio. Se inclina."),
    ("Kitsune", "Japones", "FUEGO", "Rara", 35, "Zorro 9 colas. Ilusionista."),
    ("Oni", "Japones", "FUERZA", "Legendaria", 75, "Demonio 4 brazos. Jefe de Yomi."),
    ("Tengu", "Japones", "AIRE", "Rara", 40, "Cuervo humanoide. Guerrero."),
    ("Yuki-onna", "Japones", "HIELO", "Rara", 30, "Mujer de nieve. Hermosa y mortal."),
    ("Ammit", "Egipcio", "BESTIA", "Legendaria", 70, "Devoradora. Cocodrilo-leopardo-hipo."),
    ("Anubis", "Egipcio", "ESPIRITU", "Rara", 45, "Chacal. Guia de almas. No ataca 1ro."),
    ("Bastet", "Egipcio", "BESTIA", "Comun", 20, "Gato. Curiosa. Se distrae."),
    ("Centauro", "Griego", "BESTIA", "Rara", 35, "Mitad hombre, mitad caballo."),
    ("Cerbero", "Griego", "BESTIA", "Legendaria", 75, "3 cabezas. Guarda de Hades."),
    ("Chimera", "Griego", "FUEGO", "Elite", 55, "Cabra-leon-serpiente."),
    ("Fenix", "Griego", "FUEGO", "Elite", 60, "Renace. Inmortal mientras arde."),
    ("Griffin", "Griego", "AIRE", "Elite", 50, "Aguila-leon. Guardiana."),
    ("Hidra", "Griego", "AGUA", "Elite", 55, "9 cabezas. Le crecen 2 al cortar 1."),
    ("Medusa", "Griego", "PIEDRA", "Elite", 55, "Serpientes. Petrifica."),
    ("Minotauro", "Griego", "FUERZA", "Rara", 40, "Toro-hombre. Laberinto."),
    ("Draugr", "Nordico", "MUERTO", "Comun", 25, "Muerto viviente nordico."),
    ("Fenrir", "Nordico", "BESTIA", "Legendaria", 80, "Lobo devora sol. Rompe cadenas."),
    ("Jormundgander", "Nordico", "AGUA", "Elite", 65, "Serpiente mundial."),
    ("Banshee", "Celta", "ESPIRITU", "Rara", 30, "Grito mortal. Presagia muerte."),
    ("Cernunnos", "Celta", "NATURALEZA", "Elite", 60, "Dios astado. Pacifico."),
    ("Kelpie", "Celta", "AGUA", "Rara", 35, "Caballo de rio. Ahoga."),
    ("Baba Yaga", "Eslavo", "MAGIA", "Elite", 65, "Bruja. Casa con patas."),
    ("Leshy", "Eslavo", "NATURALEZA", "Rara", 40, "Espiritu del bosque."),
    ("Rusalka", "Eslavo", "AGUA", "Comun", 20, "Ninfa acuatica. Canta."),
    ("Jiangshi", "Chino", "MUERTO", "Rara", 35, "Vampiro chino. Brinca."),
    ("Long", "Chino", "AGUA", "Elite", 60, "Dragon chino. Sabio."),
    ("Qilin", "Chino", "FUEGO", "Rara", 45, "Quimera china. Justiciera."),
    ("Garuda", "Hindu", "AIRE", "Elite", 55, "Ave divina. Enemiga de nagas."),
    ("Naga", "Hindu", "AGUA", "Rara", 40, "Serpiente divina."),
    ("Rakshasa", "Hindu", "ILUSION", "Elite", 60, "Demonio cambiaformas."),
    ("Behemoth", "Abraam", "TIERRA", "Elite", 60, "Bestia colossal."),
    ("Golem", "Abraam", "TIERRA", "Rara", 50, "Barro animado."),
    ("Leviatan", "Abraam", "AGUA", "Elite", 65, "Monstruo marino."),
    ("Basilisco", "Europeo", "PIEDRA", "Rara", 45, "Gallo-serpiente."),
    ("Hombre Lobo", "Europeo", "BESTIA", "Rara", 40, "Luna llena. Plata."),
    ("Vampiro", "Europeo", "MUERTO", "Rara", 50, "Conde. Sed de sangre."),
    ("Abiku", "Yoruba", "ESPIRITU", "Comun", 20, "Nino espiritu."),
    ("Emere", "Yoruba", "ESPIRITU", "Rara", 35, "Humano-espiritu."),
    ("Iwin", "Yoruba", "NATURALEZA", "Comun", 15, "Espiritu de arbol."),
    ("Amarok", "Inuit", "BESTIA", "Rara", 40, "Lobo gigante. Caza solo."),
    ("Mahaha", "Inuit", "HUMOR", "Comun", 15, "Demonio risueno."),
    ("Qalupalik", "Inuit", "AGUA", "Rara", 30, "Sirena inuit."),
    ("Amaru", "Andino", "SERPIENTE", "Rara", 35, "Serpiente alada."),
    ("La Llorona", "Mexicano", "ESPIRITU", "Rara", 30, "Mujer que llora."),
    ("Impundulu", "Zulu", "TRUENO", "Rara", 40, "Ave del trueno."),
    ("Tikoloshe", "Zulu", "AGUA", "Comun", 15, "Duende acuatico."),
    ("Pazuzu", "Mesopotam", "VIENTO", "Elite", 60, "Demonio del viento."),
    ("Tiamat", "Mesopotam", "CAOS", "Legendaria", 85, "Diosa primordial."),
    ("Thunderbird", "NativoAm", "TRUENO", "Elite", 55, "Ave del trueno."),
    ("Wendigo", "NativoAm", "MUERTO", "Elite", 55, "Canibalismo. Cuernos."),
    ("Anansi", "Ghana", "TRAMPA", "Rara", 30, "Arana. Teje redes."),
    ("Redcap", "Britanico", "MUERTO", "Comun", 20, "Duende sombrio."),
    ("Carmilla", "Literario", "MUERTO", "Rara", 50, "Vampiresa."),
    ("Frankenstein", "Literario", "CREACION", "Rara", 50, "Criatura triste."),
    ("Nasnas", "Arabe", "MITAD", "Comun", 20, "Mitad persona."),
    ("Barong", "Bali", "BESTIA", "Elite", 60, "Leon balines."),
    ("Menehune", "Polinesio", "TIERRA", "Comun", 15, "Duende constructor."),
    ("Moo", "Polinesio", "AGUA", "Rara", 40, "Lagarto guardian."),
    ("Ponaturi", "Polinesio", "ESPIRITU", "Rara", 35, "Espiritus del mar."),
    ("Taniwha", "Polinesio", "AGUA", "Elite", 55, "Guardian acuatico."),
    ("Div", "Persa", "DEMONIO", "Rara", 45, "Demonio persa."),
    ("Peri", "Persa", "ANGEL", "Rara", 35, "Angel caido."),
    ("Simurgh", "Persa", "SABIDURIA", "Legendaria", 80, "Ave del juicio."),
    ("Aswang", "Filipino", "MUERTO", "Rara", 40, "Vampiro filipino."),
    ("Kapre", "Filipino", "NATURALEZA", "Comun", 20, "Gigante de arbol."),
    ("Tikbalang", "Filipino", "BESTIA", "Rara", 35, "Caballo humanoide."),
    ("Boiuna", "Amazonico", "AGUA", "Elite", 50, "Serpiente negra."),
    ("Curupira", "Amazonico", "NATURALEZA", "Rara", 35, "Pies al reves."),
    ("Iara", "Amazonico", "AGUA", "Comun", 20, "Sirena amazonica."),
    ("Mapinguari", "Amazonico", "BESTIA", "Elite", 60, "Perezoso gigante."),
]

# Bestiary in compact table form (2 columns)
pdf.set_font('Calibri', 'B', 7)
col_w = [28, 16, 18, 18, 10, 80]
pdf.cell(col_w[0], 5, 'Criatura', border=1, align='C')
pdf.cell(col_w[1], 5, 'Cultura', border=1, align='C')
pdf.cell(col_w[2], 5, 'Tipo', border=1, align='C')
pdf.cell(col_w[3], 5, 'Rareza', border=1, align='C')
pdf.cell(col_w[4], 5, 'VOL', border=1, align='C')
pdf.cell(col_w[5], 5, 'Notas', border=1, align='C')
pdf.ln()

pdf.set_font('Calibri', '', 6.5)
for i, (name, cult, tipo, rar, vol, notas) in enumerate(bestiario):
    pdf.cell(col_w[0], 4, name, border=1)
    pdf.cell(col_w[1], 4, cult, border=1, align='C')
    pdf.cell(col_w[2], 4, tipo, border=1, align='C')
    pdf.cell(col_w[3], 4, rar, border=1, align='C')
    pdf.cell(col_w[4], 4, str(vol), border=1, align='C')
    pdf.cell(col_w[5], 4, notas, border=1)
    pdf.ln()
    if i % 35 == 34 and i < len(bestiario) - 1:
        pdf.add_page()
        pdf.set_font('Calibri', 'B', 7)
        pdf.cell(col_w[0], 5, 'Criatura', border=1, align='C')
        pdf.cell(col_w[1], 5, 'Cultura', border=1, align='C')
        pdf.cell(col_w[2], 5, 'Tipo', border=1, align='C')
        pdf.cell(col_w[3], 5, 'Rareza', border=1, align='C')
        pdf.cell(col_w[4], 5, 'VOL', border=1, align='C')
        pdf.cell(col_w[5], 5, 'Notas', border=1, align='C')
        pdf.ln()
        pdf.set_font('Calibri', '', 6.5)

# --- AP B: TABLA DE FICHAS ---
pdf.add_page()
pdf.titulo('APENDICE B. TABLA DE FICHAS DE SELLO')
pdf.body('Fichas base de cada criatura segun su rareza. Al agotarse, la criatura reaparece furiosa. Puede ser recapturada con 1 ficha menos.')
pdf.tabla(
    ['Rareza', 'Fichas Base', 'Vida al Reaparecer', 'ALIENTO (Sellar)', 'VIDA (Invocar)'],
    [
        ['Comun', '5', '4 HP', '2', '5'],
        ['Rara', '4', '8 HP', '3', '8'],
        ['Elite', '3', '15 HP', '5', '10'],
        ['Legendaria', '2', 'Completa', '8', '15'],
        ['Companero Caido', '8 (ALIENTO total)', '4 HP (ataca)', 'N/A', '5'],
    ],
    [32, 25, 35, 30, 28]
)
pdf.ln(5)
pdf.body('Cada vez que usas a una criatura (invocar, habilidad especial), retiras 1 ficha.')
pdf.body('Al no quedar fichas, el sello se rompe y la criatura reaparece frente al portador.')
pdf.body('Si la recapturas, su nuevo maximo de fichas es el anterior - 1.')
pdf.body('Si llega a 0 fichas base, no puede ser capturada nunca mas.')
pdf.body('El sacrificio en altar quita todas las fichas de golpe. La criatura muere para siempre.')

# --- FIN ---
pdf.add_page()
pdf.ln(40)
pdf.set_font('Calibri', 'I', 9)
pdf.cell(0, 6, '"La Key siempre encuentra un nuevo portador. El ciclo se reinicia.', align='C')
pdf.ln(6)
pdf.cell(0, 6, 'Las 80 paginas esperan. Y en el fondo de Mictlan,', align='C')
pdf.ln(6)
pdf.cell(0, 6, 'Cipactli sigue hambriento."', align='C')
pdf.ln(20)
pdf.set_font('Calibri', '', 9)
pdf.cell(0, 5, 'Documento de dominio publico bajo CC0 1.0 Universal.', align='C')
pdf.ln(5)
pdf.cell(0, 5, 'Creado por Eremosisima - Julio 2026', align='C')
pdf.ln(5)
pdf.cell(0, 5, 'github.com/Eremosisima/chanequemon', align='C')

# Metadatos del PDF
pdf.set_title('Cipactli: The Lesser Key')
pdf.set_author('Eremosisima')
pdf.set_subject('Juego de Rol de Mesa - Horror Cosmico y Supervivencia - CC0 1.0 Universal')
pdf.set_creator('generar_pdf.py')

# Guardar
output_path = 'C:/Users/x/Downloads/Cipactli_The_Lesser_Key_Rol.pdf'
pdf.output(output_path)
print(f'PDF generado: {output_path}')
print(f'Paginas: {pdf.page_no()}')
