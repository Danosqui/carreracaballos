package com.uade.carreracaballos;

import com.uade.carreracaballos.controller.CaballoController;
import com.uade.carreracaballos.controller.CarreraController;
import com.uade.carreracaballos.controller.JugadorController;
import com.uade.carreracaballos.dto.CaballoDTO;
import com.uade.carreracaballos.dto.CaballoEquilibradoDTO;
import com.uade.carreracaballos.dto.CaballoResistenteDTO;
import com.uade.carreracaballos.dto.CaballoVelozDTO;
import com.uade.carreracaballos.dto.JugadorDTO;
import com.uade.carreracaballos.model.AtributoCaballo;
import com.uade.carreracaballos.model.Caballo;
import com.uade.carreracaballos.model.CaballoEquilibrado;
import com.uade.carreracaballos.model.CaballoResistente;
import com.uade.carreracaballos.model.CaballoVeloz;
import com.uade.carreracaballos.model.EstadoCarrera;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Aplicacion CLI para probar el funcionamiento completo de "Carrera de Caballos".
 *
 * Actua como la VISTA del patron MVC: orquesta exclusivamente los controllers
 * ({@link JugadorController}, {@link CaballoController}, {@link CarreraController}).
 * Toda la logica de negocio, las reglas de la carrera y la persistencia (MySQL)
 * viven del controller para abajo (services / model / DAO).
 *
 * Unica salvedad: como ningun controller devuelve objetos {@link Caballo} del modelo
 * a la vista, la CLI instancia los caballos participantes para armar el pool y se los
 * entrega a los controllers. Instanciar participantes no es logica de negocio: el avance,
 * el ganador, el puntaje y la persistencia siguen ocurriendo dentro de los controllers.
 *
 * Requiere MySQL corriendo y las tablas creadas (ver dbCreate.sql / db.properties).
 */
public class Main {

    // ----- Parametros de la simulacion -----
    private static final int   ANCHO_BARRA   = 40;     // ancho visual de la pista en caracteres
    private static final int   MAX_INSTANTES = 500;    // tope de seguridad para el bucle de la carrera
    private static final long  PAUSA_FRAME_MS = 120;   // pausa entre instantes para poder ver el avance

    // ----- Controllers (la CLI solo habla con estos) -----
    private final JugadorController jugadorController = new JugadorController();
    private final CaballoController caballoController = new CaballoController();
    private final CarreraController carreraController = new CarreraController();

    private final Scanner scanner = new Scanner(System.in);

    // ----- Estado de la sesion -----
    private boolean hayJugador = false;
    private List<Caballo> pool = new ArrayList<>();   // caballos participantes de la corrida actual
    private int indiceSeleccionado = -1;              // indice del caballo elegido por el jugador

    public static void main(String[] args) {
        new Main().ejecutar();
    }

    private void ejecutar() {
        System.out.println("==================================================");
        System.out.println("        CARRERA DE CABALLOS  -  CLI de prueba    ");
        System.out.println("==================================================");
        System.out.println("(Requiere MySQL disponible: la persistencia pasa por los DAOs)\n");

        boolean salir = false;
        while (!salir) {
            // Menu unico (como la ventana principal de una app Swing): el jugador y el
            // caballo se eligen desde aca, y "Iniciar carrera" queda bloqueada hasta tener ambos.
            mostrarMenu();
            int opcion = leerEntero("Elegi una opcion: ");
            System.out.println();
            try {
                switch (opcion) {
                    case 1: seleccionarJugador(); break;
                    case 2: seleccionarCaballo(); break;
                    case 3: correrCarrera();      break;
                    case 0: salir = true;         break;
                    default: System.out.println("Opcion invalida.\n");
                }
            } catch (RuntimeException e) {
                // Errores tipicos: conexion a la DB caida, jugador no encontrado, etc.
                System.out.println(">> Error: " + e.getMessage() + "\n");
            }
        }

        System.out.println("Hasta luego!");
        scanner.close();
    }

    private void mostrarMenu() {
        String jugadorTxt = hayJugador
                ? jugadorController.getJugadorSeleccionado().getNombre()
                + " (puntaje: " + jugadorController.getJugadorSeleccionado().getPuntajeAcumulado() + ")"
                : "ninguno";
        String caballoTxt = indiceSeleccionado >= 0
                ? pool.get(indiceSeleccionado).getNombre()
                : "ninguno";

        System.out.println("--------------------------------------------------");
        System.out.println(" Jugador activo : " + jugadorTxt);
        System.out.println(" Tu caballo     : " + caballoTxt);
        System.out.println("--------------------------------------------------");
        System.out.println(" 1) Seleccionar jugador");
        System.out.println(" 2) Seleccionar caballo");
        System.out.println(" 3) Iniciar carrera" + (puedeCorrer() ? "" : "  (elegi jugador y caballo primero)"));
        System.out.println(" 0) Salir");
    }

