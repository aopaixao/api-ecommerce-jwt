package com.residencia.comercio.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.residencia.comercio.dtos.TokenDTO;
import com.residencia.comercio.dtos.UsuarioRecuperacaoSenhaDTO;
import com.residencia.comercio.entities.Usuario;
import com.residencia.comercio.services.TokenService;
import com.residencia.comercio.services.UsuarioService;

@RestController
@RequestMapping("/autenticacao")
public class AutenticacaoController {
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	TokenService tokenService;
	
	@Autowired
	UsuarioService usuarioService;
	
	@PostMapping
	public ResponseEntity<TokenDTO> auth(@RequestBody Usuario usuario){
		
		UsernamePasswordAuthenticationToken 
		usernamePasswordAuthenticationToken = 
		new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getSenha());
		
		Authentication 
		authentication = 
		authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		
		String token = tokenService.generateToken(authentication);
		TokenDTO tokenDTO = new TokenDTO("Bearer", token);
		return new ResponseEntity<>(tokenDTO, HttpStatus.OK);
	}
	
	@PostMapping("/registro")
	public ResponseEntity<TokenDTO> registrar(@RequestBody Usuario usuario){
		
		String encodedPass = passwordEncoder.encode(usuario.getSenha());
		usuario.setSenha(encodedPass);
		
		Usuario novoUsuario = usuarioService.save(usuario);
		
		String token = tokenService.generateTokenWithUserData(novoUsuario);
		TokenDTO tokenDTO = new TokenDTO("Bearer", token);
		return new ResponseEntity<>(tokenDTO, HttpStatus.OK);
	}
	
	
	@PostMapping("/recuperar-senha")
	public ResponseEntity<TokenDTO> recuperarSenha(@RequestBody UsuarioRecuperacaoSenhaDTO usuarioSenhaDTO){
		String encodedPass = passwordEncoder.encode(usuarioSenhaDTO.getSenha());
		usuarioSenhaDTO.setSenha(encodedPass);

        Usuario usuarioAtualizado = usuarioService.alteraSenha(usuarioSenhaDTO);
		
		String token = tokenService.generateTokenWithUserData(usuarioAtualizado);
		TokenDTO tokenDTO = new TokenDTO("Bearer", token);
		return new ResponseEntity<>(tokenDTO, HttpStatus.OK);
	}
	
}
