package com.uade.carreracaballos.service;

import com.uade.carreracaballos.DAO.CaballoDAO;
import com.uade.carreracaballos.model.Caballo;

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

    // Devuelve modelo, NO DTOs. La conversión la hace el Controller.
    public List<Caballo> listarCaballos() {
        return caballoDAO.listarCaballos();
    }

    // Devuelve caballos aleatorios excluyendo el del jugador
    public List<Caballo> getRandomCaballos(int idExcluir) {
        return caballoDAO.getRandomCaballos(idExcluir);
    }

    public void borrarCaballo(Caballo caballo) {
        caballoDAO.borrarCaballo(caballo);
    }
}
