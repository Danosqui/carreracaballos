package com.uade.carreracaballos.DAO;

import com.uade.carreracaballos.model.Jugador;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import com.uade.carreracaballos.config.JPAUtil;
import com.uade.carreracaballos.interfaz.IJugadorDAO;

import java.util.List;

public class JugadorDAO implements IJugadorDAO {
	@Override
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
	@Override
    public Jugador getJugadorById(int id) {
    	EntityManager enti = JPAUtil.getInstance().crearEntityManager();
    	try {
    		return enti.find(Jugador.class, id);
    	}
    	catch(NoResultException e) {
    		return null;
    	}
    	finally {
    		enti.close();
    	}
    }
	@Override
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
	@Override
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
	@Override
    public void borrarJugador(Jugador jugador) {
    	EntityManager em = JPAUtil.getInstance().crearEntityManager();
    	try {
    		em.getTransaction().begin();
    		Jugador managed = em.merge(jugador);
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