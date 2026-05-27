# Trabajo Práctico Obligatorio

## Carrera de Caballos

El equipo de analistas ha finalizado el relevamiento para la confección de una
aplicación de escritorio en Java que permita simular una carrera de caballos.

Se solicita al equipo de desarrollo que diseñe e implemente el sistema relevado. A
continuación se detallan los requerimientos funcionales y las pautas de entrega.

## Descripción general

La aplicación permitirá que un usuario cree un jugador, seleccione un caballo entre
varios disponibles e inicie una carrera en una pista.

Cada caballo tendrá características propias que influirán en su desempeño durante la
carrera. El avance de cada caballo deberá visualizarse en pantalla a medida que
transcurre la competencia.

El objetivo será determinar qué caballo gana la carrera según su velocidad, su
resistencia y la distancia total de la pista.

## Requerimientos funcionales

### 1. Jugador

El sistema deberá permitir crear un jugador.

De cada jugador se conoce:

- Nombre
- mail
- puntaje acumulado
- caballo seleccionado

El jugador podrá participar en carreras y acumular puntos según el resultado obtenido.

### 2. Caballos

El sistema contará con varios caballos disponibles para competir. Cada caballo
deberá tener:


- nombre
- velocidad base
- resistencia
- energía actual
- distancia recorrida

Cada caballo tendrá distintas habilidades o atributos. Por ejemplo, algunos pueden
ser:

- más veloces al comienzo
- más resistentes
- más equilibrados

Durante la carrera, los caballos avanzarán según sus atributos.

### 3. Carrera

La carrera se desarrollará sobre una pista con una distancia determinada.

Al iniciar la carrera:

- participarán varios caballos
- uno de ellos será el caballo seleccionado por el jugador
- el resto podrán ser caballos controlados automáticamente por el sistema

La carrera finalizará cuando uno de los caballos alcance o supere la distancia total de
la pista.

### 4. Avance de los caballos

En cada instante de la carrera, cada caballo avanzará una determinada cantidad de
metros en función de:

- su velocidad
- su energía o resistencia
- la distancia restante

A medida que el caballo corre, su energía podrá disminuir. Esto puede impactar en su
desempeño posterior.

No se busca una simulación realista, sino una lógica simple y consistente orientada a
objetos.


### 5. Puntaje

El jugador obtendrá puntos de acuerdo con el resultado de la carrera:

- si su caballo gana: 100 puntos
- si sale segundo: 50 puntos
- si participa: 10 puntos

El sistema deberá permitir visualizar el puntaje acumulado del jugador.

## Interfaz gráfica

La aplicación deberá desarrollarse con **Java Swing**.

Como mínimo, la interfaz deberá permitir:

- ingresar el nombre del jugador
- mostrar la lista de caballos disponibles
- seleccionar un caballo
- iniciar una carrera
- visualizar el avance de los caballos en pantalla
- informar el ganador
- mostrar el puntaje del jugador

## Visualización de la carrera

La carrera deberá verse gráficamente en una ventana.

Cada caballo podrá representarse mediante:

- una imagen, o
- un JLabel con texto, o
- una figura simple

Los caballos deberán avanzar horizontalmente sobre la pista hasta llegar a la meta.

No se exige una interfaz compleja ni estética avanzada, pero sí que el movimiento
pueda visualizarse claramente.

## Consideraciones importantes

- La carrera puede implementarse con una animación simple utilizando Timer de
    Swing.


- No se requiere un videojuego completo, sino una simulación visual sencilla.
- El foco del trabajo está en el diseño orientado a objetos, la correcta separación
    en capas y la aplicación de los conceptos vistos en clase.

# Se solicita

## Fase A: Diagrama de clases y de secuencia

Realizar en **StarUML** el diagrama de clases y de secuencia del sistema. Deberán
modelarse correctamente:

- atributos
- métodos
- relaciones entre clases
- flujo principal y secundarios del sistema

## Fase B: Código del negocio

Implementar en Java la lógica del sistema. Se deberá implementar:

- creación de jugador
- selección de caballo
- creación de carrera
- avance de caballos
- determinación del ganador
- asignación de puntaje

## Entrega final: Interfaz gráfica

Implementar la interfaz gráfica en Swing, conectándola con el negocio desarrollado
en la Fase B.

La interfaz deberá respetar el patrón **MVC**.

# Conceptos obligatorios a aplicar

## MVC

El sistema deberá separarse en:

- **Modelo** : clases del dominio


- **Vista** : interfaz gráfica en Swing
- **Controlador** : coordinación entre vista y modelo

## Singleton

Se deberá utilizar al menos un **Singleton**. Por ejemplo, puede existir una clase
SistemaCarreras o AdministradorJuego que centralice:

- los jugadores
- los caballos
- las carreras realizadas

## DTO

Se deberán utilizar **DTOs** para intercambiar información entre la vista y el
controlador.

## GRASP y SOLID

Se evaluará la correcta aplicación de los conceptos vistos en clase, especialmente:

- Controller
- Information Expert
- Low Coupling
- High Cohesion
- Responsabilidad única
- Abierto/cerrado
- Sustitución de Liskov

# Pautas para la entrega

Todas las entregas serán digitales.

Se deberá incluir:

- proyecto Java completo
- diagrama de clases
- archivo con número de grupo e integrantes
- número de fase entregada

Las entregas deben realizarse en un único archivo comprimido (.zip o .rar).


# Fases de entrega

**Fase A:** diagrama de clases y de secuencia

**Fase B:** código del negocio funcionando con controlador y entidades

**Final:** interfaz gráfica que utilice el negocio entregado en la Fase B

# Fechas de entrega

Serán provistas en **clase y** en el **cronograma.**