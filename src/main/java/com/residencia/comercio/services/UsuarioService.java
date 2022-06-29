package com.residencia.comercio.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.residencia.comercio.dtos.UsuarioRecuperacaoSenhaDTO;
import com.residencia.comercio.entities.Usuario;
import com.residencia.comercio.repositories.UsuarioRepository;

@Service
public class UsuarioService {
	@Autowired
	UsuarioRepository usuarioRepository;
	
	public Usuario save(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
	
	public Usuario alteraSenha(UsuarioRecuperacaoSenhaDTO usuarioSenhaDTO) {
		Usuario usuario = usuarioRepository.findById(usuarioSenhaDTO.getIdUsuario()).get();
		usuario.setSenha(usuarioSenhaDTO.getSenha());
		return usuarioRepository.save(usuario);
	}
	
}
