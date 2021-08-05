package com.example.financas.service;



import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.financas.exception.ErroAutenticacao;
import com.example.financas.exception.RegraNegocioException;
import com.example.financas.model.entity.Usuario;
import com.example.financas.model.repository.UsuarioRepository;
import com.example.financas.service.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	UsuarioRepository repository;
	

	
	@Test
	public void deveSAlvarUmUsuario() {
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder()
									.id(1l)
									.nome("nome")
									.email("email@email.com")
									.senha("senha")
									.build();
		
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		org.assertj.core.api.Assertions.assertThat(usuarioSalvo).isNotNull();
	}
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso () {
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		Usuario result = service.autenticar(email, senha);
		
		org.assertj.core.api.Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void deveLancarErroQuandoNaoEcnontrarUsuarioCadastradoCOmOEmailInformado() {
		
		Assertions.assertThrows(ErroAutenticacao.class, () -> {
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
			
			service.autenticar("email@email.com", "senha");
		});
		
	}
	
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		
		Assertions.assertThrows(ErroAutenticacao.class, () -> {

			String senha = "senha";
			Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario)); 
			
			service.autenticar("email@email.com", "123");
		
		});
	}	
	
	@Test
	public void deveValidarEmail() {
		
		Assertions.assertDoesNotThrow(() -> {
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
			service.validarEmail("email@email.com");
		});

		
	}
	
	@Test()
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado () {
		
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
			
			service.validarEmail("email@email.com");
		});
	}
}
