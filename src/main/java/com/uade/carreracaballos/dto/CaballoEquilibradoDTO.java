package com.uade.carreracaballos.dto;

public class CaballoEquilibradoDTO extends CaballoDTO {
	private double multiplicadorResistencia;
	private double multiplicadorVelocidad;
	public double getMultiplicadorResistencia() {
		return multiplicadorResistencia;
	}
	public void setMultiplicadorResistencia(double multiplicadorResistencia) {
		this.multiplicadorResistencia = multiplicadorResistencia;
	}
	public double getMultiplicadorVelocidad() {
		return multiplicadorVelocidad;
	}
	public void setMultiplicadorVelocidad(double multiplicadorVelocidad) {
		this.multiplicadorVelocidad = multiplicadorVelocidad;
	}
	
	
}
