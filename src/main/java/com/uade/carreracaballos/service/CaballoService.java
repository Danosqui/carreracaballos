package com.uade.carreracaballos.service;

import com.uade.carreracaballos.DAO.CaballoDAO;
import com.uade.carreracaballos.model.Caballo;

import java.util.List;

public class CaballoService {

    private CaballoDAO caballoDAO;

    public CaballoService() {
        this.caballoDAO = new CaballoDAO();
    }

    public Caballo crearCaballo(Caballo caballo) {
        caballoDAO.crearCaballo(caballo);
        return caballo;
    }

    // Devuelve modelo, NO DTOs. La conversión la hace el Controller.
    public List<Caballo> listarCaballos() {
        return caballoDAO.listarCaballos();
    }

    // Devuelve caballos aleatorios excluyendo el del jugador
    public List<Caballo> getRandomCaballos(int idExcluir) {
        return caballoDAO.getRandomCaballos(idExcluir);
    }

    public void borrarCaballo(int caballoId) {
        caballoDAO.borrarCaballo(caballoId);
    }
}
