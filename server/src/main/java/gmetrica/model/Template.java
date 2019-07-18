package gmetrica.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import gmetrica.enumeration.SituacaoTemplate;

@Entity
@Table(name = "template")
public class Template implements Serializable {
	
	
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 6170106143895516944L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column(name = "nome")
    private String nome;
	
	@Column(name = "situacao")
	@Enumerated(EnumType.STRING)
	private SituacaoTemplate situacao;
	
	@OneToOne(mappedBy = "template", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, optional = false)
	private TemplateContagem templateContagem = new TemplateContagem();
	
	@OneToMany(mappedBy = "template", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
	private Set<TemplateFuncionalidade> templateFuncionalidadeList = new LinkedHashSet<>();
	

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public SituacaoTemplate getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoTemplate situacao) {
		this.situacao = situacao;
	}

	public TemplateContagem getTemplateContagem() {
		return templateContagem;
	}

	public void setTemplateContagem(TemplateContagem templateContagem) {
		this.templateContagem = templateContagem;
	}
	
	public Set<TemplateFuncionalidade> getTemplateFuncionalidadeList() {
		return templateFuncionalidadeList;
	}

	public void setTemplateFuncionalidadeList(Set<TemplateFuncionalidade> templateFuncionalidadeList) {
		this.templateFuncionalidadeList = templateFuncionalidadeList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Template other = (Template) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
