package com.uade.carreracaballos.controller;

import java.util.List;

import com.uade.carreracaballos.model.AtributoCaballo;
import com.uade.carreracaballos.model.Caballo;
import com.uade.carreracaballos.model.CaballoEquilibrado;
import com.uade.carreracaballos.model.CaballoResistente;
import com.uade.carreracaballos.model.CaballoVeloz;

public class CaballoController {
	private List<Caballo> caballos;
	private CaballoService caballoService;
	
	public void crearCaballo(AtributoCaballo atributo, String nombre) {
		Caballo nuevocaballin;
		switch (atributo) {
			case EQUILIBRADO: nuevocaballin = new CaballoEquilibrado(nombre);
			case RESISTENTE: nuevocaballin= new CaballoResistente(nombre);
			case VELOZ: nuevocaballin = new CaballoVeloz(nombre);
			default: throw new RuntimeException("el tipo no existe");
		}
		
	}
	
	public List<CaballoDTO> listarCaballos(){
		return caballoService.listarCaballos;
	}
		
}
