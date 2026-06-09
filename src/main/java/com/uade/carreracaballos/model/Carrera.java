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
	
	public int calcularPuesto(int idCaballo) {
		List<Caballo> caballos = obtenerPosiciones();

		caballos.sort((c1, c2) -> Double.compare(c2.getDistanciaRecorrida(), c1.getDistanciaRecorrida()));
		
        for (int i = 0; i < caballos.size(); i++) 
            if (caballos.get(i).getId() == idCaballo) return i + 1;
        
        return -1;
	}
	
	public List<Caballo> obtenerPosiciones(){
		return caballos;
	}

	public EstadoCarrera getEstado() {
		return estado;
	}

}
