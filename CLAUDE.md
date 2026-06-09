# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Qué es

TPO de POO (UADE): simulador de escritorio de una carrera de caballos en Java + Swing, con
persistencia en MySQL vía Hibernate/JPA. El foco de la consigna es el diseño OO: MVC, capas,
Singleton, DTOs, GRASP/SOLID. El código, los comentarios y la documentación están en español.

## Build y ejecución

Proyecto Maven (Java 17). **No hay wrapper (`mvnw`)**: se necesita `mvn` y un JDK 17 en el PATH
(en esta máquina ninguno está instalado por defecto). No hay tests: `src/test` figura en el
`.classpath` pero no existe, así que no hay nada que correr con `mvn test`.

```bash
mvn compile                 # compilar
mvn exec:java -Dexec.mainClass=com.uade.carreracaballos.Main   # correr (requiere exec-maven-plugin, no está en el pom)
```

En la práctica el proyecto se ejecuta desde Eclipse o IntelliJ (hay `.classpath`/`.project` y
`.idea/`) corriendo la clase `Main`. El `pom.xml` no declara plugin de empaquetado ni de exec,
así que `mvn` sólo compila; lanzar la app es tarea del IDE.

**Requiere MySQL corriendo** en `localhost:3306` con la base `carreracaballos`. La conexión está
hardcodeada en `src/main/resources/META-INF/persistence.xml` (usuario/clave `root`). Hibernate
crea el esquema solo (`hibernate.hbm2ddl.auto=update`), por lo que `dbCreate.sql` es sólo
referencia y está desactualizado respecto de las entidades (ver Gotchas).

## La Vista: MenuFrame (Swing)

La Vista del MVC es la GUI Swing, que habla únicamente con los controllers (recibe/usa DTOs). Se
lanza desde `Main.java`.

- `MenuFrame` (Swing) — la única vista. Ya está completamente cableada: crea jugador, lista
  caballos, selecciona jugador y caballo, y corre la carrera con animación en
  `VentanaCarrera`/`ViewPista`.

## Arquitectura por capas

El flujo es estricto y unidireccional: **Vista → Controller → Service → DAO → (MySQL)**.
Cada capa sólo conoce a la de abajo.

```
View (MenuFrame)           habla SOLO con Controllers; recibe/usa DTOs
   ↓
Controller                 convierte Model↔DTO; arma los casos de uso
   ↓
Service                    orquestación; DEVUELVE objetos de Model, no DTOs
   ↓
DAO                        persistencia JPA (un EntityManager por operación)
   ↓
Model (entidades JPA)
```

Reglas que definen el diseño (respetarlas al extender):

- **La conversión Model↔DTO vive en el Controller**, nunca en el Service ni en el DAO.
  `CaballoController.convertirADTO(...)` (caballos) y `JugadorController.construirDTO(...)`
  (jugador) son los puntos de traducción. `CarreraController.obtenerPosiciones()` también arma
  `CaballoDTO` con la misma lógica inline (duplicación conocida, pendiente de unificar). Los
  Services devuelven `Caballo`/`Jugador` del modelo a propósito.
- **La Vista nunca toca el modelo**: todos los controllers devuelven DTOs. En particular, el
  bucle de animación usa `CarreraController.obtenerPosiciones()`, que devuelve
  `List<CaballoDTO>` (no objetos del modelo).
- **Singleton**: `config/JPAUtil` centraliza el `EntityManagerFactory`. Todos los DAO piden
  `JPAUtil.getInstance().crearEntityManager()`, abren transacción, y **cierran el EM en un
  `finally`** por operación. Consecuencia: las entidades quedan *detached* al volver al service,
  por eso `actualizarJugador` usa `merge` y no `persist`.

### Controllers y su responsabilidad

