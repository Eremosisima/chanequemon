# Cipactli: The Lesser Key — Diseño de Mecánicas

## Resumen del Sistema

```
Tres recursos: VIDA | ALIENTO | SELLOS
Ninguno se recupera gratis.
Todo tiene un costo.
Todo es una decisión.
```

---

## 1. Recursos del Jugador

### Vida
- HP estándar. Al llegar a 0, mueres.
- Se recupera lentamente de forma natural.
- Fogatas del inframundo: +30% (una vez por zona).
- Sacrificar criatura: +Vida según nivel.
- Ríos del inframundo: +10% pero -5% Vida Máxima permanente.
- **Vida Máxima se reduce permanentemente cada vez que sellas una criatura.**
  El cuerpo del portador se vacía con cada alma que contiene.

### Aliento (Stamina)
- Recurso primario para invocar criaturas.
- Se recupera:
  - Naturalmente al caminar lento (+1/10 seg fuera de combate)
  - Fogatas: +50%
  - Sacrificar criatura: +Aliento según nivel
- Sin Aliento no puedes invocar (excepto usando Vida como emergencia).

### Sellos
- 80 páginas en la Key. Cada sello = un alma atrapada.
- Cada sello ocupado reduce la Vida Máxima del portador.
- No se pueden borrar sellos voluntariamente (solo sacrificando la criatura,
  lo que rompe el sello permanentemente).

---

## 2. Sistema de Invocación

### Costes

| Rango | Ejemplos | Aliento | Vida (emergencia) | Nota |
|-------|----------|---------|-------------------|------|
| Común | Alux, Kappa, Menehune | 10% | 3% | Daño 1.5x en emergencia |
| Rara | Kitsune, Oni, Ahuizotl | 35% | 8% | Daño 1.5x en emergencia |
| Élite | Medusa, Fenrir, Tiamat | 60% | 12% | Daño 1.5x en emergencia |
| Legendaria | Jefes regionales | 80% | 15% | No pueden ser invocadas en combate |
| Cipactli | — | — | — | No puede ser invocado |

### Desgaste por uso

| Invocaciones | Estado del sello | Efecto |
|-------------|------------------|--------|
| 0-3 | Intacto | Normal |
| 4-6 | Grietado | -10% HP de la criatura |
| 7-9 | Quebrado | -30% HP, puede negarse a atacar |
| 10+ | Roto | Sello destruido. Criatura escapa como Versión Despierta (+50% stats, hostil) |

- Si la criatura "muere" en combate: +3 niveles de desgaste instantáneo.
- La criatura rota aparece en el inframundo como enemigo con memoria.
  Si la re-sellas, su desgaste inicial es 3 (empieza grietada).

---

## 3. Captura

### Requisitos
1. Criatura debajo del 30% de HP.
2. Estar en combate.
3. Seleccionar "Sellar" en el menú.

### El Ritual
- Animación de 3 segundos: la Key se abre, sombra envuelve a la criatura.
- La criatura puede interrumpir el ritual si no está aturdida o ralentizada.
- Herramientas como xtabentún (aturdimiento) o red de ixtle (inmovilizar)
  aseguran el ritual.

### Fórmula de Captura
```
P_sello = baseVoluntad × saludMod × estadoMod × herramientaMod

Donde:
  baseVoluntad = Voluntad de la criatura (0-100, inverso al captureRate)
  saludMod = 1.0 + (1.0 - currentHP / maxHP) × 1.5
  estadoMod = 1.0 (normal) | 1.3 (aturdido) | 1.4 (ralentizado) | 1.5 (inmóvil)
  herramientaMod = 0.8 (sin herramienta) | 1.0 (red) | 1.2 (xtabentún + red)

Si P_sello > umbral_voluntad → éxito
Si falla → criatura se cura 20% HP y gana +50% ataque 3 turnos.
```

---

## 4. Sacrificio

### Usos del sacrificio

| Acción | Efecto | Coste |
|--------|--------|-------|
| Curar | +Vida y Aliento según nivel | Criatura se libera (sello roto) |
| Abrir puertas | Peso de alma para portales | Criatura se libera (sello roto) |
| Activar altar | Punto de respawn | Criatura se libera (sello roto) |
| Usar altar al morir | Respawn en ese altar | Criatura se libera (sello roto) |
| Distracción en sigilo | Liberas alma para crear ruido/señuelo | Criatura se libera (sello roto) |

### Peso de Alma por nivel

| Rango | Peso de alma |
|-------|-------------|
| Común | 1 |
| Rara | 3 |
| Élite | 5 |
| Legendaria | 10 (solo para el portal final) |

Cada puerta entre regiones requiere un peso de alma específico. Una puerta
que requiere 5 puede abrirse con 5 comunes, o 1 rara + 2 comunes, o 1 élite.

---

## 5. Altares de Respawn

### Reglas
- Cada región tiene 3-5 altares.
- Activar: sacrificar 1 criatura (cualquier rango).
- Usar al morir: sacrificar 1 criatura (cualquier rango).
- Sin criaturas al morir: respawn en el último altar activo y no usado.
- Sin criaturas y sin altares activos: respawn al inicio de la región.

