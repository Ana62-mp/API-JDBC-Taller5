package com.krakedev.apijdbc.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.krakedev.apijdbc.entidades.Videojuego;
import com.krakedev.apijdbc.jdbc.VideojuegoJdbc;

@Service
public class VideojuegoService {


    public boolean insertar(Videojuego v) {
        return VideojuegoJdbc.insertar(v);
    }

    public Videojuego buscar(String codigo) {
        return VideojuegoJdbc.buscarPorCodigo(codigo);
    }

    public ArrayList<Videojuego> listar() {
        return VideojuegoJdbc.listar();
    }

    public void actualizar(String codigo, Videojuego videojuego) {
    	VideojuegoJdbc.actualizar(codigo, videojuego);
    }

    public boolean eliminar(String codigo) {
        return VideojuegoJdbc.eliminar(codigo);
    }
}