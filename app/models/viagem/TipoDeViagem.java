package models.viagem;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import models.Usuario;

@Entity
public abstract class TipoDeViagem {

	@Id
	@GeneratedValue
	private long id;

	public TipoDeViagem() {
	}

	public abstract void cadastrar(Usuario usuario, String codigoDeAcesso,
			Viagem viagem) throws Exception;

}
