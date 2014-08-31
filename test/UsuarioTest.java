import static org.junit.Assert.*;
import models.Usuario;

import org.junit.Before;
import org.junit.Test;


public class UsuarioTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void deveLancarExcecaoNoNome() {
		try {
			new Usuario(null, "nome@mail.com", "123456");
			fail("Deveria dar erro no nome");
		} catch (Exception e) {
			assertEquals("Nome não pode ser nulo", e.getMessage());
		}
		
		try {
			new Usuario("", "nome@mail.com", "123456");
			fail("Deveria dar erro no nome");
		} catch (Exception e) {
			assertEquals("Nome não pode ser vazio", e.getMessage());
		}
		
		try {
			new Usuario("Joana 100% com você ok?", "joana@mail.com", "123456");
			fail("Deveria dar erro no nome");
		} catch (Exception e) {
			assertEquals("Nome só pode conter caracteres", e.getMessage());
		}
		
		try {
			new Usuario("Joana5", "joana@mail.com", "123456");
			fail("Deveria dar erro no nome");
		} catch (Exception e) {
			assertEquals("Nome só pode conter caracteres", e.getMessage());
		}
	}
	
	@Test
	public void naoPodeAceitarEmailInvalido() {
		try {
			new Usuario("Maria", null, "123456");
			fail("Deveria dar erro no email");
		} catch (Exception e) {
			assertEquals("Email não pode ser nulo", e.getMessage());
		}
		
		try {
			new Usuario("Maria", "", "123456");
			fail("Deveria dar erro no email");
		} catch (Exception e) {
			assertEquals("Email não pode ser vazio", e.getMessage());
		}
		
		try {
			new Usuario("Maria", "marialive.com", "123456");
			fail("Deveria dar erro no email");
		} catch (Exception e) {
			assertEquals("Email inválido", e.getMessage());
		}
		
		try {
			new Usuario("Maria", "@live.com", "123456");
			fail("Deveria dar erro no email");
		} catch (Exception e) {
			assertEquals("Email inválido", e.getMessage());
		}
		
		try {
			new Usuario("Maria", "maria@", "123456");
			fail("Deveria dar erro no email");
		} catch (Exception e) {
			assertEquals("Email inválido", e.getMessage());
		}
		
		try {
			new Usuario("Maria", "maria@live", "123456");
			fail("Deveria dar erro no email");
		} catch (Exception e) {
			assertEquals("Email inválido", e.getMessage());
		}
		
		try {
			new Usuario("Maria", "maria@.com", "123456");
			fail("Deveria dar erro no email");
		} catch (Exception e) {
			assertEquals("Email inválido", e.getMessage());
		}
	}
	
	@Test
	public void naoDeveAceitarSenhasInvalidas() {
		try {
			new Usuario("Samara", "samara@hotmail.com", null);
			fail("Deveria dar erro no senha");
		} catch (Exception e) {
			assertEquals("Senha não pode ser nula", e.getMessage());
		}
		
		try {
			new Usuario("Samara", "samara@hotmail.com", "");
			fail("Deveria dar erro no senha");
		} catch (Exception e) {
			assertEquals("Senha não pode ser vazia", e.getMessage());
		}
		
		try {
			new Usuario("Samara", "samara@hotmail.com", "12345");
			fail("Deveria dar erro no senha");
		} catch (Exception e) {
			assertEquals("Senha não pode ter tamanho menor que 6", e.getMessage());
		}
		
		String senha = "";
		for (int i = 0; i < 31; i++) {
			senha += "a";
		}
		
		try {
			new Usuario("Samara", "samara@hotmail.com", senha);
			fail("Deveria dar erro no senha");
		} catch (Exception e) {
			assertEquals("Senha não pode ter tamanho maior que 30", e.getMessage());
		}
		
	}

}
