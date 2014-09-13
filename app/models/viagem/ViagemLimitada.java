package models.viagem;

import javax.persistence.Entity;

import models.Usuario;

@Entity
public class ViagemLimitada extends TipoDeViagem {

	/**
	 * Construtor valido apenas para o banco de dados, utilize o outro
	 * construtor
	 */
	public ViagemLimitada() {
	}

	public ViagemLimitada(String codigo) throws Exception {
		super.setCodigo(codigo);
	}

	@Override
	public void cadastrar(Usuario usuario, String codigoDeAcesso, Viagem viagem)
			throws Exception {
		if (!codigoDeAcesso.equals(getCodigo())) {
			throw new Exception("Código inválido");
		}
		viagem.getListaParticipantes().add(usuario);
	}

	@Override
	public String toString() {
		return "Limitada";
	}

}
