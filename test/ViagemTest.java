import static org.junit.Assert.*;

import java.util.Calendar;

import models.Local;
import models.Usuario;
import models.viagem.Viagem;
import models.viagem.ViagemAberta;

import org.junit.Before;
import org.junit.Test;

public class ViagemTest {

	private static final String DESCRICAO = "Vamos visitar a igreja onde foi gravadas varias cenas do filma o Auto da Compadecida";
	private Usuario allys;
	private Local roliude;

	@Before
	public void setUp() throws Exception {
		allys = new Usuario("Allys", "allys@outlook.com.br", "123456");
		roliude = new Local("Brasil", "Paraíba", "Cabaceiras",
				"Rua Raul Albuquerque Dinca", "Igreja onde João Grilo morreu");
	}

	@Test
	public void deveAdicionarParticipanteNaViagemAberta() {
		fail("Não implementado");
	}

	@Test
	public void deveAdicionarParticipanteNaViagemLimitada() {
		fail("Não implementado");
	}

	@Test
	public void naoDeveAceitarOrganizadorInvalido() {
		try {
			new Viagem(null, roliude, Calendar.getInstance(), DESCRICAO,
					new ViagemAberta());
		} catch (Exception e) {
			assertEquals("Organizador não pode ser nulo", e.getMessage());
		}
	}

	@Test
	public void naoDeveAceitarDescricaoInvalida() {
		try {
			new Viagem(allys, roliude, Calendar.getInstance(), null,
					new ViagemAberta());
		} catch (Exception e) {
			assertEquals("Descrição não pode ser nula", e.getMessage());
		}

		try {
			new Viagem(allys, roliude, Calendar.getInstance(), "",
					new ViagemAberta());
		} catch (Exception e) {
			assertEquals("Descrição não pode ser vazia", e.getMessage());
		}

		String descricaoLonga = "";

		for (int i = 0; i < 200; i++) {
			descricaoLonga += i;
		}

		try {
			new Viagem(allys, roliude, Calendar.getInstance(), descricaoLonga,
					new ViagemAberta());
		} catch (Exception e) {
			assertEquals("Descrição longa", e.getMessage());
		}
	}

	@Test
	public void naoDeveAceitarLocalInvalido() {
		try {
			new Viagem(allys, null, Calendar.getInstance(), DESCRICAO,
					new ViagemAberta());
		} catch (Exception e) {
			assertEquals("Local não pode ser nulo", e.getMessage());
		}
	}

	@Test
	public void naoDeveAceitarDataInvalida() {
		try {
			new Viagem(allys, roliude, null, DESCRICAO, new ViagemAberta());
		} catch (Exception e) {
			assertEquals("Data não pode ser nula", e.getMessage());
		}
	}

	@Test
	public void naoDeveAceitarTipoInvalido() {
		try {
			new Viagem(allys, roliude, Calendar.getInstance(), DESCRICAO, null);
		} catch (Exception e) {
			assertEquals("Tipo de viagem não pode ser nulo", e.getMessage());
		}
	}

}
