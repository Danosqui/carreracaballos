package com.uade.carreracaballos.model;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name="caballos")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo_caballo")
public class CaballoResistente extends Caballo {

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
