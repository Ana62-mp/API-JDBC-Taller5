package com.krakedev.apijdbc.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krakedev.apijdbc.entidades.Videojuego;
import com.krakedev.apijdbc.excepciones.ErrorResponse;
import com.krakedev.apijdbc.excepciones.VideojuegoDuplicadoException;
import com.krakedev.apijdbc.excepciones.VideojuegoNoEncontradoException;
import com.krakedev.apijdbc.services.VideojuegoService;

@RestController
@RequestMapping("/videojuegos")
public class VideojuegoController {

    private final VideojuegoService service;

    public VideojuegoController(VideojuegoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> insertar(@RequestBody Videojuego v) {
        try {
            return ResponseEntity.ok(service.insertar(v));

        } catch (VideojuegoDuplicadoException ex) {
            ErrorResponse er = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
            return new ResponseEntity<>(er, HttpStatus.CONFLICT);

        } catch (RuntimeException ex) {
            ErrorResponse er = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(er, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscar(@PathVariable String codigo) {
        try {
            return ResponseEntity.ok(service.buscar(codigo));

        } catch (VideojuegoNoEncontradoException ex) {
            ErrorResponse er = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(er, HttpStatus.NOT_FOUND);

        } catch (RuntimeException ex) {
            ErrorResponse er = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(er, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            return ResponseEntity.ok(service.listar());

        } catch (VideojuegoNoEncontradoException ex) {
            ErrorResponse er = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(er, HttpStatus.NOT_FOUND);

        } catch (RuntimeException ex) {
            ErrorResponse er = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(er, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable String codigo, @RequestBody Videojuego videojuego) {
        try {
            service.actualizar(codigo, videojuego);
            return ResponseEntity.ok("Videojuego actualizado correctamente");

        } catch (RuntimeException ex) {
            ErrorResponse er = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(er, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> eliminar(@PathVariable String codigo) {
        try {
            return ResponseEntity.ok(service.eliminar(codigo));

        } catch (RuntimeException ex) {
            ErrorResponse er = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(er, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
}