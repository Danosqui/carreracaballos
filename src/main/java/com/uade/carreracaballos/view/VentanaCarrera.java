package com.uade.carreracaballos.view;

import javax.swing.*;

import com.uade.carreracaballos.view.ViewPista;

public class VentanaCarrera extends JFrame {

    private final ViewPista pistaui = new ViewPista();

    public VentanaCarrera() {
        setTitle("Carrera de Caballos");
        setSize(986, 458);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.add(pistaui);
    }
    
    public ViewPista getPista() {
        return pistaui;
    }
}
