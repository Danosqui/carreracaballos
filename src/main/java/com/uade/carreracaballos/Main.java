package com.uade.carreracaballos;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CarreraFrame().setVisible(true));

        // Para ejecutar la CLI en vez de la GUI, comentar la linea de arriba y descomentar la de abajo:
        //CLI.main(args);
    }
}
