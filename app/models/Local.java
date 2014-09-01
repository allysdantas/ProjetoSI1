package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Local {

	@Id
	@GeneratedValue
	private long id;

	private String endereco;
	private String referencia;

	public Local() {
	}

	public Local(String endereco, String referencia) throws Exception {
		isEnderecoValido(endereco);
		isReferenciaValida(referencia);
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
		result = prime * result
				+ ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result
				+ ((referencia == null) ? 0 : referencia.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Local) {
			Local l = (Local) obj;
			return this.getEndereco().equals(l.getEndereco())
					&& this.getReferencia().equals(l.getReferencia());
		}
		return false;
	}

	@Override
	public String toString() {
		return "Local \n[Endereco=" + endereco + ", \nreferencia=" + referencia
				+ "]";
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
