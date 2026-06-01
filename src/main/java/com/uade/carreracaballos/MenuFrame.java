package com.uade.carreracaballos;

import com.uade.carreracaballos.controller.JugadorController;
import com.uade.carreracaballos.controller.CaballoController;
import com.uade.carreracaballos.controller.CarreraController;
import com.uade.carreracaballos.model.Jugador;
import com.uade.carreracaballos.dto.JugadorDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MenuFrame extends JFrame {

    private JugadorController jugadorCont;
    private CarreraController carreraCont;
    private CaballoController caballoCont;

    private JButton btnCrearJugador;
    private JTable tablaJugadores, tablaCaballos;
    private DefaultTableModel modeloTablaJugadores, modeloTablaCaballos;
    
    private JLabel nombreJugSelec, mailJugSelec;

    public MenuFrame() {

        jugadorCont = new JugadorController();
        carreraCont = new CarreraController();
        caballoCont = new CaballoController();

        configurarVentana();
        crearPanelJugador();
    }

    private void configurarVentana() {

        setTitle("Gestión de Jugadores");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    
    private void crearPanelJugador() {
        JPanel panelJugador = new JPanel(new BorderLayout(10, 10));
        panelJugador.setBorder(BorderFactory.createTitledBorder("Jugador"));

        modeloTablaJugadores = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            } 
        };
        modeloTablaJugadores.addColumn("Nombre");
        modeloTablaJugadores.addColumn("Mail");
        modeloTablaJugadores.addColumn("Puntaje");

        tablaJugadores = new JTable(modeloTablaJugadores);
        tablaJugadores.setPreferredScrollableViewportSize(new Dimension(450, 120));
        JScrollPane scrollTabla = new JScrollPane(tablaJugadores);
        panelJugador.add(scrollTabla, BorderLayout.CENTER);


        JPanel panelInferior = new JPanel(new GridLayout(1, 2, 10, 10));

        JPanel panelSeleccionado = new JPanel(new GridLayout(2, 2, 5, 5));
        panelSeleccionado.setBorder(BorderFactory.createTitledBorder("Jugador seleccionado"));
        panelSeleccionado.add(new JLabel("Nombre:"));
        nombreJugSelec = new JLabel("-");
        panelSeleccionado.add(nombreJugSelec);
        panelSeleccionado.add(new JLabel("Mail:"));
        mailJugSelec = new JLabel("-");
        panelSeleccionado.add(mailJugSelec);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCrearJugador = new JButton("Crear Jugador");
        panelAcciones.add(btnCrearJugador);

        panelInferior.add(panelSeleccionado);
        panelInferior.add(panelAcciones);
        panelJugador.add(panelInferior, BorderLayout.SOUTH);
        add(panelJugador, BorderLayout.CENTER);
        
        btnCrearJugador.addActionListener(e -> crearJugador());
        tablaJugadores.getSelectionModel().addListSelectionListener(e->seleccionarJugador());
        
        cargarJugadores();
    }
    
   
    
    private void crearJugador() {
    	String nombre = JOptionPane.showInputDialog(this, "Ingresá el nombre del jugador:");

    	if (nombre == null || nombre.isBlank()) {
    		JOptionPane.showMessageDialog(
			    this,
			    "Ingresaste un nombre invalido.",
			    "Error",
			    JOptionPane.ERROR_MESSAGE
			);
    	    return;
    	}
    	
    	String mail = JOptionPane.showInputDialog(this, "Ingresá el email del jugador:");

    	if (mail == null || mail.isBlank()) {
    		JOptionPane.showMessageDialog(
			    this,
			    "Ingresaste un mail invalido.",
			    "Error",
			    JOptionPane.ERROR_MESSAGE
			);
    		
    		return;
    	}
    	
    	jugadorCont.nuevoJugador(nombre, mail);
    	cargarJugadores();
        
    }

    private void cargarJugadores() {

        modeloTablaJugadores.setRowCount(0);
        List<Jugador> listaJugadores = jugadorCont.listarJugadores();

        for (Jugador jugador : listaJugadores) {
            Object[] fila = {
                    jugador.getNombre(),
                    jugador.getMail(),
                    jugador.getPuntajeAcumulado()
            };

            modeloTablaJugadores.addRow(fila);
        }
    }

    private void seleccionarJugador() {
        int filaSeleccionada = tablaJugadores.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un jugador");
            
            return;
        }

        String nombre = modeloTablaJugadores.getValueAt(filaSeleccionada, 0).toString();
        String mail = modeloTablaJugadores.getValueAt(filaSeleccionada, 1).toString();

        try {

            jugadorCont.seleccionarJugador(nombre, mail);
            nombreJugSelec.setText(nombre);
            mailJugSelec.setText(mail);

        } catch (Exception error) {

            JOptionPane.showMessageDialog(this, error.getMessage());
        }
    }

}