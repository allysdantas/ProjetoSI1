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
			new Local("Brasil", "Distrito Federal", "Brasilia", null,
					"Ao lado da casa da mãe Joana");
			fail("Deveria dar erro no endereço");
		} catch (Exception e) {
			assertEquals("Endereço não pode ser nulo", e.getMessage());
		}

		try {
			new Local("Brasil", "Distrito Federal", "Brasilia", "",
					"Ao lado da casa da mãe Joana");
			fail("Deveria dar erro no endereço");
		} catch (Exception e) {
			assertEquals("Endereço não pode ser vazio", e.getMessage());
		}
	}

	@Test
	public void naoDeveAceitarReferenciaInvalida() {
		try {
			new Local("Brasil", "Paraíba", "Nova Palmeira", "Rua Almiza Rosa",
					null);
			fail("Deveria dar erro no endereço");
		} catch (Exception e) {
			assertEquals("Referencia não pode ser nula", e.getMessage());
		}

		try {
			new Local("Brasil", "Paraíba", "Nova Palmeira", "Rua Almiza Rosa",
					"");
			fail("Deveria dar erro no endereço");
		} catch (Exception e) {
			assertEquals("Referencia não pode ser vazia", e.getMessage());
		}
	}

	@Test
	public void testaEquals() throws Exception {
		Local l1 = new Local("Inglaterra", "Grande Londres", "Londres",
				"Rua dos Alfineiros, nº4", "Casa dos tios de Harry");
		Local l2 = new Local("Inglaterra", "Grande Londres", "Londres",
				"Rua dos Alfineiros, nº4", "Casa dos tios de Harry");

		assertTrue(l1.equals(l2));

		Local l3 = new Local("Inglaterra", "Grande Londres", "Londres",
				"Rua dos Alfineiros, nº5", "Casa dos tios de Harry");

		assertFalse(l1.equals(l3));

		l2.setEndereco("Rua dos Alfineiros, nº5");

		assertFalse(l1.equals(l2));
	}

}
