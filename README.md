# SUDO.ku - Juego de Sudoku Premium 🧩

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-GUI-007396?style=for-the-badge&logo=java)
![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
![FlatLaf](https://img.shields.io/badge/FlatDarkLaf-Premium-282C34?style=for-the-badge)

**SUDO.ku** es una aplicación de escritorio avanzada desarrollada en Java (Swing) que lleva el clásico juego de Sudoku a un nivel superior. Diseñada bajo un enfoque arquitectónico robusto (Patrón Modelo-Vista-Controlador), ofrece una experiencia visual moderna, fluida y altamente inmersiva gracias a su tema oscuro nativo y mecánicas de juego competitivas.

---

## 🚀 Características Principales

* **Diseño Visual Premium**: Interfaz gráfica moderna basada en la librería **FlatDarkLaf**, con paletas de colores cuidadosamente seleccionadas (escala de grises azulados, acentos vibrantes e iconos escalados).
* **Navegación Intuitiva**: Soporte completo para interacción con ratón y teclado (flechas de dirección con desplazamiento cíclico envolvente y teclado numérico).
* **Temporizador en Tiempo Real**: Registro preciso del tiempo invertido en cada partida para competir en las tablas de clasificación.
* **Sistema de Pistas Fijas**: Las casillas originales generadas por el algoritmo quedan bloqueadas visual y lógicamente para evitar modificaciones accidentales.

---

## 🎮 Modos de Juego

SUDO.ku ofrece múltiples niveles adaptados a todo tipo de jugadores, ajustando dinámicamente la cantidad de pistas iniciales en el tablero:

| Modo | Huecos Vacíos | Descripción |
| :--- | :---: | :--- |
| 🟢 **Fácil** | 30 | Ideal para principiantes o partidas rápidas y relajadas. |
| 🟡 **Medio** | 45 | Un desafío equilibrado que requiere aplicar lógica intermedia. |
| 🔴 **Difícil** | 60 | Diseñado para expertos que dominan técnicas avanzadas de resolución. |
| 💀 **Hardcore** | 65 | **¡Desafío Extremo con 3 Vidas!** Cada error resta un corazón. Si llegas a 0 vidas, se declara *Game Over* y el tablero se bloquea. |

---

## 🛠️ Arquitectura y Estructura del Código

El proyecto cumple de forma estricta con las especificaciones académicas y profesionales más exigentes, dividiendo su lógica en paquetes altamente cohesivos:

```
com.sudoku
 ├── modelo
 │    ├── Sudoku.java             # Matriz activa del tablero, celdas fijas y validación de reglas
 │    ├── GeneradorSudoku.java    # Algoritmo de generación (Bloques diagonales + Backtracking)
 │    ├── GestorPuntuaciones.java # Conexión a BBDD y sentencias SQL seguras (PreparedStatement)
 │    └── RegistroPuntuacion.java # Entidad DTO para encapsular los datos de las partidas
 ├── vista
 │    ├── VentanaPrincipal.java   # Contenedor central y orquestador de vistas (CardLayout)
 │    ├── SudokuGUI.java          # Interfaz de juego, temporizador, eventos de teclado y modo Hardcore
 │    ├── PanelMenuPrincipal.java # Menú de inicio estilizado con botones redondeados
 │    ├── PanelTutorial.java      # Guía interactiva con reglas y explicación de modos
 │    ├── PanelRanking.java       # Tabla de clasificación Top 10 con renderizado de medallas
 │    └── PanelRegistroVictoria.java # Formulario de felicitación y guardado de puntuación
 └── principal
      └── JuegoSudoku.java        # Punto de entrada (main) y globales de configuración
```

---

## ⚙️ Modo Debug (Depuración y Pruebas)

Para facilitar la labor de revisión, corrección y evaluación, el proyecto incorpora un **Modo Debug** configurable desde la clase principal.

> [!NOTE]
> **¿Cómo activar/desactivar el Modo Debug?**
> En el archivo `com.sudoku.principal.JuegoSudoku.java`, localiza la constante:
> ```java
> public static final boolean MODO_DEBUG = false; // Cambiar a true para activar
> ```

### Beneficios del Modo Debug Activo (`true`):
1. **Dificultad especial "Prueba"**: Habilita dinámicamente en el menú desplegable de la interfaz la opción de dificultad `Prueba`.
2. **Victoria Instantánea**: Este modo genera un tablero completamente resuelto con **un único hueco vacío**.
3. **Evaluación Ágil**: Permite al profesor o evaluador introducir el último número y probar inmediatamente el flujo completo de victoria, reproducción de audio, detención del temporizador, registro de nombre y guardado en la base de datos sin necesidad de resolver un Sudoku entero.

Si el modo se establece en `false`, la opción "Prueba" desaparece por completo de la interfaz, dejando el juego en su estado final de producción.

---

## 🔒 Conexión Segura a Base de Datos (Evaluación Docente)

La tabla de clasificación (Ranking Top 10) persiste sus datos en un servidor MySQL externo. Para garantizar que el profesor pueda ejecutar y evaluar la aplicación de forma inmediata sin necesidad de configurar bases de datos locales, el proyecto incluye un archivo de configuración preconfigurado (`db.properties`).

> [!IMPORTANT]
> **Garantía de Seguridad y Aislamiento (Principio de Menor Privilegio)**
> Las credenciales suministradas en el archivo `db.properties` están asociadas a un usuario de base de datos específico creado exclusivamente para la evaluación. 
>
> Este usuario cuenta con **permisos estrictamente restringidos** a nivel de servidor:
> * ✅ **Permitido (`SELECT`, `INSERT`)**: Únicamente tiene capacidad para consultar el Top 10 y registrar nuevas victorias en la tabla `puntuaciones`.
> * ❌ **Bloqueado (`DELETE`, `UPDATE`, `DROP`, `ALTER`, `GRANT`)**: El usuario no tiene permisos para eliminar registros, modificar estructuras de tablas ni acceder a otras bases de datos del servidor.

Gracias a esta arquitectura de seguridad blindada, **el archivo proporcionado es 100% válido y seguro para su distribución y prueba**, garantizando que se pueda experimentar libremente la aplicación sin riesgo de vulnerar la integridad de la base de datos ni exponer información sensible.

---

## 🎯 Instrucciones de Ejecución

1. Asegúrate de contar con **Java 11 o superior** instalado en tu sistema.
2. Abre el proyecto en tu IDE favorito (IntelliJ IDEA, Eclipse, NetBeans, VS Code).
3. Asegúrate de que las dependencias de Maven (`FlatLaf`, `MySQL Connector`) estén correctamente cargadas en el `pom.xml`.
4. Ejecuta el archivo principal: `com.sudoku.principal.JuegoSudoku.java`.
5. ¡Disfruta de la partida! 🎉