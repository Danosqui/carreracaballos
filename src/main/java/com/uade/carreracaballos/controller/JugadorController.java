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
	private CaballoService caballoService; // no estamos del todo seguros de que sea asi.
	// Habria q ver bien q onda, no sabemos si corresponde esto ya que se crean 2 instancias del service, o sino crear una sola instancia en main y mandarsela a los controllers
	// info al respecto https://claude.ai/share/9e54dd5f-08bc-4fa8-b627-94a1af79903a
	
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
		
		this.jugadorSeleccionado = jugadorService.getJugador(id);
	}

	public void seleccionarJugador(String nombre, String mail) {
		Jugador jugador = jugadorService.buscarJugador(nombre, mail);
		if (jugador == null) throw new RuntimeException("Jugador no encontrado: " + nombre);
		this.jugadorSeleccionado = jugador;
	}

	/*public void seleccionarCaballo(Caballo caballo) {
		// hay que hacer que el caballo se reciba por id, y que el caballo service devuelva el caballo partiendo de esa id.
		// dantenota: no se como hacer, porque segun claude los controllers deberian hablar entre si, y segun gemini los service severian hablar entre si
		if (jugadorSeleccionado == null)
			throw new RuntimeException("Error: no se seleccionó un jugador");
		jugadorSeleccionado.seleccionarCaballo(caballo);
	}*/

	public void seleccionarCaballo(int id) { // 
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
			jugadorSeleccionado = null;
		}
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
		
		Caballo caballoSeleccionado = jug.getCaballoSeleccionado();
		if (caballoSeleccionado != null) {
			dto.setCaballoSeleccionado(caballoSeleccionado.getId());
		}
    	return dto;
    	
    }
}
