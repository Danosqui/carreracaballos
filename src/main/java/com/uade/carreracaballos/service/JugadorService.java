package com.uade.carreracaballos.service;

import com.uade.carreracaballos.dao.JugadorDAO;
import com.uade.carreracaballos.dto.JugadorDTO;
import com.uade.carreracaballos.model.Jugador;

import java.util.List;

public class JugadorService {

    private JugadorDAO jugadorDAO;

    public JugadorService() {
        this.jugadorDAO = new JugadorDAO();
    }

    public void guardarJugador(Jugador jugador) {
        jugadorDAO.crearJugador(jugador);
    }

    public JugadorDTO buscarJugador(String nombre, String mail) {
        Jugador jugador = jugadorDAO.buscarJugador(nombre);

        if (jugador == null || !jugador.getMail().equals(mail)) {
            return null;
        }

        return new JugadorDTO(
                0,
                jugador.getNombre(),
                jugador.getMail(),
                jugador.getPuntajeAcumulado()
        );
    }

    public List<Jugador> listarJugadores() {
        return jugadorDAO.listarJugadores();
    }

    public void actualizarJugador(Jugador jugador) {
        jugadorDAO.actualizarJugador(jugador);
    }
}