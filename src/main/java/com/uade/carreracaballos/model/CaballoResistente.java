package com.uade.carreracaballos.model;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo_caballo")
public class CaballoResistente extends Caballo {
	protected CaballoResistente() {
		this.energia=100;
    }
    public CaballoResistente(String nombre) {
        super(nombre);
        this.velocidad = 4.0 + Math.random() * 1.5;
        this.resistencia = 78.0 + Math.random() * 10.0;
    }
}