    /** La carrera solo arranca con jugador activo y caballo elegido. */
    private boolean puedeCorrer() {
        return hayJugador && indiceSeleccionado >= 0;
    }

    // ------------------------------------------------------------------
    // Pantalla de jugador (seleccionar; crear vive dentro de esta pantalla)
    // ------------------------------------------------------------------
    /**
     * Accion de menu "Seleccionar jugador": muestra la lista de jugadores guardados en la DB
     * para elegir uno, y ofrece "crear nuevo" dentro de la misma pantalla.
     */
    private void seleccionarJugador() {
        List<JugadorDTO> jugadores = jugadorController.listarJugadores();

        System.out.println("==================================================");
        System.out.println(" SELECCIONAR JUGADOR");
        System.out.println("==================================================");
        if (jugadores.isEmpty()) {
            System.out.println(" (no hay jugadores guardados todavia)");
        } else {
            for (int i = 0; i < jugadores.size(); i++) {
                JugadorDTO j = jugadores.get(i);
                System.out.printf("  %d) %-15s  %-30s  puntaje=%d%n",
                        i + 1, j.getNombre(), j.getMail(), j.getPnutaje());
            }
        }
        System.out.println(" --------------------------------------------------");
        System.out.println("  C) Crear nuevo jugador");
        System.out.println("  0) Volver al menu");
        System.out.println();

        String entrada = leerTexto("Elegi un jugador (numero) o una opcion: ").trim();

        if (entrada.equalsIgnoreCase("0")) {
            return;
        }
        if (entrada.equalsIgnoreCase("C")) {
            crearJugador();
            return;
        }

        // Caso: el usuario tipeo el numero de un jugador existente.
        try {
            int idx = Integer.parseInt(entrada) - 1;
            if (idx < 0 || idx >= jugadores.size()) {
                System.out.println(">> Numero fuera de rango.\n");
                return;
            }
            JugadorDTO elegido = jugadores.get(idx);
            jugadorController.seleccionarJugador(elegido.getNombre(), elegido.getMail());
            hayJugador = true;
            System.out.println(">> Jugador '" + elegido.getNombre() + "' cargado desde la DB.\n");
        } catch (NumberFormatException e) {
            System.out.println(">> Opcion invalida.\n");
        }
    }

