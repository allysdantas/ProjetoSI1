package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Local {

	@Id
	@GeneratedValue
	private long id;

	private String pais;

	private String estado;
	private String cidade;
	private String endereco;
	private String referencia;

	public Local() {
	}

	public Local(String pais, String estado, String cidade, String endereco,
			String referencia) throws Exception {
		isPaisValido(pais);
		isEstadoValido(estado);
		isCidadeValida(cidade);
		isEnderecoValido(endereco);
		isReferenciaValida(referencia);
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) throws Exception {
		isPaisValido(pais);
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) throws Exception {
		isEstadoValido(estado);
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) throws Exception {
		isCidadeValida(cidade);
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) throws Exception {
		isEnderecoValido(endereco);
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) throws Exception {
		isReferenciaValida(referencia);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cidade == null) ? 0 : cidade.hashCode());
		result = prime * result
				+ ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((pais == null) ? 0 : pais.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Local) {
			Local l = (Local) obj;
			return this.getPais().equals(l.getPais())
					&& this.getEstado().equals(l.getEstado())
					&& this.getCidade().equals(l.getCidade())
					&& this.getEndereco().equals(l.getEndereco());
		}
		return false;
	}

	private void isEnderecoValido(String endereco) throws Exception {
		if (endereco == null) {
			throw new Exception("Endereço não pode ser nulo");
		}
		if (endereco.trim().equals("")) {
			throw new Exception("Endereço não pode ser vazio");
		}
		this.endereco = endereco;
	}

	private void isPaisValido(String pais) throws Exception {
		if (pais == null) {
			throw new Exception("País não pode ser nulo");
		}
		if (pais.trim().equals("")) {
			throw new Exception("País não pode ser vazio");
		}
		this.pais = pais;
	}

	private void isEstadoValido(String estado) throws Exception {
		if (estado == null) {
			throw new Exception("Estado não pode ser nulo");
		}
		if (estado.trim().equals("")) {
			throw new Exception("Estado não pode ser vazio");
		}
		this.estado = estado;
	}

	private void isCidadeValida(String cidade) throws Exception {
		if (cidade == null) {
			throw new Exception("Cidade não pode ser nula");
		}
		if (cidade.trim().equals("")) {
			throw new Exception("Cidade não pode ser vazia");
		}
		this.cidade = cidade;
	}

	private void isReferenciaValida(String referencia) throws Exception {
		if (referencia == null) {
			throw new Exception("Referencia não pode ser nula");
		}
		if (referencia.trim().equals("")) {
			throw new Exception("Referencia não pode ser vazia");
		}
		this.referencia = referencia;
	}

}
