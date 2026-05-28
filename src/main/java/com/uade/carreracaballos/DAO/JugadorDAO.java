package com.uade.carreracaballos.dao;

import com.uade.carreracaballos.model.Jugador;
import jakarta.persistence.EntityManager;
import com.uade.carreracaballos.config.JPAUtil;



import java.util.List;

public class JugadorDAO {

    public void crearJugador(Jugador jugador) {
    	EntityManager em = JPAUtil.getInstance().crearEntityManager();
    	try {
    		em.getTransaction().begin();
    		em.persist(jugador);
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

    public Jugador buscarJugador(String nombre) {
    	EntityManager enti = JPAUtil.getInstance().crearEntityManager();
    	try {
    		List<Jugador> resultados = enti.createQuery("SELECT j FROM Jugador j WHERE j.nombre = :nombre", Jugador.class)
    		.setParameter("nombre", nombre)
    		.setMaxResults(1)
    		.getResultList();
    		return resultados.isEmpty() ? null : resultados.get(0);
    	}
    	finally {
    		enti.close();
    	}
    }

    public List<Jugador> listarJugadores(){
    	EntityManager em = JPAUtil.getInstance().crearEntityManager();
    	try {
    		return em.createQuery("SELECT j FROM Jugador j", Jugador.class)
    				.getResultList();
    	}
    	finally {
    		em.close();
    	}
    }

    public void actualizarJugador(Jugador jugador) {
    	EntityManager enti = JPAUtil.getInstance().crearEntityManager();
    	try {
    		enti.getTransaction().begin();
    		enti.merge(jugador);
    		enti.getTransaction().commit();
    	}
    	catch(RuntimeException e){
            if (enti.getTransaction().isActive()) {
                enti.getTransaction().rollback();
            }
            throw e;
    	}
    	finally {
    		enti.close();
    	}
    }
    
    public void borrarJugador(Jugador jugador) {
    	EntityManager em = JPAUtil.getInstance().crearEntityManager();
    	try {
    		em.getTransaction().begin();
    		em.remove(em.merge(jugador));
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