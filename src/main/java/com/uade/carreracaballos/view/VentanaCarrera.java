package com.uade.carreracaballos.view;

import javax.swing.*;

import com.uade.carreracaballos.view.ViewPista;


/**
 * Ventana Swing de la carrera.
 *
 * Por ahora se abre vacia: aca podes ir armando la vista de la pista, los caballos, etc.
 */
public class VentanaCarrera extends JFrame {

    private final ViewPista pistaui = new ViewPista();

    public VentanaCarrera() {
        setTitle("Carrera de Caballos");
        setSize(986, 458);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.add(pistaui);
    }

    /** La pista donde se dibujan los caballos; la consola la refresca en cada instante. */
    public ViewPista getPista() {
        return pistaui;
    }
}
