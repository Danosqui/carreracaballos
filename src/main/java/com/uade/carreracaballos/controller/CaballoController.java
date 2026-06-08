package com.uade.carreracaballos.controller;

import java.util.ArrayList;
import java.util.List;

import com.uade.carreracaballos.dto.CaballoDTO;
import com.uade.carreracaballos.dto.CaballoEquilibradoDTO;
import com.uade.carreracaballos.dto.CaballoResistenteDTO;
import com.uade.carreracaballos.dto.CaballoVelozDTO;
import com.uade.carreracaballos.model.AtributoCaballo;
import com.uade.carreracaballos.model.Caballo;
import com.uade.carreracaballos.model.CaballoEquilibrado;
import com.uade.carreracaballos.model.CaballoResistente;
import com.uade.carreracaballos.model.CaballoVeloz;
import com.uade.carreracaballos.service.CaballoService;

public class CaballoController {

	private CaballoService caballoService;

	public CaballoController() {
		this.caballoService = new CaballoService();
	}

	public void crearCaballo(String atributoString, String nombre) {
		Caballo nuevoCaballo;
		AtributoCaballo atributo = AtributoCaballo.valueOf(atributoString);
		switch (atributo) {
			case EQUILIBRADO:
				nuevoCaballo = new CaballoEquilibrado(nombre);
				break;
			case RESISTENTE:
				nuevoCaballo = new CaballoResistente(nombre);
				break;
			case VELOZ:
				nuevoCaballo = new CaballoVeloz(nombre);
				break;
			default:
				throw new RuntimeException("El tipo de caballo no existe");
		}
		caballoService.crearCaballo(nuevoCaballo);
	}

	public List<CaballoDTO> listarCaballos() {
		List<CaballoDTO> caballosDTO = new ArrayList<>();
		for (Caballo c : caballoService.listarCaballos()) {
			caballosDTO.add(convertirADTO(c));
		}
		return caballosDTO;
	}

	
	public void eliminarCaballo(int id) {
		Caballo caballo = caballoService.getCaballo(id);
		caballoService.borrarCaballo(caballo);
	}
	
	public String[] listarAtributosCaballo(){
		AtributoCaballo[] atributos = AtributoCaballo.values();
		List<String> atributosString = new ArrayList<String>();
		for (AtributoCaballo a : atributos) {
			atributosString.add(a.toString());
		}
		return atributosString.toArray(new String[0]);
	}

	// no estoy seguro de que esto deba ser public, lo cambie par aque funcione manuframe
	private CaballoDTO convertirADTO(Caballo c) {
		CaballoDTO dto;
		if (c instanceof CaballoVeloz) {
			dto = new CaballoVelozDTO();
		} else if (c instanceof CaballoResistente) {
			dto = new CaballoResistenteDTO();
		} else {
			dto = new CaballoEquilibradoDTO();
		}
		dto.setId(c.getId());
		dto.setNombre(c.getNombre());
		dto.setVelocidad(c.getVelocidad());
		dto.setResistencia(c.getResistencia());
		dto.setEnergia(c.getEnergia());
		dto.setDistanciaRecorrida(c.getDistanciaRecorrida());
		return dto;
	}
}
