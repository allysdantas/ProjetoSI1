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
	private Viagem viagem1, viagem2, viagem3;
	private TipoDeViagem viagemAberta, viagemLimitada;

	@Before
	public void setUp() throws Exception {
		allys = new Usuario("Allys", "allys@outlook.com.br", "123456");
		mariazinha = new Usuario("Mariazinha", "mariazinha@hotmail.com", "maria1234");
		joaquim = new Usuario("Joazim", "joazim@gmail.com", "joao1234");
		chiquinha = new Usuario("chiquinha", "chiquinha@gmail.com", "chica1234");
		roliude = new Local("Brasil", "Paraíba", "Cabaceiras",
				"Rua Raul Albuquerque Dinca", "Igreja onde João Grilo morreu");
		viagemAberta = new ViagemAberta();
		viagemLimitada = new ViagemLimitada("0811");
		viagem1 = new Viagem(mariazinha, roliude, Calendar.getInstance(), "Perto da feira", viagemAberta);
		viagem2 = new Viagem(allys, roliude, Calendar.getInstance(), "Perto de algum lugar", viagemLimitada);
		viagem3 = new Viagem(joaquim, roliude, Calendar.getInstance(), "perto da casa de carlos", viagemAberta);
	}

	@Test
	public void deveAdicionarParticipanteNaViagemAberta() {
		
		assertEquals(viagem1.getParticipantes().size(), 0);
		
		try {
			viagem1.cadastraParticipante(joaquim, "");;
		} catch (Exception e) {	}
		
		assertEquals(viagem1.getParticipantes().size(), 1);
		assertFalse(viagem2.isParticipante(joaquim));			
		assertTrue(viagem1.getCodigoDeAcesso().equals(""));
		assertTrue(viagem1.isParticipante(joaquim));
		assertFalse(viagem1.isParticipante(allys));	
	}

	@Test
	public void deveAdicionarParticipanteNaViagemLimitada() {
		
		assertEquals(viagem2.getParticipantes().size(), 0);
		//verificar quando o codigo é diferente
		try {
			viagem2.cadastraParticipante(mariazinha, "0811");
		} catch (Exception e) {	}
		
		assertTrue(viagem2.isParticipante(mariazinha));
		assertFalse(viagem2.isParticipante(allys));
		assertEquals(viagem2.getParticipantes().size(), 1);
		assertTrue(viagem2.getCodigoDeAcesso().equals("0811"));
		assertFalse(viagem2.getCodigoDeAcesso().equals("08111"));
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
		
		try{
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_WEEK, -1);
			
			new Viagem(allys, roliude, Calendar.getInstance(), DESCRICAO, new ViagemAberta());
		} catch(Exception e){
			assertEquals("Data não pode ser a anterior", e.getMessage());
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
