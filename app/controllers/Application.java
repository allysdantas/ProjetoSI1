package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import models.Local;
import models.Usuario;
import models.dao.GenericDAO;
import models.dao.GenericDAOImpl;
import models.viagem.TipoDeViagem;
import models.viagem.Viagem;
import models.viagem.ViagemAberta;
import models.viagem.ViagemLimitada;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.*;

public class Application extends Controller {

	private static GenericDAO dao = new GenericDAOImpl();
	private static Form<Viagem> viagemForm = form(Viagem.class)
			.bindFromRequest();

	@Transactional
	public static Result index() {
		if (session().get("user") == null) {
			return redirect(routes.Autenticacao.showLogin());
		}
		return ok(views.html.index.render(getUsuarioLogado(), getViagens(), ""));
	}

	@Transactional
	public static Result showContato() {
		return ok(views.html.contato.render());

	}

	@Transactional
	public static Result showViagem(String id) {

		long idLong = Long.parseLong(id);

		return ok(views.html.viagemInfo.render(getUsuarioLogado(),
				getViagem(idLong), ""));
	}
	
	@Transactional
	public static Result okErroCadastrarParticipante(Long idLong, String mensagem) {
		return ok(views.html.viagemInfo.render(getUsuarioLogado(),
				getViagem(idLong), ""));
	}

	@Transactional
	public static Result participarDaViagem(String id) {

		long idLong = Long.parseLong(id);

		Form<Viagem> form = viagemForm.bindFromRequest();
		String codigoDeAcesso = form.field("codigo").value();

		try {
			getViagem(idLong).cadastraParticipante(getUsuarioLogado(),
					codigoDeAcesso);
		} catch (Exception e) {
			return okErroCadastrarParticipante(idLong, e.getMessage());
		}
		
		salvaObjeto(getViagem(idLong));

		return ok(views.html.viagemInfo.render(getUsuarioLogado(),
				getViagem(idLong), ""));
	}

	@Transactional
	public static Result showSobre() {
		return ok(views.html.sobre.render());

	}

	@Transactional
	public static Result okErroCriarViagem(String mensagem) {
		return ok(views.html.index.render(getUsuarioLogado(), getViagens(),
				mensagem));
	}

	@Transactional
	public static Result criarViagem() {

		Form<Viagem> form = viagemForm.bindFromRequest();

		String pais = form.field("pais").value();
		String estado = form.field("estado").value();
		String cidade = form.field("cidade").value();
		String endereco = form.field("endereco").value();
		String referencia = form.field("referencia").value();
		String data = form.field("data").value();
		String descricao = form.field("descricao").value();
		String tipo = form.field("tipoDeViagem").value();
		String codigo = form.field("codigo").value();

		int[] d = formatarData(data);

		Local local = null;
		Viagem viagem = null;
		TipoDeViagem tipoDeViagem = null;

		try {
			Calendar dataDaViagem = new GregorianCalendar(d[0], d[1], d[2]);

			local = new Local(pais, estado, cidade, endereco, referencia);

			if (tipo.equals("aberta")) {
				tipoDeViagem = new ViagemAberta();
			} else if (tipo.equals("limitada")) {
				tipoDeViagem = new ViagemLimitada(codigo);
			}

			viagem = new Viagem(getUsuarioLogado(), local, dataDaViagem,
					descricao, tipoDeViagem);

		} catch (Exception e) {
			return okErroCriarViagem(e.getMessage());
		}

		salvaObjeto(local);
		salvaObjeto(tipoDeViagem);
		salvaObjeto(viagem);

		return index();
	}

	@Transactional
	protected static void salvaObjeto(Object obj) {
		dao.persist(obj);
		dao.merge(obj);
		dao.flush();
	}

	private static Usuario getUsuarioLogado() {
		Usuario user = new Usuario();
		if (listaUsuariosLogados().size() > 0) {
			if (listaUsuariosLogados().get(0) != null) {
				user = (Usuario) dao.findByAttributeName("Usuario", "email",
						session().get("user")).get(0);
			}
		}
		return user;
	}

	private static int[] formatarData(String data) {
		int ano = Integer.parseInt(data.substring(0, 4));
		int mes = Integer.parseInt(data.substring(5, 7));
		int dia = Integer.parseInt(data.substring(8, 10));

		return new int[] { ano, mes, dia };
	}

	private static List<Usuario> listaUsuariosLogados() {
		return dao.findByAttributeName("Usuario", "email", session()
				.get("user"));
	}

	private static List<Viagem> getViagens() {
		List<Viagem> viagens = dao.findAllByClassName("Viagem");
		if (viagens == null) {
			viagens = new ArrayList<Viagem>();
		}
		return viagens;
	}

	private static Viagem getViagem(Long id) {
		return dao.findByEntityId(Viagem.class, id);
	}
}
