package com.uade.carreracaballos.model;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo_caballo")
public class CaballoEquilibrado extends Caballo {
	protected CaballoEquilibrado() {
    	//idem padre
    }
    public CaballoEquilibrado(String nombre) {
        super(nombre);
        this.velocidad = 5.5 + Math.random() * 1.5;
        this.resistencia = 52.0 + Math.random() * 13.0;
    }

    public CaballoEquilibrado(String nombre, double velocidad, double resistencia) {
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
