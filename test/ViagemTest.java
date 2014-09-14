import static org.junit.Assert.*;

import java.util.Calendar;

import models.Local;
import models.Usuario;
import models.viagem.TipoDeViagem;
import models.viagem.Viagem;
import models.viagem.ViagemAberta;
import models.viagem.ViagemLimitada;

import org.junit.Before;
import org.junit.Test;

public class ViagemTest {

	private static final String DESCRICAO = "Vamos visitar a igreja onde foi gravadas varias cenas do filme o Auto da Compadecida";
	private Usuario allys, mariazinha, joaquim, chiquinha;
	private Local roliude;
	private Viagem viagem1, viagem2;
	private TipoDeViagem viagemAberta, viagemLimitada;
	private Calendar data;

	@Before
	public void setUp() throws Exception {
		data = Calendar.getInstance();
		data.add(Calendar.YEAR, Calendar.YEAR + 1);
		allys = new Usuario("Allys", "allys@outlook.com.br", "123456");
		mariazinha = new Usuario("Mariazinha", "mariazinha@hotmail.com",
				"maria1234");
		joaquim = new Usuario("Joaquim", "joaquim@gmail.com", "joaquim1234");
		chiquinha = new Usuario("chiquinha", "chiquinha@gmail.com", "chica1234");
		roliude = new Local("Brasil", "Paraíba", "Cabaceiras",
				"Rua Raul Albuquerque Dinca", "Igreja onde João Grilo morreu");
		viagemAberta = new ViagemAberta();
		viagemLimitada = new ViagemLimitada("0811");
		viagem1 = new Viagem(mariazinha, roliude, data, "Perto da feira",
				viagemAberta);
		viagem2 = new Viagem(allys, roliude, data, "Perto de algum lugar",
				viagemLimitada);
	}

	@Test
	public void deveAdicionarParticipanteNaViagemAberta() {

		assertEquals(viagem1.getParticipantes().size(), 0);

		try {
			viagem1.cadastraParticipante(mariazinha, "");
			;
		} catch (Exception e) {
		}

		assertEquals(viagem1.getParticipantes().size(), 1);
		assertFalse(viagem1.isParticipante(joaquim));
		assertTrue(viagem1.getCodigoDeAcesso().equals(""));
		assertTrue(viagem1.isParticipante(mariazinha));
		assertFalse(viagem1.isParticipante(allys));

		try {
			viagem1.cadastraParticipante(joaquim, "1");
			;
		} catch (Exception e) {
		}

		assertEquals(viagem1.getParticipantes().size(), 2);
	}

	@Test
	public void deveAdicionarParticipanteNaViagemLimitada() {

		assertEquals(viagem2.getParticipantes().size(), 0);

		try {
			viagem2.cadastraParticipante(allys, "0811");
		} catch (Exception e) {
		}

		assertEquals(viagem2.getParticipantes().size(), 1);

		try {
			viagem2.cadastraParticipante(mariazinha, "08124");
		} catch (Exception e) {
			assertEquals("Código inválido", e.getMessage());
		}

		assertEquals(viagem2.getParticipantes().size(), 1);

		try {
			viagem2.cadastraParticipante(chiquinha, "0811");
		} catch (Exception e) {
		}

		assertTrue(viagem2.isParticipante(allys));
		assertFalse(viagem2.isParticipante(mariazinha));
		assertTrue(viagem2.getCodigoDeAcesso().equals("0811"));
		assertFalse(viagem2.getCodigoDeAcesso().equals("08111"));
		assertEquals(viagem2.getParticipantes().size(), 2);
	}

	@Test
	public void naoDeveAceitarOrganizadorInvalido() {
		try {
			new Viagem(null, roliude, data, DESCRICAO, viagemAberta);
		} catch (Exception e) {
			assertEquals("Organizador não pode ser nulo", e.getMessage());
		}
	}

	@Test
	public void naoDeveAceitarDescricaoInvalida() {
		try {
			new Viagem(allys, roliude, data, null, viagemAberta);
		} catch (Exception e) {
			assertEquals("Descrição não pode ser nula", e.getMessage());
		}

		try {
			new Viagem(allys, roliude, data, "", viagemAberta);
		} catch (Exception e) {
			assertEquals("Descrição não pode ser vazia", e.getMessage());
		}

		String descricaoLonga = "";

		for (int i = 0; i < 200; i++) {
			descricaoLonga += i;
		}

		try {
			new Viagem(allys, roliude, data, descricaoLonga, viagemAberta);
		} catch (Exception e) {
			assertEquals("Descrição longa", e.getMessage());
		}
	}

	@Test
	public void naoDeveAceitarLocalInvalido() {
		try {
			new Viagem(allys, null, data, DESCRICAO, viagemAberta);
		} catch (Exception e) {
			assertEquals("Local não pode ser nulo", e.getMessage());
		}

	}

	@Test
	public void naoDeveAceitarDataInvalida() {
		try {
			new Viagem(allys, roliude, null, DESCRICAO, viagemAberta);
		} catch (Exception e) {
			assertEquals("Data não pode ser nula", e.getMessage());
		}

		try {
			new Viagem(allys, roliude, Calendar.getInstance(), DESCRICAO,
					viagemAberta);
		} catch (Exception e) {
			assertEquals("Ainda não existe viagem para o passado",
					e.getMessage());
		}
	}

	@Test
	public void naoDeveAceitarTipoInvalido() {
		try {
			new Viagem(allys, roliude, data, DESCRICAO, null);
		} catch (Exception e) {
			assertEquals("Tipo de viagem não pode ser nulo", e.getMessage());
		}
	}

	@Test
	public void naoDeveCriarViagemLimitadaComCodigoInválido() {
		try {
			new Viagem(joaquim, roliude, data, DESCRICAO,
					new ViagemLimitada(""));
		} catch (Exception e) {
			assertEquals("Codigo não pode ser vazio", e.getMessage());
		}

		try {
			new Viagem(joaquim, roliude, data, DESCRICAO, new ViagemLimitada(
					null));
		} catch (Exception e) {
			assertEquals("Codigo não pode ser nulo", e.getMessage());
		}
	}

	@Test
	public void naoDeveAceitarParticipanteRepetido() {
		
		assertEquals(viagem2.getParticipantes().size(), 0);
		
		try {
			viagem2.cadastraParticipante(allys, "0811");
		} catch (Exception e) {
		}

		assertEquals(viagem2.getParticipantes().size(), 1);

		try {
			viagem2.cadastraParticipante(allys, "0811");
		} catch (Exception e) {
			assertEquals("Usuário já está participando da Viagem",
					e.getMessage());
		}

		assertEquals(viagem2.getParticipantes().size(), 1);
	}
}