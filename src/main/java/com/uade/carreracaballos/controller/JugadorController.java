package com.uade.carreracaballos.controller;

import com.uade.carreracaballos.dto.JugadorDTO;
import com.uade.carreracaballos.model.Caballo;
import com.uade.carreracaballos.model.Jugador;
import com.uade.carreracaballos.service.JugadorService;
import com.uade.carreracaballos.service.CaballoService;

import java.util.ArrayList;
import java.util.List;

public class JugadorController {

	private JugadorService jugadorService;
	private CaballoService caballoService; 
	
	private Jugador jugadorSeleccionado;

	public JugadorController() {
		this.jugadorService = new JugadorService();
		this.caballoService = new CaballoService();
		this.jugadorSeleccionado = null;
	}

	public void nuevoJugador(String nombre, String mail) {
		Jugador jugador = new Jugador(nombre, mail);
		jugadorService.guardarJugador(jugador);
	}

	public void seleccionarJugador(int id) {
		if (id == -1) this.jugadorSeleccionado = null;
		else this.jugadorSeleccionado = jugadorService.getJugador(id);
	}

	public void seleccionarCaballo(int id) {
		if (jugadorSeleccionado == null)
			throw new RuntimeException("Error: no se seleccionó un jugador");
		if (id ==-1) {
			jugadorSeleccionado.seleccionarCaballo(null);
		}
		else {
			Caballo caballo = caballoService.getCaballo(id);
			jugadorSeleccionado.seleccionarCaballo(caballo);
		}
				
	}

	public List<JugadorDTO> listarJugadores() {
		List<JugadorDTO> dtos = new ArrayList<JugadorDTO>();
		for (Jugador j : jugadorService.listarJugadores()) {
			dtos.add(construirDTO(j));
		}
		return dtos;
	}

	public void eliminarJugador(int id) {
		jugadorService.eliminarJugador(id);
		if (jugadorSeleccionado != null && jugadorSeleccionado.getId() == id) {
			this.seleccionarJugador(-1);
		}
	}

	public void procesarPuntaje(int posicionJugador) {
		int puntaje = 10;
		if (posicionJugador == 1) puntaje = 100;
		else if (posicionJugador == 2) puntaje = 50;

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
		
		Caballo caballoSeleccionado = jug.getCaballoSeleccionado();
		if (caballoSeleccionado != null) {
			dto.setCaballoSeleccionado(caballoSeleccionado.getId());
		}
    	return dto;
    	
    }
}
