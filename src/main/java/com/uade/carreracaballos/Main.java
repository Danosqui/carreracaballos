package com.uade.carreracaballos;

import javax.swing.SwingUtilities;

import com.uade.carreracaballos.view.MenuFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuFrame().setVisible(true));

        // Para ejecutar la CLI en vez de la GUI, comentar la linea de arriba y descomentar la de abajo:
        //CLI.main(args);
    }
}
