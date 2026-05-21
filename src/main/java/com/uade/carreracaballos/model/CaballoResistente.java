package com.uade.carreracaballos.model;

import java.util.Random;

public class CaballoResistente extends Caballo {
	
	private double multiplicadorResistencia;
	
	public CaballoResistente(String nombre) {
		super(nombre);
		Random random = new Random();
		this.velocidad = random.nextDouble() * 0.8 + 1;
		this.resistencia = random.nextDouble();
		this.multiplicadorResistencia = random.nextDouble() + 1;
	}

	@Override
	public void avanzar() {
		Random random = new Random();
		distanciaRecorrida += velocidad * energia;
		energia -= (1 - resistencia) * (1-multiplicadorResistencia) * random.nextDouble();
	}

}
