package com.uade.carreracaballos.model;

public abstract class Caballo {

	protected String nombre;
	protected double velocidad;
	protected double energia;
	protected double resistencia;
	protected double distanciaRecorrida;

	public Caballo(String nombre) {
		this.nombre = nombre;
		this.energia = 1;
		this.distanciaRecorrida = 0;
	}

	public abstract void avanzar();

	public void descansar() {
		energia = 1;
		distanciaRecorrida = 0;
	}

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }

	public double getVelocidad() { return velocidad; }
	public double getEnergia() { return energia; }
	public double getResistencia() { return resistencia; }

	public double getDistanciaRecorrida() { return distanciaRecorrida; }
	public void setDistanciaRecorrida(double distanciaRecorrida) { this.distanciaRecorrida = distanciaRecorrida; }
}