- `JugadorController` — mantiene el `jugadorSeleccionado` de la sesión (estado de la "vista
  activa"); alta, selección por id, `seleccionarCaballo(int id)` y
  `procesarPuntaje(posicion)`.
- `CaballoController` — alta por tipo (`AtributoCaballo`) y listado como DTOs.
  `convertirADTO(Caballo)` es el único punto de conversión modelo→DTO para caballos.
- `CarreraController` — dueño de la `Carrera` en curso. `crearCarrera(idCaballoJugador,
  longitudPista)` arma el pool automáticamente: obtiene caballos aleatorios de la BD
  (excluyendo el del jugador, vía `CaballoService.getRandomCaballos`) y añade el caballo
  del jugador. También expone `carreraFinalizada()` para el bucle de animación de la GUI.

## Vistas de la carrera (GUI)

El flujo de `MenuFrame.iniciarCarrera()` cuando todo está listo:

1. Dialog para elegir el tamaño de pista (200–500m en pasos de 50).
2. `carreraCont.crearCarrera(idCaballoJugador, tamanioPista)` — arma el pool y crea `Carrera`.
3. Abre `VentanaCarrera` (986×458 px), que contiene un `ViewPista` (`JPanel` custom).
4. Lanza un `javax.swing.Timer` (~32 ms) que en cada tick: si la carrera no terminó, llama
   `carreraCont.avanzarInstante()`, obtiene `List<CaballoDTO>` de
   `carreraCont.obtenerPosiciones()` y llama
   `ventanaCarrera.getPista().actualizar(posis, tamanioPista, idCaballoJugador)`. Cuando
   `carreraCont.carreraFinalizada()` da true, frena el Timer, calcula el puesto y procesa el
   puntaje.
5. Al finalizar, muestra diálogo con el resultado, oculta `VentanaCarrera` y vuelve a `MenuFrame`.

`ViewPista` dibuja un fondo (`pista.png`) y hasta 6 sprites (`caballo1.png`…`caballo6.png`). La
posición X de cada caballo es proporcional a `distanciaRecorrida / longitudPista`. El caballo del
jugador se etiqueta en naranja; los demás en blanco. `CaballoDTO.distanciaRecorrida` es el campo
que alimenta esta lógica.

## Modelo de dominio y lógica de la simulación

**La regla del negocio está documentada en `logica_caballos.md` — leerlo antes de tocar el
modelo.** Resumen de lo no obvio:

`Caballo` es una clase abstracta (`@Entity`, herencia `SINGLE_TABLE`, discriminador
`tipo_caballo`) con tres subtipos: `CaballoVeloz`, `CaballoResistente`, `CaballoEquilibrado`.
Los tres comparten exactamente la misma fórmula de `avanzar()`; **sólo difieren en los rangos
aleatorios de `velocidad` y `resistencia`** que se fijan en el constructor `(String nombre)`.

En cada instante:

```
avance   = velocidad × (energia / 100)
desgaste = (100 - resistencia) / K          // K = 10
energia  = max(resistencia, energia - desgaste)   // el piso de energía ES la resistencia
```

Propiedad clave que vale conocer: con esa fórmula **todos los caballos llegan a su piso de
energía exactamente en el instante 10**, sin importar el tipo. Eso divide la carrera en fase
"burst" (1–10, los Veloces ganan en pistas cortas) y fase sostenida (11+, los Resistentes
ganan en pistas largas). El punto de equilibrio está cerca de los 300m.

Puntaje **implementado** (`JugadorController.procesarPuntaje`): 1°=100, 2°=50, resto=10, en línea
con `consigna.md` y `logica_caballos.md`.

`Carrera` (no es entidad JPA, es objeto en memoria) corre con todos los caballos del pool y marca
`EstadoCarrera.FINALIZADA` apenas uno cruza la meta. Cada carrera arma su pool releyendo caballos
frescos de la BD; como `energia` y `distanciaRecorrida` son `@Transient`, los constructores
no-arg los reinician (energía 100, distancia 0), así que no hace falta un reset manual.

## Gotchas / partes incompletas

Cosas que parecen bugs pero conviene entender antes de "arreglar":

- `Jugador.caballoSeleccionado` es `@Transient` (no se persiste). Entre sesiones, el jugador
  carga sin caballo asignado y hay que seleccionarlo de nuevo.
- `JugadorController` instancia su propio `CaballoService` además del `JugadorService`. Hay una
  nota en el código sobre si corresponde que los controllers o los services hablen entre sí.
- Esquema vs `dbCreate.sql`: las entidades generan tablas `caballo` (singular,
  `@Table(name="caballo")`) y `jugadores`; el SQL de referencia crea `caballos` (plural). Como
  `hbm2ddl.auto=update`, mandan las entidades, no el `.sql`.
- `persistence.xml` mezcla credenciales: la `jdbc.url` embebe `root:admin123` y además hay
  props `jdbc.user=root` / `jdbc.password=adminadmin123123` (tres valores distintos). Conviene
  unificarlas a las credenciales reales del MySQL local. Es config local de desarrollo.

## Diagramas

`diagramas/*.puml` (secuencia, PlantUML) y los `.mdj` de StarUML en la raíz documentan los casos
de uso y el diagrama de clases. Útiles para entender el flujo previsto antes de cambiar capas.
