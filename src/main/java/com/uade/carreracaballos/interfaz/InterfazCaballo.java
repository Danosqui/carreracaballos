package com.uade.carreracaballos.interfaz;
import java.util.List;

import com.uade.carreracaballos.model.Caballo;

public interface InterfazCaballo {
	 void crearCaballo(Caballo caballo);
	 Caballo getCaballo(int id);
	 List<Caballo> listarCaballos();
	 List<Caballo> getRandomCaballos(int idExcluir);
	 void borrarCaballo(Caballo caballo);
}
