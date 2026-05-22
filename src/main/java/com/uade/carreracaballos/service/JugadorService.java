package com.uade.carreracaballos.service;

import com.uade.carreracaballos.dao.JugadorDAO;
import com.uade.carreracaballos.dto.JugadorDTO;
import com.uade.carreracaballos.model.Jugador;

public class JugadorService {

    private JugadorDAO jugadorDAO;

    public JugadorService() {
        this.jugadorDAO = new JugadorDAO();
    }

    public void guardarJugador(Jugador jugador) {

        jugadorDAO.crearJugador(jugador);
    }

    public JugadorDTO buscarJugador(String nombreJugador,
                                    String mail) {

        Jugador jugador =
                jugadorDAO.buscarJugador(nombreJugador);

        if (jugador == null || !jugador.getMail().equals(mail)) {
            return null;
        }
        else{
            return new JugadorDTO(
                0,
                jugador.getNombre(),
                jugador.getMail(),
                jugador.getPuntajeAcumulado()
        );
        }
    }

    public void actualizarJugador(Jugador jugador) {

        jugadorDAO.actualizarJugador(jugador);
    }
}