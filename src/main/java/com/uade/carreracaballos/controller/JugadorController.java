package com.uade.carreracaballos.controller;

import com.uade.carreracaballos.dto.JugadorDTO;
import com.uade.carreracaballos.model.Caballo;
import com.uade.carreracaballos.model.Jugador;
import java.util.List;
import com.uade.carreracaballos.service.JugadorService;

public class JugadorController {
	private JugadorService jugadorService;
	private Jugador jugadorSeleccionado;
	
	public JugadorController() {
		this.jugadorService = new JugadorService();
		this.jugadorSeleccionado = null; 
	}
	
	public void nuevoJugador(Jugador jugador) {
		jugadorService.guardarJugador(jugador);
	}
	
	public void seleccionarJugador(int jugadorId) {
		JugadorDTO jugador = jugadorService.buscarJugador(jugadorId);
		if (jugador != null) {
			this.jugadorSeleccionado = new Jugador(
					jugador.getNombre(),
					jugador.getMail()
					);
		}
		else {
			throw new RuntimeException("La ID del jugador a buscar no existe");
		}
	}
	
	public void seleccionarCaballo(Caballo caballo) {
		if (jugadorSeleccionado != null) {
			jugadorSeleccionado.seleccionarCaballo(caballo);
		}
		else {
			throw new RuntimeException("Error: no se selecciono un jugador");
		}
	}
	
	public List<JugadorDTO> listarJugadores(){
		return jugadorService.listarJugadores();
	}
	
	public void sumarPuntajeYGuardar(int puntaje) {
		jugadorSeleccionado.sumarPuntaje(puntaje);
		jugadorService.actualizarJugador(jugadorSeleccionado);
	}
}
