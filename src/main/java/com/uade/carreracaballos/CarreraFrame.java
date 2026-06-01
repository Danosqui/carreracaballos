package com.uade.carreracaballos.ui;

import com.uade.carreracaballos.controller.JugadorController;
import com.uade.carreracaballos.dto.JugadorDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CarreraFrame extends JFrame {

    private JugadorController controlador;

    private JTextField campoNombre;
    private JTextField campoMail;
    private JButton botonCrear;
    private JButton botonSeleccionar;
    private JTable tablaJugadores;
    private DefaultTableModel modeloTabla;

    public CarreraFrame() {

        controlador = new JugadorController();

        configurarVentana();
        crearComponentes();
        cargarJugadores();
    }

    private void configurarVentana() {

        setTitle("Gestión de Jugadores");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void crearComponentes() {

        JPanel panelFormulario = new JPanel();

        panelFormulario.setBorder(BorderFactory.createTitledBorder("Nuevo Jugador"));

        panelFormulario.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel textoNombre = new JLabel("Nombre");
        campoNombre = new JTextField();

        JLabel textoMail = new JLabel("Mail");
        campoMail = new JTextField();

        botonCrear = new JButton("Crear Jugador");
        botonSeleccionar = new JButton("Seleccionar Jugador");

        panelFormulario.add(textoNombre);
        panelFormulario.add(campoNombre);

        panelFormulario.add(textoMail);
        panelFormulario.add(campoMail);

        panelFormulario.add(botonCrear);
        panelFormulario.add(botonSeleccionar);

        add(panelFormulario, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel();

        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Mail");
        modeloTabla.addColumn("Puntaje");

        tablaJugadores = new JTable(modeloTabla);

        JScrollPane scrollTabla = new JScrollPane(tablaJugadores);

        add(scrollTabla, BorderLayout.CENTER);

        botonCrear.addActionListener(e -> crearJugador());
        botonSeleccionar.addActionListener(e -> seleccionarJugador());
    }

    private void crearJugador() {

        String nombre = campoNombre.getText().trim();
        String mail = campoMail.getText().trim();

        if (nombre.isEmpty() || mail.isEmpty()) {

            JOptionPane.showMessageDialog(this, "Complete todos los campos");

            return;
        }

        try {

            controlador.nuevoJugador(nombre, mail);
            JOptionPane.showMessageDialog(this, "Jugador creado correctamente");

            limpiarCampos();
            cargarJugadores();

        } catch (Exception error) {

            JOptionPane.showMessageDialog(this, error.getMessage());
        }
    }

    private void cargarJugadores() {

        modeloTabla.setRowCount(0);
        List<JugadorDTO> listaJugadores = controlador.listarJugadores();

        for (JugadorDTO jugador : listaJugadores) {
            Object[] fila = {
                    jugador.getNombre(),
                    jugador.getMail(),
                    jugador.getPuntaje()
            };

            modeloTabla.addRow(fila);
        }
    }

    private void seleccionarJugador() {

        int filaSeleccionada = tablaJugadores.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un jugador");
            
            return;
        }

        String nombre = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
        String mail = modeloTabla.getValueAt(filaSeleccionada, 1).toString();

        try {

            controlador.seleccionarJugador(nombre, mail);
            JOptionPane.showMessageDialog(this, "Jugador seleccionado");

        } catch (Exception error) {

            JOptionPane.showMessageDialog(this, error.getMessage());
        }
    }

    private void limpiarCampos() {

        campoNombre.setText("");
        campoMail.setText("");
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            CarreraFrame ventana = new CarreraFrame();

            ventana.setVisible(true);
        });
    }
}