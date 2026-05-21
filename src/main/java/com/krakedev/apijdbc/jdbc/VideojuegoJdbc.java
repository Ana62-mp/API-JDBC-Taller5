package com.krakedev.apijdbc.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.krakedev.apijdbc.entidades.Videojuego;

public class VideojuegoJdbc {

    private static final Logger log = LoggerFactory.getLogger(VideojuegoJdbc.class);

    public void insertar(Videojuego videojuego) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = Conexion.obtenerConexion();

            String sql = "INSERT INTO videojuegos "
                    + "(codigo, nombre, plataforma, precio, disponible, genero) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            ps = con.prepareStatement(sql);
            ps.setString(1, videojuego.getCodigo());
            ps.setString(2, videojuego.getNombre());
            ps.setString(3, videojuego.getPlataforma());
            ps.setDouble(4, videojuego.getPrecio());
            ps.setBoolean(5, videojuego.isDisponible());
            ps.setString(6, videojuego.getGenero());

            ps.executeUpdate();

            log.info("Videojuego insertado correctamente: " + videojuego.getCodigo());

        } catch (SQLException e) {
            log.error("Error al insertar videojuego: " + e.getMessage(), e);
            throw new RuntimeException("No se pudo insertar el videojuego");
        } finally {
            cerrarRecursos(null, ps, con);
        }
    }

    public ArrayList<Videojuego> listar() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList<Videojuego> videojuegos = new ArrayList<>();

        try {
            con = Conexion.obtenerConexion();

            String sql = "SELECT codigo, nombre, plataforma, precio, disponible, genero FROM videojuegos";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Videojuego videojuego = new Videojuego();
                videojuego.setCodigo(rs.getString("codigo"));
                videojuego.setNombre(rs.getString("nombre"));
                videojuego.setPlataforma(rs.getString("plataforma"));
                videojuego.setPrecio(rs.getDouble("precio"));
                videojuego.setDisponible(rs.getBoolean("disponible"));
                videojuego.setGenero(rs.getString("genero"));

                videojuegos.add(videojuego);
            }

            log.info("Videojuegos listados correctamente");

            return videojuegos;

        } catch (SQLException e) {
            log.error("Error al listar videojuegos: " + e.getMessage(), e);
            throw new RuntimeException("No se pudo listar los videojuegos");
        } finally {
            cerrarRecursos(rs, ps, con);
        }
    }

    public Videojuego buscarPorCodigo(String codigo) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = Conexion.obtenerConexion();

            String sql = "SELECT codigo, nombre, plataforma, precio, disponible, genero "
                    + "FROM videojuegos WHERE codigo = ?";

            ps = con.prepareStatement(sql);
            ps.setString(1, codigo);

            rs = ps.executeQuery();

            if (rs.next()) {
                Videojuego videojuego = new Videojuego();
                videojuego.setCodigo(rs.getString("codigo"));
                videojuego.setNombre(rs.getString("nombre"));
                videojuego.setPlataforma(rs.getString("plataforma"));
                videojuego.setPrecio(rs.getDouble("precio"));
                videojuego.setDisponible(rs.getBoolean("disponible"));
                videojuego.setGenero(rs.getString("genero"));

                log.info("Videojuego encontrado: " + codigo);

                return videojuego;
            } else {
                log.warn("No existe videojuego con codigo: " + codigo);
                throw new RuntimeException("No existe videojuego con codigo: " + codigo);
            }

        } catch (SQLException e) {
            log.error("Error al buscar videojuego: " + e.getMessage(), e);
            throw new RuntimeException("No se pudo buscar el videojuego");
        } finally {
            cerrarRecursos(rs, ps, con);
        }
    }

    public void actualizar(String codigo, Videojuego videojuego) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = Conexion.obtenerConexion();

            String sql = "UPDATE videojuegos "
                    + "SET nombre = ?, plataforma = ?, precio = ?, disponible = ?, genero = ? "
                    + "WHERE codigo = ?";

            ps = con.prepareStatement(sql);
            ps.setString(1, videojuego.getNombre());
            ps.setString(2, videojuego.getPlataforma());
            ps.setDouble(3, videojuego.getPrecio());
            ps.setBoolean(4, videojuego.isDisponible());
            ps.setString(5, videojuego.getGenero());
            ps.setString(6, codigo);

            int filasActualizadas = ps.executeUpdate();

            if (filasActualizadas == 0) {
                log.warn("No existe videojuego para actualizar con codigo: " + codigo);
                throw new RuntimeException("No existe videojuego con codigo: " + codigo);
            }

            log.info("Videojuego actualizado correctamente: " + codigo);

        } catch (SQLException e) {
            log.error("Error al actualizar videojuego: " + e.getMessage(), e);
            throw new RuntimeException("No se pudo actualizar el videojuego");
        } finally {
            cerrarRecursos(null, ps, con);
        }
    }

    public void eliminar(String codigo) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = Conexion.obtenerConexion();

            String sql = "DELETE FROM videojuegos WHERE codigo = ?";

            ps = con.prepareStatement(sql);
            ps.setString(1, codigo);

            int filasEliminadas = ps.executeUpdate();

            if (filasEliminadas == 0) {
                log.warn("No existe videojuego para eliminar con codigo: " + codigo);
                throw new RuntimeException("No existe videojuego con codigo: " + codigo);
            }

            log.info("Videojuego eliminado correctamente: " + codigo);

        } catch (SQLException e) {
            log.error("Error al eliminar videojuego: " + e.getMessage(), e);
            throw new RuntimeException("No se pudo eliminar el videojuego");
        } finally {
            cerrarRecursos(null, ps, con);
        }
    }

    private void cerrarRecursos(ResultSet rs, PreparedStatement ps, Connection con) {
        try {
            if (rs != null) {
                rs.close();
            }

            if (ps != null) {
                ps.close();
            }

            if (con != null) {
                con.close();
            }

        } catch (SQLException e) {
            log.error("Error cerrando recursos: " + e.getMessage(), e);
        }
    }
}