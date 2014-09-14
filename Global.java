import play.*;

import java.util.*;

import models.dao.GenericDAO;
import models.dao.GenericDAOImpl;
import models.viagem.Viagem;
import models.viagem.TipoDeViagem;
import models.viagem.ViagemAberta;
import models.viagem.ViagemLimitada;
import models.Usuario;
import models.Local;
import play.db.jpa.JPA;

public class Global extends GlobalSettings {

	private static GenericDAO dao = new GenericDAOImpl();
	private static String codigo = "8551";
	private Random rnd = new Random();

	private static final String[] NOMES = { "Ana", "Beatriz", "Camila",
			"Diocléia", "Eloah", "Fernanda", "Giovana", "Helena", "Ivete",
			"Juliana", "Kátia", "Luciana", "Maria", "Noemi", "Ohana",
			"Patrícia", "Quênia", "Roberta", "Sueli", "Tatiana", "Ursula",
			"Vânia", "Walkiria", "Ximena", "Yara", "Zélia", "Arthur",
			"Bernardo", "Carlos", "Dênis", "Emerson", "Fabrício", "Guilherme",
			"Heitor", "Inácio", "Jonas", "Kleber", "Leonardo", "Mário",
			"Nivaldo", "Otávio", "Paulo", "Quasímodo", "Rodrigo", "Saulo",
			"Túlio", "Umbertus", "Victor", "Wagner", "Xen", "Yannick",
			"Zoroastro" };

	private static final String[] SOBRENOMES = { "Alves", "Assis", "Born",
			"Coelho", "Cunha", "Dias", "Duarte", "Farias", "Fernandes",
			"Fonseca", "Gusmão", "Lima", "Lopes", "Maia", "Melo", "Mendes",
			"Miranda", "Oliveira", "Pereira", "Ribeiro", "Rodrigues" };

	private static final String[] PAISES = { "Alemanha", "Austrália",
			"Áustria", "Bélgica", "Canadá", "China", "Cingapura",
			"Coreia do Sul", "Dinamarca", "Estados Unidos", "Espanha",
			"Finlândia", "França", "Holanda", "Hungria", "Índia", "Irlanda",
			"Israel", "Itália", "Japão", "Noruega", "Nova Zelândia", "Polônia",
			"Rússia", "Suíça" };

	public void onStart(Application app) {
		Logger.info("Aplicação inicializada...");

		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				if (!(dao.findAllByClassName("Viagem").size() > 0)) {
					List<Usuario> usuarios = criaUsuariosFake(40);
					List<Local> locais = criaLocaisFake(30);
					List<Viagem> viagens = criarViagensFakes(30, usuarios,
							locais);
					criarParticipacoesFake(100, usuarios, viagens);
				}
			}

			public String getNomeAleatorio() {
				String nome = NOMES[rnd.nextInt(NOMES.length)] + " "
						+ SOBRENOMES[rnd.nextInt(SOBRENOMES.length)];
				return nome;
			}

			public String getNickAleatorio() {
				String nome = SOBRENOMES[rnd.nextInt(SOBRENOMES.length)]
						.toLowerCase();
				int tamanho = rnd.nextInt(5);
				for (int i = 0; i < tamanho; i++) {
					nome += rnd.nextInt(10);
				}
				return nome;
			}

			public List<Usuario> criaUsuariosFake(int quantidade) {
				Set<Usuario> usuarios = new HashSet<Usuario>();
				while (usuarios.size() < quantidade) {
					String email = getNickAleatorio() + "@gmail.com";
					String senha = "123456";
					Usuario usr;
					try {
						usr = new Usuario(getNomeAleatorio(), email, senha);
						salvaObjeto(usr);
						usuarios.add(usr);
					} catch (Exception e) {
					}
				}
				return new ArrayList<Usuario>(usuarios);
			}

			public List<Local> criaLocaisFake(int quantidade) {
				List<Local> locais = new ArrayList<Local>();
				try {
					for (int i = 0; i < quantidade; i++) {
						Local local = new Local(getPaisAleatorio(), "estado",
								"cidade", "endereco", "referencia");
						salvaObjeto(local);
						locais.add(local);

					}
				} catch (Exception e) {
				}
				return locais;
			}

			public List<Viagem> criarViagensFakes(int quantidade,
					List<Usuario> usuarios, List<Local> locais) {
				try {
					List<Viagem> viagens = new ArrayList<Viagem>();
					List<TipoDeViagem> tipo = new ArrayList<TipoDeViagem>();

					TipoDeViagem aberta = new ViagemAberta();
					TipoDeViagem limitada = new ViagemLimitada(codigo);

					salvaObjeto(aberta);
					salvaObjeto(limitada);

					tipo.add(aberta);
					tipo.add(limitada);

					Viagem viagem;

					for (int i = 0; i < quantidade; i++) {
						viagem = new Viagem(getUsuarioAleatorio(usuarios),
								getLocalAleatorio(locais), getDataAleatoria(),
								"descricao", getTipoAleatorio(tipo));
						salvaObjeto(viagem);
						viagens.add(viagem);
					}

					return viagens;
				} catch (Exception e) {
				}
				return null;
			}

			private Usuario getUsuarioAleatorio(List<Usuario> usuarios) {
				return usuarios.get(rnd.nextInt(usuarios.size()));
			}

			private Local getLocalAleatorio(List<Local> locais) {
				return locais.get(rnd.nextInt(locais.size()));
			}

			private TipoDeViagem getTipoAleatorio(List<TipoDeViagem> tipo) {
				return tipo.get(rnd.nextInt(tipo.size()));
			}

			private String getPaisAleatorio() {
				return PAISES[rnd.nextInt(PAISES.length)];
			}

			private Calendar getDataAleatoria() {
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_WEEK, rnd.nextInt(7));
				calendar.add(Calendar.MONTH, rnd.nextInt(12));
				calendar.add(Calendar.YEAR, Calendar.YEAR + 1);
				return calendar;
			}

			public void criarParticipacoesFake(int quantidade,
					List<Usuario> usuarios, List<Viagem> viagens) {
				for (int i = 0; i < quantidade; i++) {
					cadastrarParticipante(getUsuarioAleatorio(usuarios),
							viagens.get(rnd.nextInt(viagens.size())));
				}

			}

			private void cadastrarParticipante(Usuario participante,
					Viagem viagem) {
				if (viagem.getTipo() instanceof ViagemAberta) {
					try {
						if (!viagem.isParticipante(participante)) {
							viagem.cadastraParticipante(participante, "");
						}
					} catch (Exception e) {
					}
				} else if (viagem.getTipo() instanceof ViagemLimitada) {
					try {
						if (!viagem.isParticipante(participante)) {
							viagem.cadastraParticipante(participante, codigo);
						}
					} catch (Exception e) {
					}
				}
				salvaObjeto(viagem);
			}

			private void salvaObjeto(Object obj) {
				dao.persist(obj);
				dao.merge(obj);
				dao.flush();
			}
		});
	}
}
