package controllers;

import static play.data.Form.form;

import java.util.List;

import models.Usuario;
import models.dao.GenericDAO;
import models.dao.GenericDAOImpl;
import play.mvc.Controller;
import play.mvc.Result;
import play.data.Form;
import play.db.jpa.Transactional;

public class Autenticacao extends Controller {

	private static GenericDAO dao = new GenericDAOImpl();
	private static Form<Usuario> usuarioForm = form(Usuario.class)
			.bindFromRequest();

	@Transactional
	public static Result show() {
		if (session().get("user") != null) {
			return redirect(routes.Application.index());
		}
		return okSucesso("");
	}

	@Transactional
	public static Result logout() {
		session().clear();
		return show();
	}

	@Transactional
	public static Result authenticate() {

		Form<Usuario> form = usuarioForm.bindFromRequest();

		String email = form.field("emailLogin").value();
		String senha = form.field("senhaLogin").value();

		if (isDadosInvalidosLogin(email, senha)) {
			return okErro("Email ou senha inválidos");
		} else {
			Usuario user = (Usuario) dao.findByAttributeName("Usuario",
					"email", email).get(0);
			session().clear();
			session("user", user.getEmail());
			return redirect(routes.Application.index());
		}
	}

	private static boolean isDadosInvalidosLogin(String email, String senha) {
		return usuarioForm.hasErrors() || !validarDadosLogin(email, senha);
	}

	private static boolean validarDadosLogin(String email, String senha) {
		List<Usuario> u = dao.findByAttributeName("Usuario", "email", email);
		if (u == null || u.isEmpty()) {
			return false;
		}
		if (!u.get(0).getSenha().equals(senha)) {
			return false;
		}
		return true;
	}

	@Transactional
	public static Result registrar() {

		Form<Usuario> form = usuarioForm.bindFromRequest();

		String nome = form.field("nomeRegistro").value();
		String email = form.field("emailRegistro").value();
		String senha = form.field("senhaRegistro").value();
		String confirmaSenha = form.field("confirmaSenhaRegistro").value();

		if (!senha.equals(confirmaSenha)) {
			return okErro("Senhas não são correspondentes");
		}

		Usuario user = null;

		try {
			user = new Usuario(nome, email, senha);
		} catch (Exception e) {
			return okErro(e.getMessage());
		}

		if (validacaoEmail(email)) {
			return okErro("Email já está em uso");
		}

		Application.salvaObjeto(user);
		return okSucesso("Cadastrado com sucesso");

	}

	@Transactional
	public static Result okErro(String mensagem) {
		return ok(views.html.autenticacao.render(usuarioForm, mensagem, ""));
	}

	@Transactional
	public static Result okSucesso(String mensagem) {
		return ok(views.html.autenticacao.render(usuarioForm, "", mensagem));
	}

	private static boolean validacaoEmail(String email) {
		List<Usuario> u = dao.findByAttributeName("Usuario", "email", email);

		for (Usuario usuario : u) {
			if (usuario.getEmail().equals(email)) {
				return true;
			}
		}
		return false;
	}
}
