# Lógica de Simulación — Carrera de Caballos

## Los tres atributos de un caballo

Cada caballo tiene exactamente tres atributos que definen su perfil:

- **velocidad:** qué tan rápido puede ir cuando está al máximo de energía.
- **resistencia:** qué tan bien aguanta el esfuerzo. Un número entre 0 y 100.
- **energia:** cuánta energía tiene en este momento. Siempre empieza en 100.

---

## Qué pasa en cada instante

La carrera avanza en pasos discretos llamados instantes. En cada instante, cada caballo hace dos cosas en orden:

**Primero avanza.** La distancia que recorre depende de su velocidad y de cuánta energía tiene en ese momento. Con energía llena avanza a tope. Con poca energía, avanza poco.

```
avance = velocidad × (energia / 100)
```

**Después se cansa.** Pierde una cantidad fija de energía por instante. Esa cantidad se deriva de su resistencia: cuanta más resistencia, menos energía pierde.

```
desgaste = (100 - resistencia) / 10
```

La energía nunca baja de cierto piso. Ese piso es exactamente el valor de `resistencia`. Un caballo con resistencia 80 nunca tendrá menos de 80 de energía.

```
energia = max(resistencia, energia - desgaste)
```

---

## La propiedad más importante del sistema

Con esta fórmula, **todos los caballos llegan a su piso de energía exactamente en 10 instantes**, sin importar el tipo. Se puede verificar matemáticamente:

```
instantes hasta el piso = (100 - resistencia) / desgaste
                        = (100 - resistencia) / ((100 - resistencia) / 10)
                        = 10
```

Esto divide cada carrera en dos fases bien diferenciadas:

- **Fase burst (instantes 1 al 10):** cada caballo está perdiendo energía activamente. Su velocidad real cae con cada instante.
- **Fase sostenida (instante 11 en adelante):** cada caballo ya llegó a su piso y corre a velocidad constante.

La velocidad constante de la fase sostenida es simplemente:

```
velocidad sostenida = velocidad × (resistencia / 100)
```

---

## Por qué cada tipo se comporta diferente

Los tres tipos difieren únicamente en los rangos de sus dos atributos configurables (`velocidad` y `resistencia`). La fórmula es idéntica para todos.

### Veloz

Tiene **velocidad alta** y **resistencia baja**. Arranca muy rápido, pero cae a un piso bajo. Su velocidad sostenida termina siendo moderada.

| | Valores |
|---|---|
| Velocidad | 8.0 – 10.0 |
| Resistencia | 35 – 45 |
| Velocidad sostenida | ~3.6 m/instante |

### Resistente

Tiene **velocidad baja** y **resistencia alta**. Arranca lento, pero su piso es muy alto. Después del instante 10 es el que más metros recorre por instante.

| | Valores |
|---|---|
| Velocidad | 4.0 – 5.5 |
| Resistencia | 78 – 88 |
| Velocidad sostenida | ~3.9 m/instante |

### Equilibrado

Tiene valores intermedios. Su velocidad inicial está por debajo del Veloz y su piso por debajo del Resistente. Nunca es el mejor en nada, pero tampoco queda muy atrás.

| | Valores |
|---|---|
| Velocidad | 5.5 – 7.0 |
| Resistencia | 52 – 65 |
| Velocidad sostenida | ~3.7 m/instante |

Dentro del mismo tipo, dos caballos siempre se comportan diferente entre sí porque sus atributos se asignan aleatoriamente dentro del rango al momento de creación.

---

## Cómo la distancia de la pista cambia quién gana

La carrera tiene una duración distinta según la longitud de la pista. Eso cambia cuánto peso tiene la fase burst vs la fase sostenida.

En una **pista corta (200m)**, la carrera termina antes de que las diferencias de piso importen demasiado. El Veloz aprovecha su burst inicial y gana con más frecuencia.

En una **pista larga (500m)**, ambos caballos pasan la mayor parte de la carrera en fase sostenida. Ahí el Resistente tiene ventaja porque su velocidad sostenida es mayor.

El punto de equilibrio está alrededor de los **300m**, donde los dos tipos tienen chances similares.

| Distancia | Quién tiene ventaja |
|---|---|
| 200m | Veloz |
| 300m | Prácticamente empatados |
| 500m | Resistente |

Esto le da valor estratégico a la elección del caballo: el jugador que elige bien según la distancia de la pista tiene una ventaja real.

---

## Caballos y carreras

Los caballos se crean en el sistema especificando un tipo (Veloz, Resistente o Equilibrado) y un nombre. Sus atributos se fijan aleatoriamente dentro del rango de su tipo en el momento de creación y no cambian después.

En cada carrera participan **todos los caballos registrados en el sistema**. El jugador elige uno como propio; el resto corre de forma automática. Al terminar la carrera, los caballos vuelven a su estado inicial (energía 100, distancia 0) para que puedan correr de nuevo.

---

## Sistema de puntaje

Al terminar cada carrera, el jugador suma puntos según la posición de su caballo:

| Posición | Puntos |
|---|---|
| 1° lugar | 30 |
| 2° lugar | 20 |
| 3° en adelante | 10 |

El puntaje es acumulado a lo largo de todas las carreras del jugador.

---

## Resumen de la lógica completa

```
Al crear un caballo:
  → asignar velocidad y resistencia aleatorios según el tipo
  → energia inicial = 100

Al iniciar una carrera:
  → resetear energia a 100 y distancia a 0 en todos los caballos
  → el jugador elige uno; el resto corre automáticamente

En cada instante:
  → para cada caballo:
      avance   = velocidad × (energia / 100)
      desgaste = (100 - resistencia) / 10
      energia  = max(resistencia, energia - desgaste)
  → si algún caballo llegó a la meta → terminar la carrera

Al terminar:
  → determinar posiciones por distancia recorrida
  → asignar puntaje al jugador según la posición de su caballo
```
