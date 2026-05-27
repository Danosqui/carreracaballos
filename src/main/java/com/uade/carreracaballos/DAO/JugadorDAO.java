package com.uade.carreracaballos.DAO;

import com.uade.carreracaballos.dto.JugadorDTO;
import com.uade.carreracaballos.model.Jugador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JugadorDAO {

    public void crearJugador(Jugador jugador) {
        String sql = "INSERT INTO jugadores(nombre, mail, puntaje) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, jugador.getNombre());
            stmt.setString(2, jugador.getMail());
            stmt.setInt(3, jugador.getPuntajeAcumulado());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear jugador", e);
        }
    }

    public Jugador buscarJugador(String nombre) {
        String sql = "SELECT * FROM jugadores WHERE nombre = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Jugador(
                            rs.getString("nombre"),
                            rs.getString("mail"),
                            rs.getInt("puntaje")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar jugador", e);
        }

        return null;
    }

    public List<JugadorDTO> listarJugadores() {
        List<JugadorDTO> jugadores = new ArrayList<>();
        String sql = "SELECT * FROM jugadores ORDER BY puntaje DESC";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                jugadores.add(new JugadorDTO(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("mail"),
                        rs.getInt("puntaje")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar jugadores", e);
        }

        return jugadores;
    }

    public void actualizarJugador(Jugador jugador) {
        String sql = "UPDATE jugadores SET puntaje = ? WHERE mail = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, jugador.getPuntajeAcumulado());
            stmt.setString(2, jugador.getMail());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar jugador", e);
        }
    }
}
