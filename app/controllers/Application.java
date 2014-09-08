package controllers;

import models.Usuario;
import models.dao.GenericDAO;
import models.dao.GenericDAOImpl;
import play.db.jpa.Transactional;
import play.mvc.*;

public class Application extends Controller {

	private static GenericDAO dao = new GenericDAOImpl();

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
		return ok(views.html.index.render(user, ""));
	}
	
	@Transactional
	public static Result showContato() {
		return ok(views.html.contato.render());
		
	}
	
	@Transactional
	public static Result showSobre() {
		return ok(views.html.sobre.render());
		
	}
	
	@Transactional
	protected static void salvaObjeto(Object obj) {
		dao.persist(obj);
		dao.merge(obj);
		dao.flush();
	}
}
