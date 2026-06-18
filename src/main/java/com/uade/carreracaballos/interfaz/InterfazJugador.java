package com.uade.carreracaballos.interfaz;
import java.util.List;

import com.uade.carreracaballos.model.Jugador;

public interface InterfazJugador {
	void crearJugador(Jugador jugador);
	Jugador getJugadorById(int id);
	List<Jugador> listarJugadores();
	void actualizarJugador(Jugador jugador);
	void borrarJugador(Jugador jugador);
}
