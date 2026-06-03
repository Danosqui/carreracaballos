package com.uade.carreracaballos.view;
import javax.swing.JPanel;
import java.awt.Image;        // clase base para imágenes
import java.awt.Graphics;     // para paintComponent
import java.awt.Color;
import java.util.List;
import javax.swing.ImageIcon; // para cargar el archivo

import com.uade.carreracaballos.dto.CaballoDTO;
import com.uade.carreracaballos.model.Caballo;

public class ViewPista extends JPanel {

    private Image fondo;
    private Image[] caballos = new Image[6];

    int caballoSize = 60;

    private static final int X_INICIAL = 50;
    private static final int X_META    = 755;

    private List<CaballoDTO> participantes;    private double[] distancias;       // distancia (posiblemente interpolada) por caballo
    private int longitudPista = 1;
    //private Caballo miCaballo;

    public ViewPista() {
    	// imagen 1693x929, ratio
        java.net.URL urlPista = getClass().getResource("/com/uade/carreracaballos/view/assets/pista.png");
        java.net.URL urlCaballo = getClass().getResource("/com/uade/carreracaballos/view/assets/caballo3.png");

        fondo = new ImageIcon(urlPista).getImage();
        for (int i=0;i<caballos.length;i++) {
        	caballos[i] = new ImageIcon(urlCaballo).getImage();
        }
    }


    public void actualizar(List<CaballoDTO> participantes, int longitudPista) {
        this.participantes = participantes;
        this.longitudPista = Math.max(1, longitudPista);
        
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo, 0, 0, 864, 458, this);

        if (participantes == null) {
            return;
        }

        // Como mucho 6 caballos (hay 6 carriles / imagenes).
        int n = Math.min(participantes.size(), caballos.length);
        for (int i = 0; i < n; i++) {
            CaballoDTO c = participantes.get(i);
            System.out.println(c.getDistanciaRecorrida()+" "+c.getNombre());

            // Avance proporcional: 0 -> X_INICIAL, longitudPista -> X_META.
            double ratio = c.getDistanciaRecorrida() / longitudPista;
            ratio = Math.max(0.0, Math.min(1.0, ratio));
            int x = X_INICIAL + (int) Math.round((X_META - X_INICIAL) * ratio);
            int y = 90 + 35 * i;

            g.drawImage(caballos[i], x, y, caballoSize, caballoSize, this);

            String etiqueta = c.getNombre();
            //g.setColor(c == miCaballo ? Color.YELLOW : Color.WHITE);
            g.drawString(etiqueta, X_INICIAL, y + 12);
        }
    }
}
