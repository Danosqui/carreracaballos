package com.uade.carreracaballos.model;

import java.util.Random;

public class CaballoVeloz extends Caballo {
	
	private double multiplicadorVelocidad;

	public CaballoVeloz(String nombre) {
		super(nombre);
		Random random = new Random();
		this.velocidad = random.nextDouble() + 1;
		this.resistencia = random.nextDouble() * 0.8;
		this.multiplicadorVelocidad = random.nextDouble() + 1;
	}

	@Override
	public void avanzar() {
		Random random = new Random();
		distanciaRecorrida += velocidad * multiplicadorVelocidad * energia;
		energia -= (1 - resistencia) * random.nextDouble() * 0.2;
	}
	
	

}
