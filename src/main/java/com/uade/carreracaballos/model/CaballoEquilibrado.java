package com.uade.carreracaballos.model;

import java.util.Random;

public class CaballoEquilibrado extends Caballo {

	private double multiplicadorResistencia;
	private double multiplicadorVelocidad;

	public CaballoEquilibrado(String nombre) {
		super(nombre);
		Random random = new Random();
		this.velocidad = random.nextDouble()+ 1;
		this.resistencia = random.nextDouble();
		this.multiplicadorResistencia = random.nextDouble() * 0.4 + 1;
		this.multiplicadorVelocidad = random.nextDouble() * 0.4 + 1;
	}

	@Override
	public void avanzar() {
		Random random = new Random();
		distanciaRecorrida += velocidad * multiplicadorVelocidad * energia;
		energia -= (1 - resistencia) * (1-multiplicadorResistencia) * random.nextDouble();

	}

}
