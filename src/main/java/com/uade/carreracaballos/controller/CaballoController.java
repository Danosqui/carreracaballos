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

	public void crearCaballo(AtributoCaballo atributo, String nombre) {
		Caballo nuevoCaballo;
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
