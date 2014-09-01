import static org.junit.Assert.*;
import models.Local;

import org.junit.Before;
import org.junit.Test;

public class LocalTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void naoDeveAceitarEnderecoInvalido() {
		try {
			new Local(null, "Ao lado da casa da mãe Joana");
			fail("Deveria dar erro no endereço");
		} catch (Exception e) {
			assertEquals("Endereço não pode ser nulo", e.getMessage());
		}

		try {
			new Local("", "Ao lado da casa da mãe Joana");
			fail("Deveria dar erro no endereço");
		} catch (Exception e) {
			assertEquals("Endereço não pode ser vazio", e.getMessage());
		}
	}

	@Test
	public void naoDeveAceitarReferenciaInvalida() {
		try {
			new Local("Rua dos Jardins", null);
			fail("Deveria dar erro no endereço");
		} catch (Exception e) {
			assertEquals("Referencia não pode ser nula", e.getMessage());
		}

		try {
			new Local("Rua dos Jardins", "");
			fail("Deveria dar erro no endereço");
		} catch (Exception e) {
			assertEquals("Referencia não pode ser vazia", e.getMessage());
		}
	}

	@Test
	public void testaEquals() throws Exception {
		Local l1 = new Local("Rua dos Alfineiros, nº4", "Casa dos tios de Harry");
		Local l2 = new Local("Rua dos Alfineiros, nº4", "Casa dos tios de Harry");
		
		assertTrue(l1.equals(l2));
		
		Local l3 = new Local("Rua dos Alfineiros, nº4", "Casa dos tios de Harry Potter");
		
		assertFalse(l1.equals(l3));
		
		l2.setEndereco("Rua dos Alfineiros, nº5");
		
		assertFalse(l1.equals(l2));
	}

}
