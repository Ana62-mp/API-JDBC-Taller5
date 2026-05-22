package com.krakedev.apijdbc.excepciones;

public class ErrorResponse {
	private String m;
	private int codigo;
	
	public ErrorResponse(String m, int cod) {
		this.m=m;
		this.codigo=cod;
	}

	public String getM() {
		return m;
	}

	public int getCodigo() {
		return codigo;
	}

	
	
	
}
