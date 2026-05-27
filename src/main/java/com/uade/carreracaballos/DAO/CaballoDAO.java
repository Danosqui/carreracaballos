package com.uade.carreracaballos.DAO;

import com.uade.carreracaballos.model.Caballo;
import com.uade.carreracaballos.model.CaballoEquilibrado;
import com.uade.carreracaballos.model.CaballoResistente;
import com.uade.carreracaballos.model.CaballoVeloz;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CaballoDAO {

    public void crearCaballo(Caballo caballo) {
        String tipo;
        if (caballo instanceof CaballoVeloz)        tipo = "VELOZ";
        else if (caballo instanceof CaballoResistente) tipo = "RESISTENTE";
        else                                           tipo = "EQUILIBRADO";

        String sql = "INSERT INTO caballos(nombre, tipo, velocidad, resistencia, energia, distanciaRecorrida) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, caballo.getNombre());
            stmt.setString(2, tipo);
            stmt.setDouble(3, caballo.getVelocidad());
            stmt.setDouble(4, caballo.getResistencia());
            stmt.setDouble(5, caballo.getEnergia());
            stmt.setDouble(6, caballo.getDistanciaRecorrida());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear caballo", e);
        }
    }

    public List<Caballo> listarCaballos() {
        List<Caballo> caballos = new ArrayList<>();
        String sql = "SELECT * FROM caballos ORDER BY id";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Caballo c = construirCaballo(rs);
                if (c != null) caballos.add(c);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar caballos", e);
        }

        return caballos;
    }

    public List<Caballo> getRandomCaballos(int idExcluir) {
        List<Caballo> caballos = new ArrayList<>();
        String sql = "SELECT * FROM caballos WHERE id != ? ORDER BY RAND() LIMIT 5";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idExcluir);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Caballo c = construirCaballo(rs);
                    if (c != null) caballos.add(c);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener caballos aleatorios", e);
        }

        return caballos;
    }

    public void borrarCaballo(int caballoId) {
        String sql = "DELETE FROM caballos WHERE id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, caballoId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al borrar caballo", e);
        }
    }

    private Caballo construirCaballo(ResultSet rs) throws SQLException {
        String tipo        = rs.getString("tipo");
        String nombre      = rs.getString("nombre");
        double velocidad   = rs.getDouble("velocidad");
        double resistencia = rs.getDouble("resistencia");

        switch (tipo) {
            case "VELOZ":       return new CaballoVeloz(nombre, velocidad, resistencia);
            case "RESISTENTE":  return new CaballoResistente(nombre, velocidad, resistencia);
            case "EQUILIBRADO": return new CaballoEquilibrado(nombre, velocidad, resistencia);
            default:
                System.err.println("Tipo desconocido: " + tipo);
                return null;
        }
    }
}
