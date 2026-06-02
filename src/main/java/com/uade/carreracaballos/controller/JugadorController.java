package com.uade.carreracaballos.controller;

import com.uade.carreracaballos.dto.JugadorDTO;
import com.uade.carreracaballos.model.Caballo;
import com.uade.carreracaballos.model.Jugador;
import com.uade.carreracaballos.service.JugadorService;

import java.util.ArrayList;
import java.util.List;

public class JugadorController {

	private JugadorService jugadorService;
	private Jugador jugadorSeleccionado;

	public JugadorController() {
		this.jugadorService = new JugadorService();
		this.jugadorSeleccionado = null;
	}

	public void nuevoJugador(String nombre, String mail) {
		Jugador jugador = new Jugador(nombre, mail, 0);
		jugadorService.guardarJugador(jugador);
	}

	public void seleccionarJugador(int id) {
		
		this.jugadorSeleccionado = jugadorService.getJugador(id);
	}

	public void seleccionarJugador(String nombre, String mail) {
		Jugador jugador = jugadorService.buscarJugador(nombre, mail);
		if (jugador == null) throw new RuntimeException("Jugador no encontrado: " + nombre);
		this.jugadorSeleccionado = jugador;
	}

	public void seleccionarCaballo(Caballo caballo) {
		// hay que hacer que el caballo se reciba por id, y que el caballo service devuelva el caballo partiendo de esa id.
		// dantenota: no se como hacer, porque segun claude los controllers deberian hablar entre si, y segun gemini los service severian hablar entre si
		if (jugadorSeleccionado == null)
			throw new RuntimeException("Error: no se seleccionó un jugador");
		jugadorSeleccionado.seleccionarCaballo(caballo);
	}

	public List<JugadorDTO> listarJugadores() {
		List<JugadorDTO> dtos = new ArrayList<JugadorDTO>();
		for (Jugador j : jugadorService.listarJugadores()) {
			dtos.add(construirDTO(j));
		}
		return dtos;
	}

	public void procesarPuntaje(int posicionJugador) {
		int puntaje = 10;
		if (posicionJugador == 1) puntaje = 30;
		else if (posicionJugador == 2) puntaje = 20;

		jugadorSeleccionado.sumarPuntaje(puntaje);
		jugadorService.actualizarJugador(jugadorSeleccionado);
	}

	public JugadorDTO getJugadorSeleccionado() {
		if (jugadorSeleccionado==null) {
			return null;
		}
		return construirDTO(jugadorSeleccionado);
	}
	
	private JugadorDTO construirDTO(Jugador jug) {
		JugadorDTO dto = new JugadorDTO(
    			jug.getId(),
    			jug.getNombre(),
    			jug.getMail(),
    			jug.getPuntajeAcumulado()
    			);
    	return dto;
    	
    }
}
