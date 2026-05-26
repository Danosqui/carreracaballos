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
		}
		int n = caballos.size();
	    for (int i = 0; i < n - 1; i++) { //bubble sort cortesia de claude esperemos que funque
	        for (int j = 0; j < n - i - 1; j++) {
	            if (caballos.get(j).getDistanciaRecorrida() < caballos.get(j + 1).getDistanciaRecorrida()) {
	                Caballo temp = caballos.get(j);
	                caballos.set(j, caballos.get(j + 1));
	                caballos.set(j + 1, temp);
	            }
	        }
	    }
	    if (caballos.get(caballos.size()-1).distanciaRecorrida >= longitudPista) { //si el primero ya termino
			
			estado = EstadoCarrera.FINALIZADA;
			
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
