package com.uade.carreracaballos.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "caballo")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_caballo")
public abstract class Caballo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int id;
	@Column(nullable=false, length=100)
    protected String nombre;
	@Column(nullable=false)
    protected double velocidad;
	@Transient
    protected double energia;
	@Column(nullable=false)
    protected double resistencia;
	@Transient
    protected double distanciaRecorrida;

    protected static final double K = 10.0;
    
    protected Caballo() {
    	//hibernate se queja si no hay un default constructor :/
    }

    public Caballo(String nombre) {
        this.nombre = nombre;
        this.energia = 100.0;
        this.distanciaRecorrida = 0.0;
    }

    public void avanzar() {
    	double avance = velocidad * (energia / 100.0);
        distanciaRecorrida += avance;
        double desgaste = (100.0 - resistencia) / K;
        energia = Math.max(resistencia, energia - desgaste);
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getVelocidad() { return velocidad; }
    public double getEnergia() { return energia; }
    public double getResistencia() { return resistencia; }
    public int getId() { return id;}

    public double getDistanciaRecorrida() { return distanciaRecorrida; }
    public void setDistanciaRecorrida(double distanciaRecorrida) { this.distanciaRecorrida = distanciaRecorrida; }
}
