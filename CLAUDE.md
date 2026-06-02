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

## Dos vistas, mismo negocio

Hay **dos frontends intercambiables** que actúan como la Vista del MVC y hablan únicamente con
los controllers. Se elige cuál corre en `Main.java` comentando/descomentando una línea:

- `MenuFrame` (Swing) — la GUI, es la que arranca por defecto.
- `CLI` — frontend de texto con animación ASCII de la carrera. **Es el camino más completo y
  funcional**; sirve como referencia de cómo orquestar los controllers de punta a punta
  (crear jugador → elegir caballo → correr → puntaje). La GUI está parcialmente cableada.

## Arquitectura por capas

El flujo es estricto y unidireccional: **Vista → Controller → Service → DAO → (MySQL)**.
Cada capa sólo conoce a la de abajo.

```
View (MenuFrame / CLI)     habla SOLO con Controllers; recibe/usa DTOs
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
  `CaballoController.convertirADTO(...)` y `JugadorController.construirDTO(...)` son los únicos
  puntos de traducción. Los Services devuelven `Caballo`/`Jugador` del modelo a propósito.
- **La Vista nunca toca el modelo** salvo una excepción documentada: ningún controller devuelve
  objetos `Caballo` del modelo, así que la `CLI` reconstruye instancias de `Caballo` desde los
  DTOs (`caballoDesdeDTO`) para armar el pool de la carrera. Instanciar participantes no se
  considera lógica de negocio; el avance, el ganador y el puntaje siguen ocurriendo en los
  controllers.
- **Singleton**: `config/JPAUtil` centraliza el `EntityManagerFactory`. Todos los DAO piden
  `JPAUtil.getInstance().crearEntityManager()`, abren transacción, y **cierran el EM en un
  `finally`** por operación. Consecuencia: las entidades quedan *detached* al volver al service,
  por eso `actualizarJugador` usa `merge` y no `persist`.

### Controllers y su responsabilidad

- `JugadorController` — mantiene el `jugadorSeleccionado` de la sesión (estado de la "vista
  activa"); alta, selección por id o por nombre+mail, y `procesarPuntaje(posicion)`.
- `CaballoController` — alta por tipo (`AtributoCaballo`) y listado como DTOs.
- `CarreraController` — dueño de la `Carrera` en curso: `crearCarrera`, `iniciarCarrera`,
  `avanzarInstante`, `calcularPuesto` (ordena por distancia), `obtenerEstado`. La GUI lo crea
  pero todavía no lo alimenta con un pool (ver Gotchas).

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

Puntaje **implementado** (`JugadorController.procesarPuntaje`): 1°=30, 2°=20, resto=10.
Ojo: `consigna.md` pide 100/50/10 — la implementación sigue a `logica_caballos.md`, no a la
consigna.

`Carrera` (no es entidad JPA, es objeto en memoria) corre con todos los caballos del pool y marca
`EstadoCarrera.FINALIZADA` apenas uno cruza la meta. `Caballo.descansar()` resetea energía a 100
y distancia a 0 para volver a correr.

## Gotchas / partes incompletas

Cosas que parecen bugs pero conviene entender antes de "arreglar":

- **GUI a medio cablear** (`MenuFrame`): `seleccionarCaballo()` no llama al controller (la línea
  está comentada); `iniciarCarrera()` invoca `carreraCont.iniciarCarrera()` sin haber creado la
  carrera ni armado el pool; `seleccionarJugador()` lee nombre y mail de la **columna 0 (el ID)**.
  La CLI no tiene estos problemas.
- `JugadorController.seleccionarCaballo(int id)` es un stub (cuerpo comentado); existe una nota
  en el código sobre la duda de diseño "¿los controllers o los services deberían hablar entre sí?".
- `Jugador.caballoSeleccionado` es `@Transient` (no se persiste). `JugadorDTO.caballoSeleccionadoId`
  nunca se setea en el constructor, así que arranca en `0`, pero `MenuFrame.iniciarCarrera` lo
  compara contra `-1`: esa guarda nunca se cumple.
- Esquema vs `dbCreate.sql`: las entidades generan tablas `caballo` (singular,
  `@Table(name="caballo")`) y `jugadores`; el SQL de referencia crea `caballos` (plural). Como
  `hbm2ddl.auto=update`, mandan las entidades, no el `.sql`.
- `persistence.xml` mezcla credenciales: la `jdbc.url` embebe `root:admin123` y además hay
  props `user`/`password` con `root`/`root`. Es config local de desarrollo.

## Diagramas

`diagramas/*.puml` (secuencia, PlantUML) y los `.mdj` de StarUML en la raíz documentan los casos
de uso y el diagrama de clases. Útiles para entender el flujo previsto antes de cambiar capas.
