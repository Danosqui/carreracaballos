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
import java.util.ArrayList;
import java.util.List;

public class MenuFrame extends JFrame {

    private JugadorController jugadorCont;
    private CarreraController carreraCont;
    private CaballoController caballoCont;

    private JButton btnCrearJugador;
    private JButton btnEliminarJugador;
    private JButton btnCrearCaballo;
    private JButton btnEliminarCaballo;
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

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 3));
        btnCrearJugador = new JButton("Crear Jugador");
        btnEliminarJugador = new JButton("Eliminar Jugador");
        btnEliminarJugador.setEnabled(false);

        panelAcciones.add(btnCrearJugador);
        panelAcciones.add(btnEliminarJugador);

        panelInferior.add(panelSeleccionado);
        panelInferior.add(panelAcciones);
        panelJugador.add(panelInferior, BorderLayout.SOUTH);
        panelContenedor.add(panelJugador);

        btnCrearJugador.addActionListener(e -> crearJugador());
        btnEliminarJugador.addActionListener(e -> eliminarJugador());
        tablaJugadores.getSelectionModel().addListSelectionListener(e -> procesarSeleccionJugador());
        
        cargarJugadores();
    }
    
    private void crearJugador() {
        JTextField campoNombre = new JTextField();
        JTextField campoMail   = new JTextField();
        Object[] campos = {
            "Nombre:", campoNombre,
            "Mail:",   campoMail
        };

        int resultado = JOptionPane.showConfirmDialog(
            this, campos, "Crear Jugador", JOptionPane.OK_CANCEL_OPTION);

        if (resultado != JOptionPane.OK_OPTION) return;

        String nombre = campoNombre.getText().trim();
        String mail   = campoMail.getText().trim();

        if (nombre.isBlank()) {
            JOptionPane.showMessageDialog(this, "Ingresaste un nombre invalido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (mail.isBlank()) {
            JOptionPane.showMessageDialog(this, "Ingresaste un mail invalido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        jugadorCont.nuevoJugador(nombre, mail);
        cargarJugadores();
        //se selecciona el jugador q acabas de crear
        int ultimaRow= tablaJugadores.getRowCount() -1;
        tablaJugadores.setRowSelectionInterval(ultimaRow, ultimaRow);
        procesarSeleccionJugador();
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

    private void eliminarJugador() {
        int fila = tablaJugadores.getSelectedRow();
        if (fila == -1) return;
        int id = Integer.parseInt(modeloTablaJugadores.getValueAt(fila, 0).toString());
        String nombre = modeloTablaJugadores.getValueAt(fila, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(
            this, "¿Eliminar al jugador '" + nombre + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        jugadorCont.eliminarJugador(id);
        btnEliminarJugador.setEnabled(false);
        nombreJugSelec.setText("-");
        mailJugSelec.setText("-");
        cargarJugadores();
        cargarCaballos();
    }

    private void procesarSeleccionJugador() {
        int filaSeleccionada = tablaJugadores.getSelectedRow();
        if (filaSeleccionada == -1) {
            // NO METER CODIGO ACA.
            return;
        }

        int id = Integer.parseInt(modeloTablaJugadores.getValueAt(filaSeleccionada, 0).toString());
        String nombre = modeloTablaJugadores.getValueAt(filaSeleccionada, 1).toString();
        String mail = modeloTablaJugadores.getValueAt(filaSeleccionada, 2).toString();

        try {

            jugadorCont.seleccionarJugador(id);
            nombreJugSelec.setText(nombre);
            mailJugSelec.setText(mail);
            btnEliminarJugador.setEnabled(true);
            if (tablaCaballos.getSelectedRow()!=-1) {
            	procesarSeleccionCaballo();
            }

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
        //modeloTablaCaballos.addColumn("Energía");

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

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 3));
        btnCrearCaballo = new JButton("Crear Caballo");
        btnEliminarCaballo = new JButton("Eliminar Caballo");
        btnEliminarCaballo.setEnabled(false);
        panelAcciones.add(btnCrearCaballo);
        panelAcciones.add(btnEliminarCaballo);

        panelInferior.add(panelSeleccionado);
        panelInferior.add(panelAcciones);
        panelCaballos.add(panelInferior, BorderLayout.SOUTH);
        panelContenedor.add(panelCaballos);

        btnCrearCaballo.addActionListener(e -> crearCaballo());
        btnEliminarCaballo.addActionListener(e -> eliminarCaballo());
        tablaCaballos.getSelectionModel().addListSelectionListener(e -> procesarSeleccionCaballo());

        cargarCaballos();
    }

    private void crearCaballo() {
        JTextField campoNombre = new JTextField();
        
        JComboBox<String> comboTipo = new JComboBox<>(caballoCont.listarAtributosCaballo());

        Object[] campos = {
            "Nombre:", campoNombre,
            "Tipo:",   comboTipo
        };

        int resultado = JOptionPane.showConfirmDialog(
            this, campos, "Crear Caballo", JOptionPane.OK_CANCEL_OPTION);

        if (resultado != JOptionPane.OK_OPTION) return;

        String nombre = campoNombre.getText().trim();
        if (nombre.isBlank()) {
            JOptionPane.showMessageDialog(this, "Ingresaste un nombre invalido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String atributo = (String) comboTipo.getSelectedItem();
        caballoCont.crearCaballo(atributo, nombre);
        cargarCaballos();
      
        JugadorDTO jSeleccionado = jugadorCont.getJugadorSeleccionado();
        if (jSeleccionado != null) {
        	int caballoSeleccionado = jSeleccionado.getCaballoSeleccionadoId();
        	if (caballoSeleccionado != -1) {
        		for (int i = 0; i < tablaCaballos.getRowCount(); i++) {
        			int idFila = Integer.parseInt(modeloTablaCaballos.getValueAt(i, 0).toString());
        			if (idFila == caballoSeleccionado) {
        				tablaCaballos.setRowSelectionInterval(i, i);
        			}
        	        
        		}
        	}
        }
    }

    private void cargarCaballos() {

        modeloTablaCaballos.setRowCount(0);
        List<CaballoDTO> listaCaballos = caballoCont.listarCaballos();

        for (CaballoDTO caballo : listaCaballos) {
            Object[] fila = {
                caballo.getId(),
                caballo.getNombre(),
                String.format("%.2f", caballo.getVelocidad()),
                String.format("%.2f", caballo.getResistencia())
            };

            modeloTablaCaballos.addRow(fila);
        }
    }

    private void eliminarCaballo() {
        int fila = tablaCaballos.getSelectedRow();
        if (fila == -1) return;
        int id = Integer.parseInt(modeloTablaCaballos.getValueAt(fila, 0).toString());
        String nombre = modeloTablaCaballos.getValueAt(fila, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(
            this, "¿Eliminar el caballo '" + nombre + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        caballoCont.eliminarCaballo(id);
        jugadorCont.seleccionarCaballo(-1);
		btnEliminarCaballo.setEnabled(false);
		nombreCabSelec.setText("-");
		velocidadCabSelec.setText("-");

        cargarCaballos();
    }

    private void procesarSeleccionCaballo() {
        int filaSeleccionada = tablaCaballos.getSelectedRow();
        if (filaSeleccionada == -1) {
        	// NO METER CODIGO ACA.
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

        btnEliminarCaballo.setEnabled(true);

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


        Integer[] tamanosPista = { 200, 250, 300, 350, 400, 450, 500 };
        Integer tamanioPista = (Integer) JOptionPane.showInputDialog(
                this,
                "Elegí el tamaño de la pista (metros):",
                "Tamaño de pista",
                JOptionPane.QUESTION_MESSAGE,
                null,
                tamanosPista,
                tamanosPista[0]
        );
        if (tamanioPista == null) return;

        carreraCont.crearCarrera(caballoSeleccionadoId, tamanioPista);
        
        VentanaCarrera ventanaCarrera = new VentanaCarrera();
        javax.swing.SwingUtilities.invokeLater(() -> ventanaCarrera.setVisible(true));
        this.setVisible(false);

        carreraCont.iniciarCarrera(); //realmente hace falta esto? lo unico q hace es estadocarrera=en curso

        Timer timer = new Timer(32, null);

        timer.addActionListener(e -> {
        	if (carreraCont.carreraFinalizada()) {
        		timer.stop();
        		int puesto = carreraCont.calcularPuesto(caballoSeleccionadoId);
        		jugadorCont.procesarPuntaje(puesto);
        		
                String mensaje;
                
                if (puesto == 1) mensaje = "Felicidades, tu caballo gano la carrera.";
                else mensaje = "Tu caballo salio en "+puestoATexto(puesto)+" puesto.";
      
                JOptionPane.showMessageDialog(this, mensaje, "Carrera finalizada", JOptionPane.INFORMATION_MESSAGE);
                
                cargarJugadores();
                ventanaCarrera.setVisible(false);
                this.setVisible(true);
        	} else {
                carreraCont.avanzarInstante();
                List<CaballoDTO> posis = carreraCont.obtenerPosiciones();
                ventanaCarrera.getPista().actualizar(posis, tamanioPista, caballoSeleccionadoId);
        	}
        });
        timer.start();
    }
    
    private String puestoATexto(int puesto) {
    	String txt = puesto+"";
    	
    	if (puesto == 1 || puesto == 3) txt+="ro";
    	else if (puesto == 2) txt+="do";
    	else txt+="to";
    	
    	return txt;
    	
    }

}