    /** Alta de jugador (accion interna de la pantalla de seleccion). Persiste y lo deja activo. */
    private void crearJugador() {
        String nombre = leerTexto("Nombre del jugador: ");
        String mail   = leerTexto("Mail del jugador  : ");

        try {
            // Por si ya existia: lo recuperamos en vez de duplicarlo.
            jugadorController.seleccionarJugador(nombre, mail);
            System.out.println(">> Ese jugador ya existia: se cargo desde la DB.");
        } catch (RuntimeException noExiste) {
            jugadorController.nuevoJugador(nombre, mail);
            jugadorController.seleccionarJugador(nombre, mail);
            System.out.println(">> Jugador creado y guardado.");
        }

        hayJugador = true;
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Caballos
    // ------------------------------------------------------------------
    /**
     * Accion de menu "Seleccionar caballo": lista todos los caballos guardados en la DB,
     * permite crear uno nuevo (solo tipo y nombre) y deja elegir uno como el del jugador.
     * El pool de la carrera se arma con todos los caballos de esa lista.
     */
    private void seleccionarCaballo() {
        pool = poolDesdeDB();

        System.out.println("==================================================");
        System.out.println(" SELECCIONAR CABALLO");
        System.out.println("==================================================");
        if (pool.isEmpty()) {
            System.out.println(" (no hay caballos guardados todavia)");
        } else {
            for (int i = 0; i < pool.size(); i++) {
                Caballo c = pool.get(i);
                String marca = (i == indiceSeleccionado) ? "  <== TU CABALLO" : "";
                System.out.printf("  %d) %-14s [%-11s]%s%n",
                        i + 1, c.getNombre(), atributoDe(c), marca);
            }
        }
        System.out.println(" --------------------------------------------------");
        System.out.println("  C) Crear nuevo caballo");
        System.out.println("  0) Volver al menu");
        System.out.println();

        String entrada = leerTexto("Elegi un caballo (numero) o una opcion: ").trim();

        if (entrada.equalsIgnoreCase("0")) {
            return;
        }
        if (entrada.equalsIgnoreCase("C")) {
            crearCaballo();
            return;
        }

        try {
            int idx = Integer.parseInt(entrada) - 1;
            if (idx < 0 || idx >= pool.size()) {
                System.out.println(">> Numero fuera de rango.\n");
                return;
            }
            indiceSeleccionado = idx;
            if (hayJugador) {
                jugadorController.seleccionarCaballo(pool.get(idx));
            }
            System.out.println(">> Elegiste a " + pool.get(idx).getNombre()
                    + ". El resto correran de forma automatica.\n");
        } catch (NumberFormatException e) {
            System.out.println(">> Opcion invalida.\n");
        }
    }

    /** Alta de caballo: solo pide tipo y nombre. Persiste y lo deja como el caballo elegido. */
    private void crearCaballo() {
        System.out.println("Tipo de caballo:");
        System.out.println("  1) Veloz");
        System.out.println("  2) Resistente");
        System.out.println("  3) Equilibrado");
        int tipo = leerEntero("Selecciona el tipo: ");
        AtributoCaballo atributo;
        switch (tipo) {
            case 1: atributo = AtributoCaballo.VELOZ;       break;
            case 2: atributo = AtributoCaballo.RESISTENTE;  break;
            case 3: atributo = AtributoCaballo.EQUILIBRADO; break;
            default:
                System.out.println(">> Tipo invalido.\n");
                return;
        }

        String nombre = leerTexto("Nombre del caballo: ");
        caballoController.crearCaballo(atributo, nombre);

        // Refrescamos el pool y seleccionamos el recien creado (siempre queda al final).
        pool = poolDesdeDB();
        indiceSeleccionado = pool.size() - 1;
        if (hayJugador) {
            jugadorController.seleccionarCaballo(pool.get(indiceSeleccionado));
        }
        System.out.println(">> Caballo '" + nombre + "' creado y seleccionado.\n");
    }

    /** Reconstruye el pool de modelo desde los caballos persistidos en la DB. */
    private List<Caballo> poolDesdeDB() {
        List<Caballo> resultado = new ArrayList<>();
        for (CaballoDTO dto : caballoController.listarCaballos()) {
            resultado.add(caballoDesdeDTO(dto));
        }
        return resultado;
    }

    /** Convierte un DTO al objeto de modelo correspondiente segun su subtipo. */
    private Caballo caballoDesdeDTO(CaballoDTO dto) {
        if (dto instanceof CaballoVelozDTO)      return new CaballoVeloz(dto.getNombre());
        if (dto instanceof CaballoResistenteDTO) return new CaballoResistente(dto.getNombre());
        return new CaballoEquilibrado(dto.getNombre());
    }

    private void mostrarPool() {
        for (int i = 0; i < pool.size(); i++) {
            Caballo c = pool.get(i);
            String marca = (i == indiceSeleccionado) ? "  <== TU CABALLO" : "";
            System.out.printf("  %d) %-14s [%-11s]%s%n",
                    i + 1, c.getNombre(), atributoDe(c), marca);
        }
    }

    // ------------------------------------------------------------------
    // 4) Carrera
    // ------------------------------------------------------------------
    private void correrCarrera() {
        if (!hayJugador) {
            System.out.println(">> Primero selecciona un jugador (opcion 1).\n");
            return;
        }
        if (indiceSeleccionado < 0) {
            System.out.println(">> Primero selecciona tu caballo (opcion 2).\n");
            return;
        }

        // Aseguramos que el jugador activo tenga asociado el caballo elegido (cubre el caso
        // en que se eligio el caballo antes que el jugador).
        jugadorController.seleccionarCaballo(pool.get(indiceSeleccionado));

        int longitud = leerEntero("Longitud de la pista (metros, ej. 200): ");
        if (longitud <= 0) {
            System.out.println(">> La longitud debe ser positiva.\n");
            return;
        }

        // Reiniciamos el estado de los caballos por si ya corrieron antes.
        for (Caballo c : pool) {
            c.descansar();
        }

        Caballo miCaballo = pool.get(indiceSeleccionado);

        // Toda la mecanica de la carrera ocurre del controller para abajo.
        carreraController.crearCarrera(pool, longitud);
        carreraController.iniciarCarrera();

        System.out.println("\n=== LARGAN! Pista de " + longitud + " metros ===\n");

        // La carrera termina cuando algun caballo cruza la meta (lo decide el modelo,
        // que marca FINALIZADA en avanzarCorredores). El tope es solo una red de seguridad.
        int instante = 0;
        while (carreraController.obtenerEstado() != EstadoCarrera.FINALIZADA
                && instante < MAX_INSTANTES) {

            carreraController.avanzarInstante();
            instante++;

            renderPista(carreraController.obtenerPosiciones(), longitud, miCaballo, instante);
            dormir(PAUSA_FRAME_MS);
        }

        finalizarCarrera(miCaballo, longitud);
    }

    /** Dibuja una barra de avance por caballo (orden actual de posiciones). */
    private void renderPista(List<Caballo> posiciones, int longitud, Caballo miCaballo, int instante) {
        System.out.println("Instante " + instante + ":");
        for (Caballo c : posiciones) {
            double ratio = Math.max(0.0, Math.min(1.0, c.getDistanciaRecorrida() / longitud));
            int llenos = (int) Math.round(ANCHO_BARRA * ratio);
            StringBuilder barra = new StringBuilder();
            for (int i = 0; i < ANCHO_BARRA; i++) {
                barra.append(i < llenos ? '=' : ' ');
            }
            String marca = (c == miCaballo) ? " <== TU" : "";
            System.out.printf("  %-12s |%s| %6.1fm%s%n",
                    c.getNombre(), barra, Math.max(0.0, c.getDistanciaRecorrida()), marca);
        }
        System.out.println();
    }

    /** El puesto y el puntaje los resuelven los controllers; aca solo se muestra el resultado. */
    private void finalizarCarrera(Caballo miCaballo, int longitud) {
        // El puesto de cada caballo lo determina el CarreraController (1 = ganador).
        int puestoJugador = carreraController.calcularPuesto(miCaballo);

        // Orden de la grilla solo para presentacion (el modelo ya no ordena la lista).
        List<Caballo> orden = new ArrayList<>(carreraController.obtenerPosiciones());
        orden.sort((a, b) -> Double.compare(b.getDistanciaRecorrida(), a.getDistanciaRecorrida()));

        System.out.println("==================================================");
        System.out.println(" CARRERA FINALIZADA");
        System.out.println("==================================================");
        System.out.println(" Posiciones finales:");
        for (int i = 0; i < orden.size(); i++) {
            Caballo c = orden.get(i);
            String marca = (c == miCaballo) ? "  <== TU CABALLO" : "";
            System.out.printf("   %d) %-12s  %.1fm%s%n",
                    i + 1, c.getNombre(), Math.max(0.0, c.getDistanciaRecorrida()), marca);
        }

        // El puntaje lo calcula y persiste el JugadorController segun el puesto.
        int antes = jugadorController.getJugadorSeleccionado().getPuntajeAcumulado();
        jugadorController.procesarPuntaje(puestoJugador);
        int despues = jugadorController.getJugadorSeleccionado().getPuntajeAcumulado();

        System.out.println();
        System.out.println(" Ganador        : " + orden.get(0).getNombre());
        System.out.println(" Tu caballo     : " + miCaballo.getNombre() + " (puesto " + puestoJugador + ")");
        System.out.println(" Puntos ganados : " + (despues - antes));
        System.out.println(" Puntaje acumulado de " + jugadorController.getJugadorSeleccionado().getNombre()
                + ": " + despues);
        System.out.println("==================================================\n");
    }

    // ------------------------------------------------------------------
    // Utilidades
    // ------------------------------------------------------------------
    private AtributoCaballo atributoDe(Caballo c) {
        if (c instanceof CaballoVeloz)      return AtributoCaballo.VELOZ;
        if (c instanceof CaballoResistente) return AtributoCaballo.RESISTENTE;
        return AtributoCaballo.EQUILIBRADO;
    }

    private int leerEntero(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linea = scanner.hasNextLine() ? scanner.nextLine().trim() : "0";
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("  (ingresa un numero valido)");
            }
        }
    }

    private String leerTexto(String prompt) {
        System.out.print(prompt);
        String linea = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
        while (linea.isEmpty()) {
            System.out.print("  (no puede estar vacio) " + prompt);
            linea = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
        }
        return linea;
    }

    private void dormir(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
