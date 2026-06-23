package com.uade.carreracaballos.service;

import com.uade.carreracaballos.DAO.JugadorDAO;
import com.uade.carreracaballos.dto.CaballoDTO;
import com.uade.carreracaballos.dto.JugadorDTO;
import com.uade.carreracaballos.interfaz.IJugadorDAO;
import com.uade.carreracaballos.model.Caballo;
import com.uade.carreracaballos.model.Jugador;

import java.util.ArrayList;
import java.util.List;

public class JugadorService {

    private IJugadorDAO jugadorDAO;

    public JugadorService() {
        this.jugadorDAO = new JugadorDAO();
    }

    public void guardarJugador(Jugador jugador) {
        jugadorDAO.crearJugador(jugador);
    }
    
    public Jugador getJugador(int id) {
    	Jugador jugador = jugadorDAO.getJugadorById(id);
    	return jugador;
    	
    }

    public List<JugadorDTO> listarJugadores() {
        List<Jugador>jugadores= jugadorDAO.listarJugadores();
        List<JugadorDTO> jugadoresDTO= new ArrayList<>();
        for (Jugador j : jugadores) {
			jugadoresDTO.add(construirDTO(j));
		}
        return jugadoresDTO;
    }

    public void actualizarJugador(Jugador jugador) {
        jugadorDAO.actualizarJugador(jugador);
    }

    public void eliminarJugador(int id) {
        Jugador jugador = jugadorDAO.getJugadorById(id);
        jugadorDAO.borrarJugador(jugador);
    }
    public JugadorDTO construirDTO(Jugador jug) {
		JugadorDTO dto = new JugadorDTO(
    			jug.getId(),
    			jug.getNombre(),
    			jug.getMail(),
    			jug.getPuntajeAcumulado()
    			);
		
		Caballo caballoSeleccionado = jug.getCaballoSeleccionado();
		if (caballoSeleccionado != null) {
			dto.setCaballoSeleccionado(caballoSeleccionado.getId());
		}
    	return dto;
    	
    }
    
}