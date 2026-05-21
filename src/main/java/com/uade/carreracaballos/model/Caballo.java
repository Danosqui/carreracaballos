package com.uade.carreracaballos.model;

import java.util.Random;

public abstract class Caballo {

	protected String nombre;
	protected double velocidad; //entre 1 y 2
	protected double energia; //empieza en 1 y baja hasta 0
	protected double resistencia; //entre 0 y 1
	protected double distanciaRecorrida; //en metros
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getDistanciaRecorrida() {
		return distanciaRecorrida;
	}

	public void setDistanciaRecorrida(double distanciaRecorrida) {
		this.distanciaRecorrida = distanciaRecorrida;
	}

	public Caballo(String nombre) {
		this.nombre=nombre;
		this.energia = 1;
		this.distanciaRecorrida = 0;
	}
	
	public abstract void avanzar();
	
	public void descansar() {
		energia = 1;
		distanciaRecorrida = 0;
	}
}
