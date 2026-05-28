package com.uade.carreracaballos.view;
import javax.swing.JPanel;
import java.awt.Image;        // clase base para imágenes
import java.awt.Graphics;     // para paintComponent
import javax.swing.ImageIcon; // para cargar el archivo

public class ViewPista extends JPanel {

    private Image fondo;
    private Image[] caballos = new Image[6];
    

    int caballoSize = 60;
    
    public ViewPista() {
    	// imagen 1693x929, ratio 
        java.net.URL urlPista = getClass().getResource("/com/uade/carreracaballos/view/assets/pista.png");
        java.net.URL urlCaballo = getClass().getResource("/com/uade/carreracaballos/view/assets/caballo3.png");

        fondo = new ImageIcon(urlPista).getImage();
        for (int i=0;i<caballos.length;i++) {
        	caballos[i] = new ImageIcon(urlCaballo).getImage();
        }
    }
    
    // x inicial = 50, 

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo, 0, 0, 864, 458, this);
        for (int i=0;i<caballos.length;i++) {
        	g.drawImage(caballos[i], 755, 90+35*i, caballoSize, caballoSize, this);
        }
        
    }
}