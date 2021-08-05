package com.example.financas.service;

import com.example.financas.model.entity.Usuario;

public interface UsuarioService {
	
	Usuario autenticar (String email ,String senha);
	
	Usuario salvarUsuario (Usuario usuario);
	
	void validarEmail ( String email);
}
