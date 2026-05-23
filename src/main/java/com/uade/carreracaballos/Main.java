package com.uade.carreracaballos;

import com.uade.carreracaballos.DAO.CaballoDAO;
import com.uade.carreracaballos.DAO.JugadorDAO;
import com.uade.carreracaballos.dto.JugadorDTO;
import com.uade.carreracaballos.model.Caballo;
import com.uade.carreracaballos.model.CaballoEquilibrado;
import com.uade.carreracaballos.model.CaballoResistente;
import com.uade.carreracaballos.model.CaballoVeloz;
import com.uade.carreracaballos.model.Jugador;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        CaballoDAO caballoDAO = new CaballoDAO();
        JugadorDAO jugadorDAO = new JugadorDAO();

        System.out.println("=== TEST DE PERSISTENCIA — Carrera de Caballos ===\n");

        // --- Caballos ---
        System.out.println(">> Creando caballos...");
        Caballo veloz      = new CaballoVeloz("Rayo");
        Caballo resistente = new CaballoResistente("Tortuga Veloz");
        Caballo equilibrado = new CaballoEquilibrado("Centurion");

        caballoDAO.crearCaballo(veloz);
        caballoDAO.crearCaballo(resistente);
        caballoDAO.crearCaballo(equilibrado);
        System.out.println("   Rayo (VELOZ)          → guardado");
        System.out.println("   Tortuga Veloz (RES)   → guardado");
        System.out.println("   Centurion (EQ)         → guardado");

        System.out.println("\n>> Listando caballos desde la BD:");
        List<Caballo> caballos = caballoDAO.listarCaballos();
        for (Caballo c : caballos) {
            System.out.printf("   %-20s  vel=%.2f  res=%.2f  energia=%.2f%n",
                    c.getNombre(), c.getVelocidad(), c.getResistencia(), c.getEnergia());
        }

        // --- Jugadores ---
        System.out.println("\n>> Creando jugador...");
        Jugador jugador = new Jugador("Thomas", "tdocampo7@gmail.com");
        jugadorDAO.crearJugador(jugador);
        System.out.println("   Thomas → guardado");

        System.out.println("\n>> Listando jugadores desde la BD:");
        List<JugadorDTO> jugadores = jugadorDAO.listarJugadores();
        for (JugadorDTO j : jugadores) {
            System.out.printf("   %-15s  mail=%-30s  puntaje=%d%n",
                    j.getNombre(), j.getMail(), j.getPnutaje());
        }

        System.out.println("\n=== TEST FINALIZADO ===");
    }
}
