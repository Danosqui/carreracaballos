
package com.uade.carreracaballos.model;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo_caballo")
public class CaballoVeloz extends Caballo {
	protected CaballoVeloz() {
    	this.energia=100;
    }
    public CaballoVeloz(String nombre) {
        super(nombre);
        this.velocidad = 8.0 + Math.random() * 2.0;
        this.resistencia = 35.0 + Math.random() * 10.0;
    }

    public CaballoVeloz(String nombre, double velocidad, double resistencia) {
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
