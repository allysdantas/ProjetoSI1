package models.viagem;

import javax.persistence.Entity;

import models.Usuario;

@Entity
public class ViagemAberta extends TipoDeViagem {

	@Override
	public void cadastrar(Usuario usuario, String codigoDeAcesso, Viagem viagem)
			throws Exception {
		viagem.getListaParticipantes().add(usuario);
	}

	@Override
	public String toString() {
		return "Aberta";
	}

}
