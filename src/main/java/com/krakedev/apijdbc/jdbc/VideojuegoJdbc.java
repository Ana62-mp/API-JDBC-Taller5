package com.krakedev.apijdbc.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.krakedev.apijdbc.entidades.Videojuego;
import com.krakedev.apijdbc.excepciones.VideojuegoDuplicadoException;
import com.krakedev.apijdbc.excepciones.VideojuegoNoEncontradoException;

public class VideojuegoJdbc {

	private static final Logger log = LoggerFactory.getLogger(VideojuegoJdbc.class);

	private static final String SQL_BUSCAR_POR_CODIGO = "SELECT codigo, nombre, plataforma, precio, disponible, genero "
			+ "FROM videojuegos WHERE codigo = ?";

	private static final String SQL_INSERTAR = "INSERT INTO videojuegos "
			+ "(codigo, nombre, plataforma, precio, disponible, genero) " + "VALUES (?, ?, ?, ?, ?, ?)";

	private static final String SQL_LISTAR = "SELECT codigo, nombre, plataforma, precio, disponible, genero "
			+ "FROM videojuegos";

	private static final String SQL_ACTUALIZAR = "UPDATE videojuegos "
			+ "SET nombre = ?, plataforma = ?, precio = ?, disponible = ?, genero = ? " + "WHERE codigo = ?";

	private static final String SQL_ELIMINAR = "DELETE FROM videojuegos WHERE codigo = ?";

	public static boolean insertar(Videojuego v) {
		Connection con = null;
		PreparedStatement psBuscar = null;
		PreparedStatement psInsertar = null;
		ResultSet rs = null;

		try {
			con = Conexion.obtenerConexion();

			psBuscar = con.prepareStatement(SQL_BUSCAR_POR_CODIGO);
			psBuscar.setString(1, v.getCodigo());

			rs = psBuscar.executeQuery();

			if (!rs.next()) {
				psInsertar = con.prepareStatement(SQL_INSERTAR);

				psInsertar.setString(1, v.getCodigo());
				psInsertar.setString(2, v.getNombre());
				psInsertar.setString(3, v.getPlataforma());
				psInsertar.setDouble(4, v.getPrecio());
				psInsertar.setBoolean(5, v.isDisponible());
				psInsertar.setString(6, v.getGenero());

				int filas = psInsertar.executeUpdate();

				log.info("Videojuego insertado. Filas afectadas: " + filas);

				return filas > 0 ? true : false;

			} else {
				throw new VideojuegoDuplicadoException("Está tratando de insertar un codigo que ya existe");
			}

		} catch (SQLException e) {
			log.error("Error al insertar videojuego: " + e.getMessage(), e);
			throw new RuntimeException("Error general al insertar el videojuego");
		} finally {
			cerrarRecursos(rs, psBuscar, null);
			cerrarRecursos(null, psInsertar, con);
		}
	}

	public static ArrayList<Videojuego> listar() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		ArrayList<Videojuego> videojuegos = new ArrayList<>();

		try {
			con = Conexion.obtenerConexion();

			ps = con.prepareStatement(SQL_LISTAR);
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

			if (videojuegos.isEmpty()) {
				throw new VideojuegoNoEncontradoException("No existe ningun videojuego");
			}
			log.info("Videojuegos listados correctamente");

			return videojuegos;

		} catch (SQLException e) {
			log.error("Error al listar videojuegos: " + e.getMessage(), e);
			throw new RuntimeException("Error general al listar los videojuegos");
		} finally {
			cerrarRecursos(rs, ps, con);
		}
	}

	public static Videojuego buscarPorCodigo(String codigo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = Conexion.obtenerConexion();

			ps = con.prepareStatement(SQL_BUSCAR_POR_CODIGO);
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
				throw new VideojuegoNoEncontradoException("No existe videojuego con codigo: " + codigo);
			}

		} catch (SQLException e) {
			log.error("Error al buscar videojuego: " + e.getMessage(), e);
			throw new RuntimeException("Error general al buscar el videojuego");
		} finally {
			cerrarRecursos(rs, ps, con);
		}
	}

	public static void actualizar(String codigo, Videojuego videojuego) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = Conexion.obtenerConexion();

			ps = con.prepareStatement(SQL_ACTUALIZAR);
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
			throw new RuntimeException("Error general al actualizar el videojuego");
		} finally {
			cerrarRecursos(null, ps, con);
		}
	}

	public static boolean eliminar(String codigo) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = Conexion.obtenerConexion();

			ps = con.prepareStatement(SQL_ELIMINAR);
			ps.setString(1, codigo);

			int filasEliminadas = ps.executeUpdate();

			if (filasEliminadas == 0) {
				log.warn("No existe videojuego para eliminar con codigo: " + codigo);
				throw new RuntimeException("No existe videojuego con codigo: " + codigo);
			}

			log.info("Videojuego eliminado correctamente: " + codigo);
			return filasEliminadas > 0 ? true : false;
		} catch (SQLException e) {
			log.error("Error al eliminar videojuego: " + e.getMessage(), e);
			throw new RuntimeException("Error general al eliminar el videojuego");
		} finally {
			cerrarRecursos(null, ps, con);
		}
	}

	private static void cerrarRecursos(ResultSet rs, PreparedStatement ps, Connection con) {
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