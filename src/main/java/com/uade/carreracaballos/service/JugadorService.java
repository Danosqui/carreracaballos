package com.uade.carreracaballos.service;

import com.uade.carreracaballos.DAO.JugadorDAO;
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

    public Jugador buscarJugador(String nombre, String mail) {
        Jugador jugador = jugadorDAO.buscarJugador(nombre);

        if (jugador == null || !jugador.getMail().equals(mail)) {
            return null;
        }

        return jugador;
    }
    
    public Jugador getJugador(int id) {
    	Jugador jugador = jugadorDAO.getJugadorById(id);
    	return jugador;
    	
    }

    public List<Jugador> listarJugadores() {
        return jugadorDAO.listarJugadores();
    }

    public void actualizarJugador(Jugador jugador) {
        jugadorDAO.actualizarJugador(jugador);
    }
    
    
}