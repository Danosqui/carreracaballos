package com.uade.carreracaballos.dto;

public class CaballoDTO {
	protected int id;
	protected String nombre;
	protected double velocidad;
	protected double resistencia;
	protected double energia;
	protected double distanciaRecorrida;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public double getVelocidad() {
		return velocidad;
	}
	public void setVelocidad(double velocidad) {
		this.velocidad = velocidad;
	}
	public double getEnergia() {
		return energia;
	}
	public void setEnergia(double energia) {
		this.energia = energia;
	}
	public double getResistencia() {
		return resistencia;
	}
	public void setResistencia(double resistencia) {
		this.resistencia = resistencia;
	}
	public double getDistanciaRecorrida() {
		return distanciaRecorrida;
	}
	public void setDistanciaRecorrida(double distanciaRecorrida) {
		this.distanciaRecorrida = distanciaRecorrida;
	}
}
