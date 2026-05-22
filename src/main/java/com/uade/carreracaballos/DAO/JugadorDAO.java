package com.uade.carreracaballos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.uade.carreracaballos.model.Jugador;

public class JugadorDAO {

    public void crearJugador(Jugador jugador) {

        String sql =
            "INSERT INTO jugadores(nombre, mail, puntaje) VALUES (?, ?, ?)";

        try {

            Connection conn =
                DBConnection
                    .getInstance()
                    .getConnection();

            PreparedStatement stmt =
                    conn.prepareStatement(sql);

            stmt.setString(1, jugador.getNombre());
            stmt.setString(2, jugador.getMail());
            stmt.setInt(3, jugador.getPuntajeAcumulado());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Jugador buscarJugador(String nombreJugador) {

        String sql =
            "SELECT * FROM jugadores WHERE nombre = ?";

        try {

            Connection conn =
                DBConnection
                    .getInstance()
                    .getConnection();

            PreparedStatement stmt =
                    conn.prepareStatement(sql);

            stmt.setString(1, nombreJugador);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Jugador jugador = new Jugador(
                        rs.getString("nombre"),
                        rs.getString("mail")
                );

                jugador.sumarPuntaje(
                        rs.getInt("puntaje")
                );

                return jugador;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void actualizarJugador(Jugador jugador) {

        String sql =
            "UPDATE jugadores SET puntaje = ? WHERE mail = ?";

        try {

            Connection conn =
                DBConnection
                    .getInstance()
                    .getConnection();

            PreparedStatement stmt =
                    conn.prepareStatement(sql);

            stmt.setInt(
                    1,
                    jugador.getPuntajeAcumulado()
            );

            stmt.setString(
                    2,
                    jugador.getMail()
            );

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}