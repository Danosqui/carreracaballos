package com.uade.carreracaballos.controller;

import com.uade.carreracaballos.dto.JugadorDTO;
import com.uade.carreracaballos.model.Caballo;
import com.uade.carreracaballos.model.Jugador;
import com.uade.carreracaballos.service.JugadorService;

import java.util.ArrayList;
import java.util.List;

public class JugadorController {

	private JugadorService jugadorService;
	private List<Jugador> jugadores;
	private Jugador jugadorSeleccionado;

	public JugadorController() {
		this.jugadorService = new JugadorService();
		this.jugadores = new ArrayList<>();
		this.jugadorSeleccionado = null;
	}

	public void nuevoJugador(String nombre, String mail) {
		Jugador jugador = new Jugador(nombre, mail, 0);
		jugadorService.guardarJugador(jugador);
		jugadores.add(jugador);
	}

	public void seleccionarJugador(JugadorDTO jugador) {
		if (jugador != null) {
			this.jugadorSeleccionado = new Jugador(
					jugador.getNombre(),
					jugador.getMail(),
					jugador.getPnutaje()
			);
		} else {
			throw new RuntimeException("Error de parametro: jugador es null");
		}
	}

	public void seleccionarCaballo(Caballo caballo) {
		if (jugadorSeleccionado != null) {
			jugadorSeleccionado.seleccionarCaballo(caballo);
		} else {
			throw new RuntimeException("Error: no se seleccionó un jugador");
		}
	}

	public List<JugadorDTO> listarJugadores() {
		return jugadorService.listarJugadores();
	}

	public void procesarPuntaje(int posicionJugador) {
		//	AAAAAAAAAAAAAA
		// REIVSAR QUE EL PUNTAJE ESTE BIEN SETEADOOOO
		// AAAAAAAAAA
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
