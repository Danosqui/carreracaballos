package com.uade.carreracaballos;

import com.uade.carreracaballos.config.JPAUtil;
import com.uade.carreracaballos.dao.CaballoDAO;
import com.uade.carreracaballos.dao.JugadorDAO;
import com.uade.carreracaballos.model.Caballo;
import com.uade.carreracaballos.model.CaballoEquilibrado;
import com.uade.carreracaballos.model.CaballoResistente;
import com.uade.carreracaballos.model.CaballoVeloz;
import com.uade.carreracaballos.model.Jugador;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Prueba DIRECTA de los DAOs sin pasar por controllers ni services.
 * Propósito: validar que la persistencia (MySQL) funciona correctamente.
 */
public class DAOTest {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  PRUEBA DIRECTA DE DAOs");
        System.out.println("========================================\n");

        try {
            limpiarDB();

            // Test CaballoDAO
            testCaballoDAO();
            
            System.out.println("\n" + "=".repeat(40) + "\n");
            
            // Test JugadorDAO
            testJugadorDAO();
            
            System.out.println("\n========================================");
            System.out.println("  ✓ TODAS LAS PRUEBAS COMPLETADAS");
            System.out.println("========================================");
        } catch (Exception e) {
            System.err.println("✗ ERROR EN LAS PRUEBAS:");
            e.printStackTrace();
        }
    }

    private static void limpiarDB() {
        EntityManager em = JPAUtil.getInstance().crearEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Jugador").executeUpdate();
            em.createQuery("DELETE FROM Caballo").executeUpdate();
            em.getTransaction().commit();
            System.out.println("DB limpiada\n");
        } finally {
            em.close();
        }
    }

    // ========================================
    // PRUEBAS DE CaballoDAO
    // ========================================
    private static void testCaballoDAO() {
        System.out.println("PROBANDO: CaballoDAO");
        System.out.println("-".repeat(40));

        CaballoDAO caballoDAO = new CaballoDAO();

        // Test 1: Crear caballos de diferentes tipos
        System.out.println("\n1. Creando caballos...");
        Caballo veloz = new CaballoVeloz("Rayo");
        Caballo resistente = new CaballoResistente("Titán");
        Caballo equilibrado = new CaballoEquilibrado("Centauro");

        caballoDAO.crearCaballo(veloz);
        System.out.println("   ✓ Caballo Veloz: " + veloz.getNombre());
        
        caballoDAO.crearCaballo(resistente);
        System.out.println("   ✓ Caballo Resistente: " + resistente.getNombre());
        
        caballoDAO.crearCaballo(equilibrado);
        System.out.println("   ✓ Caballo Equilibrado: " + equilibrado.getNombre());

        // Test 2: Listar todos los caballos
        System.out.println("\n2. Listando caballos...");
        List<Caballo> caballos = caballoDAO.listarCaballos();
        System.out.println("   Total de caballos en DB: " + caballos.size());
        for (Caballo c : caballos) {
            String tipo = c.getClass().getSimpleName();
            System.out.printf("   - %-15s [%s]%n", c.getNombre(), tipo);
        }

        // Test 3: Obtener un caballo al azar
        System.out.println("\n3. Obteniendo caballos al azar...");
        List<Caballo> random = caballoDAO.getRandomCaballos(2);
        System.out.println("   Caballos seleccionados: " + random.size());
        for (Caballo c : random) {
            System.out.println("   - " + c.getNombre());
        }

        System.out.println("\n✓ CaballoDAO: OK");
    }

    // ========================================
    // PRUEBAS DE JugadorDAO
    // ========================================
    private static void testJugadorDAO() {
        System.out.println("PROBANDO: JugadorDAO");
        System.out.println("-".repeat(40));

        JugadorDAO jugadorDAO = new JugadorDAO();

        // Test 1: Crear jugadores
        System.out.println("\n1. Creando jugadores...");
        Jugador j1 = new Jugador("Carlos", "carlos@mail.com", 0);
        Jugador j2 = new Jugador("María", "maria@mail.com", 0);
        Jugador j3 = new Jugador("Juan", "juan@mail.com", 0);

        jugadorDAO.crearJugador(j1);
        System.out.println("   ✓ Jugador: " + j1.getNombre() + " (" + j1.getMail() + ")");
        
        jugadorDAO.crearJugador(j2);
        System.out.println("   ✓ Jugador: " + j2.getNombre() + " (" + j2.getMail() + ")");
        
        jugadorDAO.crearJugador(j3);
        System.out.println("   ✓ Jugador: " + j3.getNombre() + " (" + j3.getMail() + ")");

        // Test 2: Listar todos los jugadores
        System.out.println("\n2. Listando jugadores...");
        List<Jugador> jugadores = jugadorDAO.listarJugadores();
        System.out.println("   Total de jugadores en DB: " + jugadores.size());
        for (Jugador j : jugadores) {
            System.out.printf("   - %-12s | %-25s | puntaje: %d%n", 
                j.getNombre(), j.getMail(), j.getPuntajeAcumulado());
        }

        // Test 3: Buscar un jugador por nombre
        System.out.println("\n3. Buscando jugador por nombre...");
        Jugador encontrado = jugadorDAO.buscarJugador("Carlos");
        if (encontrado != null) {
            System.out.println("   ✓ Encontrado: " + encontrado.getNombre());
            System.out.println("     Mail: " + encontrado.getMail());
            System.out.println("     Puntaje: " + encontrado.getPuntajeAcumulado());
        } else {
            System.out.println("   ✗ No encontrado");
        }

        // Test 4: Actualizar puntaje de un jugador
        System.out.println("\n4. Actualizando puntaje de un jugador...");
        Jugador parActualizar = jugadorDAO.buscarJugador("María");
        if (parActualizar != null) {
            parActualizar.sumarPuntaje(100);
            jugadorDAO.actualizarJugador(parActualizar);
            System.out.println("   ✓ María: puntaje actualizado a " + parActualizar.getPuntajeAcumulado());
            
            // Verificar que se guardó
            Jugador verificado = jugadorDAO.buscarJugador("María");
            System.out.println("   ✓ Verificación: puntaje en DB = " + verificado.getPuntajeAcumulado());
        }

        // Test 5: Sumar más puntaje a otro jugador
        System.out.println("\n5. Sumando más puntaje...");
        Jugador parSumar = jugadorDAO.buscarJugador("Juan");
        if (parSumar != null) {
            parSumar.sumarPuntaje(50);
            jugadorDAO.actualizarJugador(parSumar);
            System.out.println("   ✓ Juan: puntaje actualizado a " + parSumar.getPuntajeAcumulado());
        }

        // Test 6: Listar nuevamente para ver cambios
        System.out.println("\n6. Listando jugadores después de actualizaciones...");
        List<Jugador> jugadoresActualizados = jugadorDAO.listarJugadores();
        for (Jugador j : jugadoresActualizados) {
            System.out.printf("   - %-12s | puntaje: %3d%n", j.getNombre(), j.getPuntajeAcumulado());
        }

        System.out.println("\n✓ JugadorDAO: OK");
    }
}

