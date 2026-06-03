package com.uade.carreracaballos.controller;

import java.util.List;

import com.uade.carreracaballos.model.Caballo;
import com.uade.carreracaballos.model.Carrera;
import com.uade.carreracaballos.model.EstadoCarrera;
import com.uade.carreracaballos.service.CaballoService;

public class CarreraController {

    private Carrera carrera;
    private CaballoService caballoService;

    public CarreraController() {
    	caballoService = new CaballoService();
    }

    public void crearCarrera(int idCaballoJugador, int longitudPista) {
    	List<Caballo> caballos = caballoService.getRandomCaballos(idCaballoJugador);
    	Caballo caballoJugador = caballoService.getCaballo(idCaballoJugador);
    	
    	caballos.add(caballoJugador);
    	
        carrera = new Carrera(caballos, longitudPista);
    }

    public void iniciarCarrera() {

        if (carrera != null) {
            carrera.iniciarCarrera();
        }
    }

    public boolean avanzarInstante() {

        if (carrera == null) {
            return false;
        }

        if (carrera.getEstado() == EstadoCarrera.FINALIZADA) {
            return false;
        }

        carrera.avanzarCorredores();

        return true;
    }

    public List<Caballo> obtenerPosiciones() {

        if (carrera == null) {
            return null;
        }

        return carrera.obtenerPosiciones();
    }

    public int calcularPuesto(int idCaballo) {
        List<Caballo> caballos = carrera.obtenerPosiciones();

		caballos.sort((c1, c2) -> Double.compare(c2.getDistanciaRecorrida(), c1.getDistanciaRecorrida())); //ordenamos por distancia recorrida, el que mas corrio primero

        for (int i = 0; i < caballos.size(); i++) {
            if (caballos.get(i).getId() == idCaballo) {
                return i + 1; // El puesto es el índice + 1
            }
        }
        
        return -1;
    }

    public EstadoCarrera obtenerEstado() {

        if (carrera == null) {
            return null;
        }

        return carrera.getEstado();
    }
    
    public boolean carreraFinalizada() {
    	if (carrera==null) return false;
    	
    	return carrera.getEstado() == EstadoCarrera.FINALIZADA;
    	
    }
}