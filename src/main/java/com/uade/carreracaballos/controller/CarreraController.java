package com.uade.carreracaballos.controller;

import java.util.List;

import com.uade.carreracaballos.model.Caballo;
import com.uade.carreracaballos.model.Carrera;
import com.uade.carreracaballos.model.EstadoCarrera;

public class CarreraController {

    private Carrera carrera;

    public CarreraController() {
    }

    public void crearCarrera(List<Caballo> caballos, int longitudPista) {

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

    public int calcularPuesto(Caballo caballo) {
        List<Caballo> caballos = carrera.obtenerPosiciones();

		caballos.sort((c1, c2) -> Double.compare(c2.getDistanciaRecorrida(), c1.getDistanciaRecorrida())); //ordenamos por distancia recorrida, el que mas corrio primero

        for (int i = 0; i < caballos.size(); i++) {
            if (caballos.get(i) == caballo) {
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
}