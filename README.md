# Bella's Heart

![Pantalla de inicio de Bella's Heart](docs/screenshots/inicio.jpg)

**Bella's Heart** es una novela visual narrativa desarrollada como Trabajo Fin de Grado. El jugador acompaña a Bella y David, una pareja que acaba de mudarse junta, y sus decisiones van moldeando la relación. El objetivo del juego es concienciar sobre la violencia de género: ayudar a reconocer señales de una relación tóxica y dar a conocer los recursos de ayuda disponibles.

Está desarrollado en Java con [libGDX](https://libgdx.com/) y usa una base de datos SQLite local.

## Características

- **Historias por capítulos y escenas**, con personajes, fondos y diálogos ramificados.
- **Decisiones con consecuencias**: cada opción suma puntos emocionales y de relación. Con ellos se calcula el estado psicológico de Bella (`STABLE` → `DISTRESSED`) y el de la relación (`CONFIDENT` → `CRITICAL`), y de ellos depende qué capítulos se desbloquean.
- **Varias partidas por jugador**, que se pueden guardar y retomar desde el capítulo en el que se quedaron.
- **Historial** de las partidas y de las decisiones tomadas.
- **Foro de noticias** sobre violencia de género y **recursos de ayuda** (016, Fundación Ana Bella…).
- **Tutorial** guiado y música de fondo.
- **Panel de administración**: gestión de jugadores, noticias, historias, capítulos y escenas, con un editor visual de decisiones.
- Cuentas con roles (jugador / administrador) y contraseñas cifradas con BCrypt.

## Capturas

### Acceso

<table>
  <tr>
    <td><img src="docs/screenshots/login.jpg" alt="Inicio de sesión"></td>
    <td><img src="docs/screenshots/registro.jpg" alt="Registro"></td>
  </tr>
  <tr>
    <td align="center">Inicio de sesión</td>
    <td align="center">Registro</td>
  </tr>
</table>

### Jugador

<table>
  <tr>
    <td><img src="docs/screenshots/menu-jugador.jpg" alt="Menú principal"></td>
    <td><img src="docs/screenshots/tutorial.jpg" alt="Tutorial"></td>
  </tr>
  <tr>
    <td align="center">Menú principal</td>
    <td align="center">Tutorial</td>
  </tr>
  <tr>
    <td><img src="docs/screenshots/nueva-partida.jpg" alt="Elegir historia"></td>
    <td><img src="docs/screenshots/detalle-historia.jpg" alt="Crear partida"></td>
  </tr>
  <tr>
    <td align="center">Nueva partida: elegir historia</td>
    <td align="center">Nueva partida: crear partida</td>
  </tr>
  <tr>
    <td><img src="docs/screenshots/cargar-partida.jpg" alt="Cargar partida"></td>
    <td><img src="docs/screenshots/capitulos.jpg" alt="Capítulos"></td>
  </tr>
  <tr>
    <td align="center">Cargar partida</td>
    <td align="center">Catálogo de capítulos</td>
  </tr>
  <tr>
    <td colspan="2"><img src="docs/screenshots/escena.jpg" alt="Escena de juego"></td>
  </tr>
  <tr>
    <td colspan="2" align="center">Escena de juego</td>
  </tr>
  <tr>
    <td><img src="docs/screenshots/foro.jpg" alt="Foro de noticias"></td>
    <td><img src="docs/screenshots/recursos.jpg" alt="Recursos de ayuda"></td>
  </tr>
  <tr>
    <td align="center">Foro de noticias</td>
    <td align="center">Recursos de ayuda</td>
  </tr>
  <tr>
    <td colspan="2"><img src="docs/screenshots/perfil.jpg" alt="Perfil"></td>
  </tr>
  <tr>
    <td colspan="2" align="center">Perfil</td>
  </tr>
</table>

### Administrador

<table>
  <tr>
    <td><img src="docs/screenshots/admin-menu.jpg" alt="Menú de administración"></td>
    <td><img src="docs/screenshots/admin-foro.jpg" alt="Gestión de noticias"></td>
  </tr>
  <tr>
    <td align="center">Menú de administración</td>
    <td align="center">Gestión de noticias</td>
  </tr>
  <tr>
    <td><img src="docs/screenshots/admin-historias.jpg" alt="Gestión de historias"></td>
    <td><img src="docs/screenshots/admin-detalle-historia.jpg" alt="Detalle de historia"></td>
  </tr>
  <tr>
    <td align="center">Gestión de historias</td>
    <td align="center">Detalle de historia y capítulos</td>
  </tr>
  <tr>
    <td colspan="2"><img src="docs/screenshots/admin-editor-visual.jpg" alt="Editor visual de decisiones"></td>
  </tr>
  <tr>
    <td colspan="2" align="center">Editor visual de decisiones de una escena</td>
  </tr>
</table>

## Tecnologías

- Java 11+ y [libGDX](https://libgdx.com/) 1.12.1 (backend de escritorio LWJGL3)
- SQLite (`sqlite-jdbc`) con un pequeño ORM propio basado en anotaciones (`@Table`, `@Column`)
- jBCrypt para el cifrado de contraseñas
- JUnit 4, Mockito y JaCoCo para los tests
- Gradle y [Construo](https://github.com/fourlastor-alexandria/construo) para empaquetar el juego por plataforma

## Estructura del proyecto

```
core/      Lógica del juego compartida
  model/     Entidades (User, Player, Game, History, Chapter, Scene, Decision…)
  manager/   Acceso a datos y lógica de negocio
  screens/   Pantallas: General (inicio, login, registro), Player y Admin
  utils/     Base de datos, recursos, estilos de la interfaz…
lwjgl3/    Lanzador de escritorio
android/   Lanzador de Android
assets/    Imágenes, fuentes, música, base de datos inicial (game.db) y datos (data.sql)
```

## Cómo ejecutarlo

Requisitos: JDK 11 o superior.

1. El juego lee la base de datos de `%APPDATA%\BellasHeart\game.db`. La primera vez, copia la base de datos inicial del repositorio:

   ```bat
   mkdir "%APPDATA%\BellasHeart"
   copy assets\game.db "%APPDATA%\BellasHeart\game.db"
   ```

2. Genera el jar ejecutable:

   ```bash
   gradlew.bat lwjgl3:jar
   ```

3. Lánzalo desde la raíz del repositorio, ya que las rutas de los recursos empiezan por `assets/`:

   ```bash
   java -jar lwjgl3/build/libs/TFG-1.0.0.jar
   ```

### Otras tareas de Gradle

- `core:test`: ejecuta los tests unitarios y genera el informe de cobertura de JaCoCo en `core/build/reports/jacoco/test/html`.
- `lwjgl3:packageWinX64` (también `packageLinuxX64`, `packageMacM1` y `packageMacX64`): empaqueta el juego con un JDK incluido mediante Construo.
- `clean`: elimina las carpetas `build`.