### Diseño
- Los altares están en ubicaciones clave (antes de zonas peligrosas).
- Decidir si activar o no es el ciclo económico central.
- Al final del juego, cada altar activado es una criatura menos.
  El jugador siente el peso de sus decisiones anteriores.

---

## 6. Herramientas

### Categorías

| Tipo | Objetos |
|------|---------|
| Daño controlado | Navaja de obsidiana (sangrado), Cal viva (daño lento + baja defensa) |
| Control | Miel de abeja melipona (ralentiza), Xtabentún (aturde) |
| Sigilo | Copal (invisibilidad 1 turno), Polvo de hueso (ceguera a la criatura) |
| Distracción | Pluma de quetzal (señuelo), Espejo de humo (reflejo ilusorio) |
| Captura | Red de ixtle (inmoviliza), Semillas de cacao (diálogo con Despiertos) |

### Crafting
- Las herramientas se encuentran en el inframundo o se crean combinando
  materiales (ej: cal + miel = trampa pegajosa que daña y ralentiza).
- No hay recetas fijas — el jugador experimenta.
- Materiales se obtienen de: criaturas derrotadas, altares saqueados,
  secretos de cada región.

---

## 7. Sigilo

### Estados de alerta

| Estado | Efecto |
|--------|--------|
| Inconsciente | Criatura no te ha detectado. Puedes pasar de largo o preparar emboscada |
| Alerta | Detectó algo (ruido, luz). Busca. Si no encuentra, vuelve a inconsciente tras 10 seg |
| Hostil | Te detectó directamente. Inicia combate. Puede llamar a criaturas cercanas |
| Alarma general | Zona en alerta. Todas las criaturas cercanas entran en alerta. Dura 60 seg |

### Mecánicas de sigilo

- **Oscuridad**: pararte en sombras te hace invisible. La mayoría de criaturas
  no pueden ver en oscuridad total. Algunas sí (Camazotz, Tzitzimime).
- **Luz de la Key**: abrir la Key emite luz visible. Te delata a distancia.
- **Ruido de terreno**: barro=0, piedra=1, huesos=2, agua=3 (escala de detección).
  Correr duplica el ruido.
- **Patrones**: cada criatura tiene un patrón de patrulla, sueño, o merodeo.
  Aprendes rutas observando.
- **Criaturas únicas**: algunas (Alux, Kapre) no son hostiles por defecto —
  puedes negociar o ignorarlas.

---

## 8. Los 8 Legendarios

### Lista por región

| Región | Legendario | Peso de alma |
|--------|------------|-------------|
| Mictlan | Cipactli (jefe final, no invocable) | — |
| Xibalba | Camazotz (rey murciélago) | 10 |
| Duat | Ammit (devoradora de almas) | 10 |
| Helheim | Fenrir (lobo que devora el sol) | 10 |
| Hades | Cerbero (guardian del inframundo) | 10 |
| Yomi | Oni (señor demonio) | 10 |
| Naraka | Tiamat (caos primordial) | 10 |
| Hamistagan | Simurgh (ave del juicio) | 10 |

### Reglas especiales de los legendarios
1. No pueden ser invocados en combate.
2. No pueden ser sacrificados para curar, abrir puertas menores, ni activar altares.
3. Su sello está marcado desde el inicio con un sigilo diferente.
4. Solo pueden ser liberados en el portal final.
5. Al liberarlos en el portal, cada uno libera un fragmento de Cipactli.
6. Sin los 8, el portal no se abre. Con los 8, Cipactli emerge.

---

## 9. Combate Final contra Cipactli

Ver narrativa.md — sección "Cipactli — El Combate Final".

Resumen: no hay HP. No hay invocaciones. Es puro sigilo + herramientas + una
decisión final: entregar la Key.

---

## 10. Progresión y Flujo

```
REGIÓN 1 (tutorial, criaturas comunes)
  → Aprendes a sellar, invocar, sigilo básico
  → Primer jefe: criatura élite

REGIÓN 2-7 (cada una presenta nueva mecánica)
  → Nuevas herramientas, nuevos peligros
  → Jefe legendario al final de cada una
  → Puertas entre regiones requieren sacrificios

REGIÓN 8 (Hamistagan, la más difícil)
  → Último legendario
  → Revelación de Mictlantecuhtli

PORTAL FINAL
  → Sacrificas los 8 legendarios
  → Cipactli emerge
  → Combate final (sigilo + herramientas + sacrificio de la Key)

FINAL
  → Según decisiones del jugador
```

---

## 11. Notas de Diseño

- **No hay grinding.** Las criaturas no suben de nivel por combate. Su poder
  es fijo. Lo que cambia es cuánto decides usarlas y arriesgar su desgaste.
- **Cada criatura es única.** Si se rompe su sello, desaparece para siempre
  de tu Key (aunque puedas re-sellarla como Versión Despierta).
- **La escasez es el motor.** Nunca tienes suficientes criaturas. Siempre
  necesitas más. Pero cada nueva captura reduce tu Vida Máxima.
- **El final no es opcional.** No puedes quedarte con los 8 legendarios.
  El juego te obliga a enfrentar que los conseguiste para perderlos.
