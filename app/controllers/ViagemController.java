package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import models.Local;
import models.Usuario;
import models.viagem.TipoDeViagem;
import models.viagem.Viagem;
import models.viagem.ViagemAberta;
import models.viagem.ViagemLimitada;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class ViagemController extends Controller {

	private static Form<Viagem> viagemForm = form(Viagem.class)
			.bindFromRequest();

	@Transactional
	public static Result showViagem(String id) {

		long idLong = Long.parseLong(id);

		return ok(views.html.viagemInfo.render(Application.getUsuarioLogado(),
				getViagem(idLong), ""));
	}

	@Transactional
	public static Result showEditar(String id) {
		long idLong = Long.parseLong(id);

		return ok(views.html.edicaoViagem.render(
				Application.getUsuarioLogado(), getViagem(idLong), ""));

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

			viagem = new Viagem(Application.getUsuarioLogado(), local,
					dataDaViagem, descricao, tipoDeViagem);

		} catch (Exception e) {
			return okErroCriarViagem(e.getMessage());
		}
		Application.salvaObjeto(local);
		Application.salvaObjeto(tipoDeViagem);
		Application.salvaObjeto(viagem);

		return Application.index();
	}

	@Transactional
	public static Result editarViagem() {
		Form<Viagem> form = viagemForm.bindFromRequest();

		String idViagem = form.field("id-viagem").value();

		long id = Long.parseLong(idViagem);

		Viagem viagem = getViagem(id);

		String pais = form.field("pais").value();
		String estado = form.field("estado").value();
		String cidade = form.field("cidade").value();
		String endereco = form.field("endereco").value();
		String referencia = form.field("referencia").value();
		String data = form.field("data").value();
		String descricao = form.field("descricao").value();
		String tipo = form.field("tipoDeViagem").value();
		String codigo = form.field("codigo").value();

		Calendar dataDaViagem = null;
		try {
			if (!isVazio(pais)) {
				viagem.getLocal().setPais(pais);
			}
			if (!isVazio(estado)) {
				viagem.getLocal().setEstado(estado);
			}
			if (!isVazio(cidade)) {
				viagem.getLocal().setCidade(cidade);
			}
			if (!isVazio(endereco)) {
				viagem.getLocal().setEndereco(endereco);
			}
			if (!isVazio(referencia)) {
				viagem.getLocal().setReferencia(referencia);
			}
			if (!isVazio(data)) {
				int[] d = formatarData(data);
				dataDaViagem = new GregorianCalendar(d[0], d[1], d[2]);
				viagem.setData(dataDaViagem);
			}
			if (!isVazio(descricao)) {
				viagem.setDescricao(descricao);
			}
			if (!isVazio(tipo)) {
				TipoDeViagem tipoDeViagem = null;
				if (tipo.equals("aberta")) {
					tipoDeViagem = new ViagemAberta();
				} else if (tipo.equals("limitada")) {
					tipoDeViagem = new ViagemLimitada(codigo);
				}
				Application.salvaObjeto(tipoDeViagem);
				viagem.setTipo(tipoDeViagem);
			}

		} catch (Exception e) {
			return okErroEditarViagem(viagem, e.getMessage());
		}
		Application.salvaObjeto(viagem);

		return showViagem(idViagem);
	}

	@Transactional
	public static Result participarDaViagem() {

		Form<Viagem> form = viagemForm.bindFromRequest();
		String codigoDeAcesso = form.field("codigo").value();
		String idViagem = form.field("id-viagem").value();

		long id = Long.parseLong(idViagem);
		Viagem v = getViagem(id);
		Usuario participante = Application.getUsuarioLogado();

		try {
			v.cadastraParticipante(participante, codigoDeAcesso);
		} catch (Exception e) {
			return okErroCadastrarParticipante(v, e.getMessage());
		}

		Application.salvaObjeto(v);

		return showViagem(idViagem);
	}

	@Transactional
	public static Result okErroCadastrarParticipante(Viagem viagem,
			String mensagem) {
		return ok(views.html.viagemInfo.render(Application.getUsuarioLogado(),
				viagem, mensagem));
	}

	@Transactional
	public static Result okErroCriarViagem(String mensagem) {
		return ok(views.html.index.render(Application.getUsuarioLogado(),
				getViagens(), mensagem));
	}

	@Transactional
	public static Result okErroEditarViagem(Viagem viagem, String mensagem) {
		return ok(views.html.edicaoViagem.render(
				Application.getUsuarioLogado(), viagem, mensagem));
	}

	private static boolean isVazio(String str) {
		return str == null || str.trim().equals("");
	}

	private static Viagem getViagem(Long id) {
		return Application.getDAO().findByEntityId(Viagem.class, id);
	}

	private static int[] formatarData(String data) {
		int ano = Integer.parseInt(data.substring(0, 4));
		int mes = Integer.parseInt(data.substring(5, 7));
		int dia = Integer.parseInt(data.substring(8, 10));

		return new int[] { ano, mes, dia };
	}

	protected static List<Viagem> getViagens() {
		List<Viagem> viagens = Application.getDAO()
				.findAllByClassName("Viagem");
		if (viagens == null) {
			viagens = new ArrayList<Viagem>();
		}
		return viagens;
	}

}
