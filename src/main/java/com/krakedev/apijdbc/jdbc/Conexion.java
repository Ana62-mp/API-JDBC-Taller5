package com.krakedev.apijdbc.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Conexion {

    private static final Logger log = LoggerFactory.getLogger(Conexion.class);

    private static final String URL = "jdbc:postgresql://localhost:5432/apijdbc";  //jdbc:mysql://localhost:3306/apijdbc
    private static final String USER = "postgres";
    private static final String PASSWORD = "c0tton14";

    public static Connection obtenerConexion() {
        try {
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

            log.info("Conexion realizada exitosamente");
            return con;

        } catch (SQLException e) {
            log.error("Error estableciendo conexion " + e.getMessage());

            throw new RuntimeException("No se pudo establecer conexion");
        }
    }

    public static void main(String[] args) {
        Connection con = null;

        try {
            con = obtenerConexion();

        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    log.error("Error cerrando conexion " + e.getMessage());
                }
            }
        }
    }
}