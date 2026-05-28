package com.uade.carreracaballos.view;
import javax.swing.JPanel;
import java.awt.Image;        // clase base para imágenes
import java.awt.Graphics;     // para paintComponent
import javax.swing.ImageIcon; // para cargar el archivo

public class ViewCaballo extends JPanel {

    private Image sprite;

    public ViewCaballo() {
        java.net.URL url = getClass().getResource("/com/uade/carreracaballos/view/assets/caballo3.png");
        if (url == null) {
            System.err.println("ERROR: no se encontró el sprite del caballo en el classpath");
        } else {
            sprite = new ImageIcon(url).getImage();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(sprite, 50, 50, 50, 50, this);
    }
}