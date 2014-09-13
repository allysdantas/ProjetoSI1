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

public class AutenticacaoController extends Controller {

	private static GenericDAO dao = new GenericDAOImpl();
	private static Form<Usuario> usuarioForm = form(Usuario.class)
			.bindFromRequest();

	@Transactional
	public static Result showLogin() {
		if (session().get("user") != null) {
			return redirect(routes.Application.index());
		}
		return okSucessoLogin("");
	}

	@Transactional
	public static Result showCadastro() {
		return okSucessoCadastro("");
	}

	@Transactional
	public static Result logout() {
		session().clear();
		return showLogin();
	}

	@Transactional
	public static Result autenticar() {

		Form<Usuario> form = usuarioForm.bindFromRequest();

		String email = form.field("email").value();
		String senha = form.field("senha").value();

		if (isDadosInvalidosLogin(email, senha)) {
			return okErroLogin("Email ou senha inválidos");
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
	public static Result cadastrar() {

		Form<Usuario> form = usuarioForm.bindFromRequest();

		String nome = form.field("nome").value();
		String email = form.field("email").value();
		String senha = form.field("senha").value();
		String confirmaSenha = form.field("confirmaSenha").value();

		if (!senha.equals(confirmaSenha)) {
			return okErroCadastro("Senhas não correspondem");
		}

		Usuario user = null;

		try {
			user = new Usuario(nome, email, senha);
		} catch (Exception e) {
			return okErroCadastro(e.getMessage());
		}

		if (validacaoEmail(email)) {
			return okErroCadastro("Email já está em uso");
		}

		Application.salvaObjeto(user);
		return okSucessoLogin("Cadastrado com sucesso");

	}

	@Transactional
	public static Result okErroLogin(String mensagem) {
		return ok(views.html.login.render(usuarioForm, mensagem, ""));
	}

	@Transactional
	public static Result okErroCadastro(String mensagem) {
		return ok(views.html.cadastro.render(usuarioForm, mensagem, ""));
	}

	@Transactional
	public static Result okSucessoLogin(String mensagem) {
		return ok(views.html.login.render(usuarioForm, "", mensagem));
	}

	@Transactional
	public static Result okSucessoCadastro(String mensagem) {
		return ok(views.html.cadastro.render(usuarioForm, "", mensagem));
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
