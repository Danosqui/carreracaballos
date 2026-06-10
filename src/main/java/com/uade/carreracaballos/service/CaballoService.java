package com.uade.carreracaballos.service;

import com.uade.carreracaballos.DAO.CaballoDAO;
import com.uade.carreracaballos.model.Caballo;
import com.uade.carreracaballos.dto.CaballoDTO;

import java.util.List;

public class CaballoService {

    private CaballoDAO caballoDAO;

    public CaballoService() {
        this.caballoDAO = new CaballoDAO();
    }

    public void crearCaballo(Caballo caballo) {
        caballoDAO.crearCaballo(caballo);

    }
    
    public Caballo getCaballo(int id) {
    	return caballoDAO.getCaballo(id);
    }

    public List<Caballo> listarCaballos() {
        return caballoDAO.listarCaballos();
    }

    public List<Caballo> getRandomCaballos(int idExcluir) {
        return caballoDAO.getRandomCaballos(idExcluir);
    }

    public void borrarCaballo(Caballo caballo) {
        caballoDAO.borrarCaballo(caballo);
    }

    public CaballoDTO aDTO(Caballo caballo) {
        CaballoDTO dto = new CaballoDTO();
		dto.setId(caballo.getId());
		dto.setNombre(caballo.getNombre());
		dto.setVelocidad(caballo.getVelocidad());
		dto.setResistencia(caballo.getResistencia());
		dto.setEnergia(caballo.getEnergia());
		dto.setDistanciaRecorrida(caballo.getDistanciaRecorrida());
        return dto;
    }
}
