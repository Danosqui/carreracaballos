package com.uade.carreracaballos.model;

import java.util.List;

public class Carrera {
	
	private List<Caballo> caballos;
	private estadoCarrera estado;
	private int longitudPista;
	
	public Carrera(List<Caballo> caballos, int longitudPista) {
		this.caballos = caballos;
		this.longitudPista = longitudPista;
		this.estado=estadoCarrera.STBY;
	}
	
	public void iniciarCarrera() {
		this.estado = estadoCarrera.EN_CURSO;
	}
	
	public estadoCarrera avanzarCorredores() {
		
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
			
			estado = estadoCarrera.FINALIZADA;
			
		}
	    return estado;
	}
	
	public List<Caballo> obtenerPosiciones(){
		return caballos;
	}
	public estadoCarrera getEstado() {
		return estado;
	}

}
