package com.uade.carreracaballos.view;
import javax.swing.JPanel;
import java.awt.Image;        // clase base para imágenes
import java.awt.Graphics;     // para paintComponent
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import javax.swing.ImageIcon; // para cargar el archivo

import com.uade.carreracaballos.dto.CaballoDTO;
import com.uade.carreracaballos.model.Caballo;

public class ViewPista extends JPanel {

    private Image fondo;
    private Image[] caballos = new Image[6];

    int caballoSize = 60;

    private static final int X_INICIAL = 130;
    private static final int X_META    = 880;

    private List<CaballoDTO> participantes;
    private int longitudPista;
    private int idCaballoJugador;

    public ViewPista() {
    	// imagen 1693x929, ratio
        java.net.URL urlPista = getClass().getResource("/com/uade/carreracaballos/view/assets/pista.png");

        fondo = new ImageIcon(urlPista).getImage();
        for (int i=0;i<caballos.length;i++) {
            java.net.URL urlCaballo = getClass().getResource("/com/uade/carreracaballos/view/assets/caballo"+(i+1)+".png");
        	caballos[i] = new ImageIcon(urlCaballo).getImage();
        }
    }


    public void actualizar(List<CaballoDTO> participantes, int longitudPista, int idCaballoJugador) {
        this.participantes = participantes;
        this.longitudPista = Math.max(1, longitudPista);
        this.idCaballoJugador = idCaballoJugador;
        
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo, 0, 0, 986, 458, this);

        if (participantes == null) {
            return;
        }

        // Como mucho 6 caballos (hay 6 carriles / imagenes).
        int n = Math.min(participantes.size(), caballos.length);
        for (int i = 0; i < n; i++) {
            CaballoDTO c = participantes.get(i);

            // Avance proporcional: 0 -> X_INICIAL, longitudPista -> X_META.
            double ratio = c.getDistanciaRecorrida() / longitudPista;
            ratio = Math.max(0.0, Math.min(1.0, ratio));
            int xCaballo = X_INICIAL + (int) Math.round((X_META - X_INICIAL) * ratio);
            int yCaballo = 90 + 35 * i;

            g.drawImage(caballos[i], xCaballo, yCaballo, caballoSize, caballoSize, this);

            String etiqueta = c.getNombre();
            g.setColor(c.getId() == idCaballoJugador ? Color.ORANGE : Color.WHITE);
            g.setFont(new Font ("Helvetica", 0, 16));
            g.drawString(etiqueta, 20, 123 + 37*i);
        }
    }
}
