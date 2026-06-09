package com.uade.carreracaballos;

import javax.swing.SwingUtilities;

import com.uade.carreracaballos.view.MenuFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuFrame().setVisible(true));
    }
}
