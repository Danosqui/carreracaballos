package com.uade.carreracaballos.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("RESISTENTE")
public class CaballoResistente extends Caballo {

    protected CaballoResistente() {}

    public CaballoResistente(String nombre) {
        super(nombre);
        this.velocidad = 4.0 + Math.random() * 1.5;
        this.resistencia = 78.0 + Math.random() * 10.0;
    }

    public CaballoResistente(String nombre, double velocidad, double resistencia) {
        super(nombre, velocidad, resistencia);
    }

    @Override
    public void avanzar() {
        double avance = velocidad * (energia / 100.0);
        distanciaRecorrida += avance;
        double desgaste = (100.0 - resistencia) / K;
        energia = Math.max(resistencia, energia - desgaste);
    }
}
