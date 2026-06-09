package com.uade.carreracaballos.controller;

import java.util.ArrayList;
import java.util.List;

import com.uade.carreracaballos.dto.CaballoDTO;
import com.uade.carreracaballos.model.Caballo;
import com.uade.carreracaballos.model.CaballoResistente;
import com.uade.carreracaballos.model.CaballoVeloz;
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

    public void avanzarInstante() {

        carrera.avanzarCorredores();

    }
    
    public List<CaballoDTO> obtenerPosiciones(){
    	if (carrera==null) return null;
    	List<Caballo> posiciones = carrera.obtenerPosiciones();
    	List<CaballoDTO> posicionesDTO = new ArrayList<CaballoDTO>();
    	for (Caballo c : posiciones) {
    		posicionesDTO.add(caballoService.aDTO(c));
    	}
    	return posicionesDTO;
    }

    public int calcularPuesto(int idCaballo) {
    	return carrera.calcularPuesto(idCaballo);
    }
    
    public boolean carreraFinalizada() {
    	if (carrera==null) return false;
    	
    	return carrera.getEstado() == EstadoCarrera.FINALIZADA;
    	
    }
}