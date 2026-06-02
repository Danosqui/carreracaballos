package com.uade.carreracaballos.view;

import com.uade.carreracaballos.controller.JugadorController;
import com.uade.carreracaballos.controller.CaballoController;
import com.uade.carreracaballos.controller.CarreraController;

import com.uade.carreracaballos.model.AtributoCaballo;

import com.uade.carreracaballos.dto.JugadorDTO;
import com.uade.carreracaballos.dto.CaballoDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MenuFrame extends JFrame {

    private JugadorController jugadorCont;
    private CarreraController carreraCont;
    private CaballoController caballoCont;

    private JButton btnCrearJugador;
    private JButton btnCrearCaballo;
    private JButton btnIniciarCarrera;
    private JTable tablaJugadores, tablaCaballos;
    private DefaultTableModel modeloTablaJugadores, modeloTablaCaballos;

    private JLabel nombreJugSelec, mailJugSelec;
    private JLabel nombreCabSelec, velocidadCabSelec;

    private JPanel panelContenedor;

    public MenuFrame() {

        jugadorCont = new JugadorController();
        carreraCont = new CarreraController();
        caballoCont = new CaballoController();

        configurarVentana();
        crearPanelJugador();
        crearPanelCaballos();
        crearPanelIniciar();
    }

    private void configurarVentana() {

        setTitle("Gestión de Jugadores");
        setSize(550, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panelContenedor = new JPanel(new GridLayout(2, 1, 10, 10));
        panelContenedor.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(panelContenedor, BorderLayout.CENTER);
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
        
        modeloTablaJugadores.addColumn("ID");
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
        panelContenedor.add(panelJugador);
        
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
        List<JugadorDTO> listaJugadores = jugadorCont.listarJugadores();

        for (JugadorDTO jugador : listaJugadores) {
            Object[] fila = {
            		jugador.getId(),
                    jugador.getNombre(),
                    jugador.getMail(),
                    jugador.getPuntaje()
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

        int id = Integer.parseInt(modeloTablaJugadores.getValueAt(filaSeleccionada, 0).toString());
        String nombre = modeloTablaJugadores.getValueAt(filaSeleccionada, 1).toString();
        String mail = modeloTablaJugadores.getValueAt(filaSeleccionada, 2).toString();

        try {

            jugadorCont.seleccionarJugador(id);
            nombreJugSelec.setText(nombre);
            mailJugSelec.setText(mail);

        } catch (Exception error) {

            JOptionPane.showMessageDialog(this, error.getMessage());
        }
    }

    private void crearPanelCaballos() {
        JPanel panelCaballos = new JPanel(new BorderLayout(10, 10));
        panelCaballos.setBorder(BorderFactory.createTitledBorder("Caballo"));

        modeloTablaCaballos = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        modeloTablaCaballos.addColumn("ID");
        modeloTablaCaballos.addColumn("Nombre");
        modeloTablaCaballos.addColumn("Velocidad");
        modeloTablaCaballos.addColumn("Resistencia");
        modeloTablaCaballos.addColumn("Energía");

        tablaCaballos = new JTable(modeloTablaCaballos);
        tablaCaballos.setPreferredScrollableViewportSize(new Dimension(450, 120));

        JScrollPane scrollTabla = new JScrollPane(tablaCaballos);
        panelCaballos.add(scrollTabla, BorderLayout.CENTER);


        JPanel panelInferior = new JPanel(new GridLayout(1, 2, 10, 10));

        JPanel panelSeleccionado = new JPanel(new GridLayout(2, 2, 5, 5));
        panelSeleccionado.setBorder(BorderFactory.createTitledBorder("Caballo seleccionado"));
        panelSeleccionado.add(new JLabel("Nombre:"));
        nombreCabSelec = new JLabel("-");
        panelSeleccionado.add(nombreCabSelec);
        panelSeleccionado.add(new JLabel("Velocidad:"));
        velocidadCabSelec = new JLabel("-");
        panelSeleccionado.add(velocidadCabSelec);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCrearCaballo = new JButton("Crear Caballo");
        panelAcciones.add(btnCrearCaballo);

        panelInferior.add(panelSeleccionado);
        panelInferior.add(panelAcciones);
        panelCaballos.add(panelInferior, BorderLayout.SOUTH);
        panelContenedor.add(panelCaballos);

        btnCrearCaballo.addActionListener(e -> crearCaballo());
        tablaCaballos.getSelectionModel().addListSelectionListener(e -> seleccionarCaballo());

        cargarCaballos();
    }

    private void crearCaballo() {
    	String nombre = JOptionPane.showInputDialog(this, "Ingresá el nombre del caballo:");

    	if (nombre == null || nombre.isBlank()) {
    		JOptionPane.showMessageDialog(
				    this,
				    "Ingresaste un nombre invalido.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				);
    	    return;
    	}

    	AtributoCaballo[] opciones = AtributoCaballo.values();
    	AtributoCaballo atributo = (AtributoCaballo) JOptionPane.showInputDialog(
				this,
				"Elegí el tipo de caballo:",
				"Tipo",
				JOptionPane.QUESTION_MESSAGE,
				null,
				opciones,
				opciones[0]
			);

    	if (atributo == null) {
    		JOptionPane.showMessageDialog(
				    this,
				    "Tipo de caballo invalido.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				);

    		return;
    	}

    	caballoCont.crearCaballo(atributo, nombre);
    	cargarCaballos();

    }

    private void cargarCaballos() {

        modeloTablaCaballos.setRowCount(0);
        List<CaballoDTO> listaCaballos = caballoCont.listarCaballos();

        for (CaballoDTO caballo : listaCaballos) {
            Object[] fila = {
            		caballo.getId(),
                    caballo.getNombre(),
                    caballo.getVelocidad(),
                    caballo.getResistencia(),
                    caballo.getEnergia()
            };

            modeloTablaCaballos.addRow(fila);
        }
    }

    private void seleccionarCaballo() {
        int filaSeleccionada = tablaCaballos.getSelectedRow();
        if (filaSeleccionada == -1) {
            return;
        }
        
        JugadorDTO jugadorSeleccionado = jugadorCont.getJugadorSeleccionado();
        if (jugadorSeleccionado == null) {
        	JOptionPane.showMessageDialog(
			    this,
			    "Primero debes seleccionar un jugador.",
			    "Error",
			    JOptionPane.ERROR_MESSAGE
			);
        	tablaCaballos.clearSelection();
        	
        	return;
        }

        String nombre = modeloTablaCaballos.getValueAt(filaSeleccionada, 1).toString();
        String velocidad = modeloTablaCaballos.getValueAt(filaSeleccionada, 2).toString();
        int id = Integer.parseInt(modeloTablaCaballos.getValueAt(filaSeleccionada, 0).toString());
        
        nombreCabSelec.setText(nombre);
        velocidadCabSelec.setText(velocidad);
        
        jugadorCont.seleccionarCaballo(id);
    }

    private void crearPanelIniciar() {
        JPanel panelIniciar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnIniciarCarrera = new JButton("Iniciar Carrera");
        panelIniciar.add(btnIniciarCarrera);
        add(panelIniciar, BorderLayout.SOUTH);
        btnIniciarCarrera.addActionListener(e->iniciarCarrera());
    }
    
    private void iniciarCarrera() {
        JugadorDTO jugadorSeleccionado = jugadorCont.getJugadorSeleccionado();

        if (jugadorSeleccionado == null) {
            JOptionPane.showMessageDialog(
			    this,
			    "No seleccionaste un jugador.",
			    "Error",
			    JOptionPane.ERROR_MESSAGE
			);

            return;
        }

        int caballoSeleccionadoId = jugadorSeleccionado.getCaballoSeleccionadoId();

        if (caballoSeleccionadoId == -1) {
            JOptionPane.showMessageDialog(
                this,
                "El jugador seleccionado no tiene un caballo asignado.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        carreraCont.iniciarCarrera();
    }

}