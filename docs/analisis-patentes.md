# Análisis de Patentes — Chanequemon

## Patentes de Nintendo Identificadas y Cómo las Evitamos

### 1. Captura por menú en combate por turnos

**Patentes relevantes:** JP7493117, US 12,179,111, US 12,220,638

**Lo que cubren:** Apuntar en tiempo real + lanzar objeto de captura + mostrar probabilidad + alternar entre modo captura/batalla.

**Cómo lo evitamos:**
- ❌ Sin apuntado direccional
- ❌ Sin retícula/punto de mira
- ❌ Sin indicador de probabilidad visible
- ❌ Sin lanzamiento de objeto (animaciones in-place)
- ❌ Sin alternancia de modos (un solo modo de combate)
- ✅ Captura es una opción del menú de combate (prior art desde 1996)

### 2. Invocación de criaturas para combate

**Patentes relevantes:** US 12,403,397

**Lo que cubre:** Lanzar una criatura capturada como objeto virtual hacia un objetivo.

**Cómo lo evitamos:**
- ❌ Sin lanzamiento
- ❌ Sin dirección de apuntado
- ✅ La criatura aparece mediante animación de portal/círculo de invocación en posición fija
- ✅ Selección por menú, no por puntería

### 3. Monturas y desplazamiento

**Patentes relevantes:** JP7545191, US 12,246,255, US 12,409,387

**Lo que cubren:** Cambiar entre monturas terrestres/acuáticas/aéreas en pleno movimiento.

**Cómo lo evitamos:**
- ❌ Sin criaturas montables
- ✅ Solo caminar + teletransporte a waypoints
- ✅ Velocidad constante

### 4. Portales entre dimensiones

**No hay patente aplicable.** Prior art masivo: Super Mario 64 (1996), Portal (2007), The Legend of Zelda: Ocarina of Time (1998), etc.

## Plataformas: Minecraft / Roblox

**Minecraft (Paper Plugin):**
- La captura usa GUI de inventario (chest GUI), no apuntado con el cursor
- Las criaturas usan NBT tags para datos, no modelos personalizados
- Sin raycasting para captura
- Sin proyectiles para sellos de captura

**Roblox (Luau):**
- La captura usa ScreenGui con TextButtons, no Mouse.Hit ni raycasting
- Sin Beam/Projectile para captura
- Animaciones con TweenService/ParticleEmitter, no trayectorias balísticas
- Sin RemoteEvent direccional para captura

## Resumen Legal

Todo el sistema opera dentro del espacio legal de Pokémon Red/Blue (1996) — captura por menú, combate por turnos, sin apuntado en tiempo real. Nintendo no patentó ese sistema porque ya existía como prior art. Este proyecto se mantiene dentro de ese mismo espacio legal.
