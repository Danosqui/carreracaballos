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
		Jugador jugador = new Jugador(nombre, mail);
		jugadorService.guardarJugador(jugador);
		jugadores.add(jugador);
	}

	public void seleccionarJugador(String nombre, String mail) {
		JugadorDTO jugadorDTO = jugadorService.buscarJugador(nombre, mail);
		if (jugadorDTO != null) {
			this.jugadorSeleccionado = new Jugador(
					jugadorDTO.getNombre(),
					jugadorDTO.getMail()
			);
			this.jugadorSeleccionado.sumarPuntaje(jugadorDTO.getPnutaje());
		} else {
			throw new RuntimeException("Jugador no encontrado");
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

	public void sumarPuntajeYGuardar(int puntaje) {
		jugadorSeleccionado.sumarPuntaje(puntaje);
		jugadorService.actualizarJugador(jugadorSeleccionado);
	}

	public Jugador getJugadorSeleccionado() {
		return jugadorSeleccionado;
	}
}
