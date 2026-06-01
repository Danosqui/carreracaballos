package com.uade.carreracaballos.controller;

import com.uade.carreracaballos.dto.JugadorDTO;
import com.uade.carreracaballos.model.Caballo;
import com.uade.carreracaballos.model.Jugador;
import com.uade.carreracaballos.service.JugadorService;

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

	public void seleccionarJugador(Jugador jugador) {
		if (jugador == null) throw new RuntimeException("Error de parametro: jugador es null");
		this.jugadorSeleccionado = jugador;
	}

	public void seleccionarJugador(String nombre, String mail) {
		Jugador jugador = jugadorService.buscarJugador(nombre, mail);
		if (jugador == null) throw new RuntimeException("Jugador no encontrado: " + nombre);
		this.jugadorSeleccionado = jugador;
	}

	public void seleccionarCaballo(Caballo caballo) {
		if (jugadorSeleccionado == null)
			throw new RuntimeException("Error: no se seleccionó un jugador");
		jugadorSeleccionado.seleccionarCaballo(caballo);
	}

	public List<Jugador> listarJugadores() {
		return jugadorService.listarJugadores();
	}

	public void procesarPuntaje(int posicionJugador) {
		int puntaje = 10;
		if (posicionJugador == 1) puntaje = 30;
		else if (posicionJugador == 2) puntaje = 20;

		jugadorSeleccionado.sumarPuntaje(puntaje);
		jugadorService.actualizarJugador(jugadorSeleccionado);
	}

	public Jugador getJugadorSeleccionado() {
		return jugadorSeleccionado;
	}
}
