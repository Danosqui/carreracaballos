package com.uade.carreracaballos.model;

public class Jugador {
	
	
	private String nombre;
	private String mail;
	private int puntajeAcumulado;
	private Caballo caballoSeleccionado;

	public Jugador(String nombre, String mail, int puntaje) {
		this.nombre = nombre;
		this.mail = mail;
		this.puntajeAcumulado = puntaje;
		this.caballoSeleccionado = null;
	}
	
	public void sumarPuntaje(int puntaje) {
		puntajeAcumulado += puntaje;
	}
	public void seleccionarCaballo(Caballo caballo) {
		caballoSeleccionado = caballo;
	}
	
	//getters y setters aunque en realidad seleccionarcaballo es un setter con otro nombre
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public int getPuntajeAcumulado() {
		return puntajeAcumulado;
	}

	public Caballo getCaballoSeleccionado() {
		return caballoSeleccionado;
	}
}
