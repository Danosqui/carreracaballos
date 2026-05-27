package com.uade.carreracaballos.model;

import java.util.List;

public class Carrera {
	
	private List<Caballo> caballos;
	private EstadoCarrera estado;
	private int longitudPista;
	
	public Carrera(List<Caballo> caballos, int longitudPista) {
		this.caballos = caballos;
		this.longitudPista = longitudPista;
		this.estado=EstadoCarrera.STBY;
	}
	
	public void iniciarCarrera() {
		this.estado = EstadoCarrera.EN_CURSO;
	}
	
	public EstadoCarrera avanzarCorredores() {
		for (Caballo caballo : caballos) {
			caballo.avanzar();
			if (caballo.distanciaRecorrida >= this.longitudPista) {
				estado = EstadoCarrera.FINALIZADA;
				break;
			}
		}
		return estado;
	}
	
	public List<Caballo> obtenerPosiciones(){
		return caballos;
	}

	public EstadoCarrera getEstado() {
		return estado;
	}

}
