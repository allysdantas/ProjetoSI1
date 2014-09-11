package models.viagem;

import javax.persistence.Entity;

import models.Usuario;

@Entity
public class ViagemLimitada extends TipoDeViagem {

	private String codigo;

	/**
	 * Construtor valido apenas para o banco de dados, utilize o outro
	 * construtor
	 */
	public ViagemLimitada() {
	}

	public ViagemLimitada(String codigo) throws Exception {
		isCodigoValido(codigo);
	}

	@Override
	public void cadastrar(Usuario usuario, String codigoDeAcesso, Viagem viagem)
			throws Exception {
		isCodigoValido(codigoDeAcesso);
		if (codigoDeAcesso.equals(codigo)) {
			throw new Exception("Código inválido");
		}
		viagem.getListaParticipantes().add(usuario);

	}

	@Override
	public String toString() {
		return "Limitada";
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
