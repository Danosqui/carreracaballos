package com.uade.carreracaballos.DAO;

import com.uade.carreracaballos.model.Caballo;
import com.uade.carreracaballos.model.Jugador;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import com.uade.carreracaballos.config.JPAUtil;
import java.util.List;

public class CaballoDAO {

    public void crearCaballo(Caballo caballo) {
    	EntityManager enti = JPAUtil.getInstance().crearEntityManager();
    	try {
    		enti.getTransaction().begin();
    		enti.persist(caballo);
    		enti.getTransaction().commit();
    	}
    	catch(RuntimeException e) {
    		if (enti.getTransaction().isActive()) {
    			enti.getTransaction().rollback();
    		}
    		throw e;
    	}
    	finally {
    		enti.close();
    	}
    }
    
    public Caballo getCaballo(int id) {
    	EntityManager em = JPAUtil.getInstance().crearEntityManager();
    	return em.find(Caballo.class, id);
    }

    public List<Caballo> listarCaballos() {
    	EntityManager em = JPAUtil.getInstance().crearEntityManager();
    	try {
    		return em.createQuery("SELECT c FROM Caballo c", Caballo.class)
    				.getResultList();
    	}
    	finally {
    		em.close();
    	}
    }

    public List<Caballo> getRandomCaballos(int idExcluir) {
    	EntityManager em = JPAUtil.getInstance().crearEntityManager();
    	int CANTIDAD_CABALLOS_CARRERA=5;
    	try { //NOTA: SI ESTO NO FUNCIONA CAMBIAR EL <> POR !=, ESTOY CONFIANDO EN CLAUDIA ME DIJO Q ES ASI
    		return em.createQuery("SELECT c FROM Caballo c WHERE c.id <> :excluir ORDER BY RAND()", Caballo.class)
    		.setParameter("excluir", idExcluir)
    		.setMaxResults(CANTIDAD_CABALLOS_CARRERA)
    		.getResultList();
    	}
    	finally {
    		em.close();
    	}
    }

    public void borrarCaballo(Caballo caballo) {
    	EntityManager em = JPAUtil.getInstance().crearEntityManager();
    	try {
    		em.getTransaction().begin();
    		Caballo managed = em.merge(caballo);
    		em.remove(managed);
    		em.getTransaction().commit();
    	}
    	catch(RuntimeException e) {
    		if (em.getTransaction().isActive()) {
    			em.getTransaction().rollback();
    		}
    		throw e;
    	}
    	finally {
    		em.close();
    	}
    }

}
