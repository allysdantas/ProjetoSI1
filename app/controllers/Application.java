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

		Usuario user = new Usuario();
		if (dao.findByAttributeName("Usuario", "email", session().get("user"))
				.size() > 0) {
			if (dao.findByAttributeName("Usuario", "email",
					session().get("user")).get(0) != null) {
				user = (Usuario) dao.findByAttributeName("Usuario", "email",
						session().get("user")).get(0);
			}
		}
		return ok(views.html.index.render(user, getViagens(), ""));
	}

	@Transactional
	public static Result showContato() {
		return ok(views.html.contato.render());

	}

	@Transactional
	public static Result okErroCriarViagem(Usuario usuario, String mensagem) {
		return ok(views.html.index.render(usuario, getViagens(), mensagem));
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

		Calendar dataDaViagem = new GregorianCalendar(d[0], d[1], d[2]);

		Local local = null;
		Viagem viagem = null;
		TipoDeViagem tipoDeViagem = null;
		Usuario organizador = null;
		try {
			organizador = (Usuario) dao.findByAttributeName("Usuario", "email",
					session().get("user")).get(0);
			local = new Local(pais, estado, cidade, endereco, referencia);

			if (tipo.equals("aberta")) {
				tipoDeViagem = new ViagemAberta();
			} else if (tipo.equals("limitada")) {
				tipoDeViagem = new ViagemLimitada(codigo);
			}

			viagem = new Viagem(organizador, local, dataDaViagem, descricao,
					tipoDeViagem);
		} catch (Exception e) {
			return okErroCriarViagem(organizador, e.getMessage());
		}

		salvaObjeto(local);
		salvaObjeto(tipoDeViagem);
		salvaObjeto(viagem);

		return index();
	}

	@Transactional
	public static Result showSobre() {
		return ok(views.html.sobre.render());

	}

	private static int[] formatarData(String data) {
		int ano = Integer.parseInt(data.substring(0, 4));
		int mes = Integer.parseInt(data.substring(5, 7));
		int dia = Integer.parseInt(data.substring(8, 10));

		return new int[] { ano, mes, dia };
	}

	@Transactional
	protected static void salvaObjeto(Object obj) {
		dao.persist(obj);
		dao.merge(obj);
		dao.flush();
	}

	private static List<Viagem> getViagens() {
		List<Viagem> viagens = dao.findAllByClassName("Viagem");
		if (viagens == null) {
			viagens = new ArrayList<Viagem>();
		}
		return viagens;
	}
}
