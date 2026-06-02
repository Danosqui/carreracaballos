package com.uade.carreracaballos.dto;

public class JugadorDTO {
	
	private int id;
	private String nombre;
	private String mail;
	private int puntaje;
	private int caballoSeleccionadoId;
	
	public JugadorDTO(int id, String nombre, String mail, int puntaje) {
		this.id=id;
		this.nombre=nombre;
		this.mail=mail;
		this.puntaje=puntaje;
	}
	
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
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public int getPuntaje() {
		return puntaje;
	}
	public void setPuntaje(int puntaje) {
		this.puntaje = puntaje;
	}
	public int getCaballoSeleccionadoId() {
		return caballoSeleccionadoId;
	}
	public void setCaballoSeleccionado(int id) {
		this.caballoSeleccionadoId = id;
	}
	

}
