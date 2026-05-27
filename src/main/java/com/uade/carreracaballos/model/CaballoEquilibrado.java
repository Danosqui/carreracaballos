package com.uade.carreracaballos.model;

public class CaballoEquilibrado extends Caballo {

    public CaballoEquilibrado(String nombre) {
        super(nombre);
        this.velocidad = 5.5 + Math.random() * 1.5;
        this.resistencia = 52.0 + Math.random() * 13.0;
    }

    @Override
    public void avanzar() {
        double avance = velocidad * (energia / 100.0);
        distanciaRecorrida += avance;
        double desgaste = (100.0 - resistencia) / K;
        energia = Math.max(resistencia, energia - desgaste);
    }
}
