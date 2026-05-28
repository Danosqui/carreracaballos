package com.uade.carreracaballos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="jugadores")
public class Jugador {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable=false, length=100)
	private String nombre;
	@Column(nullable=false, length=100)
	private String mail;
	@Column(nullable=false, name="puntaje")
	private int puntajeAcumulado;
	@Transient
	private Caballo caballoSeleccionado;

	protected Jugador() {}

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
