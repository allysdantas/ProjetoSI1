package models.viagem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.Local;
import models.Usuario;

@Entity(name = "Viagem")
public class Viagem {

	@Id
	@GeneratedValue
	private long id;

	@OneToOne
	private Local local;
	private Calendar data;
	private String descricao;

	@OneToOne
	private TipoDeViagem tipo;

	@OneToOne
	private Usuario organizador;

	@ManyToMany
	private List<Usuario> participantes;

	public Viagem() {
		participantes = new ArrayList<Usuario>();
	}

	public Viagem(Usuario organizador, Local local, Calendar data,
			String descricao, TipoDeViagem tipo) throws Exception {
		this();
		isOrganizadorValido(organizador);
		isLocalValido(local);
		isDataValida(data);
		isDescricaoValida(descricao);
		isTipoDeViagemValido(tipo);
	}

	public List<Usuario> getParticipantes() {
		return Collections.unmodifiableList(participantes);
	}

	protected List<Usuario> getListaParticipantes() {
		return participantes;
	}

	public boolean isParticipante(Usuario usuario) {
		return participantes.contains(usuario);
	}

	public void cadastraParticipante(Usuario participante, String codigoDeAcesso)
			throws Exception {
		this.tipo.cadastrar(participante, codigoDeAcesso, this);
	}

	public Usuario getOrganizador() {
		return organizador;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) throws Exception {
		isLocalValido(local);
	}

	public Calendar getData() {
		return data;
	}

	public String dataDaViagem() {
		String data = "" + this.data.get(Calendar.DAY_OF_MONTH) + "/"
				+ this.data.get(Calendar.MONTH) + "/"
				+ this.data.get(Calendar.YEAR);

		return data;
	}

	public void setData(Calendar data) throws Exception {
		isDataValida(data);
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) throws Exception {
		isDescricaoValida(descricao);
	}

	public TipoDeViagem getTipo() {
		return tipo;
	}

	public void setTipo(TipoDeViagem tipo) throws Exception {
		isTipoDeViagemValido(tipo);
	}

	public String getIdString() {
		return "" + id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCodigoDeAcesso() {
		return this.tipo.getCodigo();
	}

	public void setCodigoDeAcesso(String codigo) throws Exception {
		this.tipo.setCodigo(codigo);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((local == null) ? 0 : local.hashCode());
		result = prime * result
				+ ((organizador == null) ? 0 : organizador.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Viagem) {
			Viagem v = (Viagem) obj;
			return this.getOrganizador().equals(v.getOrganizador())
					&& this.getDescricao().equals(v.getDescricao())
					&& this.getLocal().equals(v.getLocal());
		}
		return false;
	}

	@Override
	public String toString() {
		return "Viagem \n" + local.toString() + ", \ndescricao=" + descricao
				+ ", \norganizador=" + organizador.toString() + "]";
	}

	private void isOrganizadorValido(Usuario organizador) throws Exception {
		if (organizador == null) {
			throw new Exception("Organizador não pode ser nulo");
		}
		this.organizador = organizador;
	}

	private void isTipoDeViagemValido(TipoDeViagem tipo) throws Exception {
		if (tipo == null) {
			throw new Exception("Tipo de viagem não pode ser nulo");
		}
		this.tipo = tipo;
	}

	private void isDataValida(Calendar data) throws Exception {
		if (data == null) {
			throw new Exception("Data não pode ser nula");
		}
		this.data = data;
	}

	private void isDescricaoValida(String descricao) throws Exception {
		if (descricao == null) {
			throw new Exception("Descrição não pode ser nula");
		}
		if (descricao.trim().equals("")) {
			throw new Exception("Descrição não pode ser vazia");
		}
		if (descricao.length() > 450) {
			throw new Exception("Descrição longa");
		}
		this.descricao = descricao;
	}

	private void isLocalValido(Local local) throws Exception {
		if (local == null) {
			throw new Exception("Local não pode ser nulo");
		}
		this.local = local;
	}

	public long getId() {
		return id;
	}

}
