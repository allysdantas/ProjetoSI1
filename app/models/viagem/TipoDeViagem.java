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
	
	private String codigo;

	public TipoDeViagem() {
		this.codigo = "";
	}

	public abstract void cadastrar(Usuario usuario, String codigoDeAcesso,
			Viagem viagem) throws Exception;

	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) throws Exception {
		isCodigoValido(codigo);
		this.codigo = codigo;
	}
	
	public long getId() {
		return id;
	}
	
	private void isCodigoValido(String codigo) throws Exception {
		if (codigo == null) {
			throw new Exception("Codigo não pode ser nulo");
		}
		if (codigo.trim().equals("")) {
			throw new Exception("Codigo não pode ser vazio");
		}
		
		this.codigo = codigo;
	}
}
