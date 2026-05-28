package com.uade.carreracaballos.view;

import javax.swing.*;

import com.uade.carreracaballos.view.ViewPista;


/**
 * Ventana Swing de la carrera.
 *
 * Por ahora se abre vacia: aca podes ir armando la vista de la pista, los caballos, etc.
 */
public class VentanaCarrera extends JFrame {

    public VentanaCarrera() {
        setTitle("Carrera de Caballos");
        setSize(864, 458);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        ViewPista pistaui = new ViewPista();
        
        this.add(pistaui);
    
        // TODO: agregar los componentes de la carrera (pista, caballos, etc.)
    }
}
