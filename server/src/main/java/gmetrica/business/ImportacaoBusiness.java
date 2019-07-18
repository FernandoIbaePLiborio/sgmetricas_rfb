package gmetrica.business;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.demoiselle.jee.crud.AbstractBusiness;
import org.demoiselle.jee.rest.exception.DemoiselleRestException;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Table;

import gmetrica.enumeration.Classificacao;
import gmetrica.enumeration.Complexidade;
import gmetrica.enumeration.MetodoContagem;
import gmetrica.enumeration.ScopeCreep;
import gmetrica.enumeration.Situacao;
import gmetrica.enumeration.TipoContagem;
import gmetrica.enumeration.TipoDemanda;
import gmetrica.enumeration.TipoFuncionalidade;
import gmetrica.enumeration.TipoProjeto;
import gmetrica.model.Contagem;
import gmetrica.model.Funcionalidade;
import gmetrica.model.Importacao;
import gmetrica.model.Template;
import gmetrica.model.TemplateFuncionalidade;
import gmetrica.util.TreatNumber;

@Stateless
public class ImportacaoBusiness extends AbstractBusiness<Importacao, Long> {
	
	@Inject
	private TemplateBusiness templateBusiness;
	
	@Inject
	private ContagemBusiness contagemBusiness;
	
	private Contagem contagem;
	/**
	 * Realiza a importação da planilha de contagem
	 * @throws Exception 
	 */
	public void importacao(Importacao importacao) throws Exception{
			
			Template template = templateBusiness.find(importacao.getIdTemplate());
		
			String[] arquivo = importacao.getArquivoBase64().split(";");
			String[] conteudo = arquivo[1].split(",");
			
			byte[] bytes = Base64.decodeBase64(conteudo[1]);
    		InputStream is = new ByteArrayInputStream(bytes);
			SpreadsheetDocument planilha = SpreadsheetDocument.loadDocument(is);
			
			Contagem contagem = new Contagem();
			List<Funcionalidade> aliList = new ArrayList<>();
			List<Funcionalidade> aieList = new ArrayList<>();
			List<Funcionalidade> eeList = new ArrayList<>();
			List<Funcionalidade> ceList = new ArrayList<>();
			List<Funcionalidade> seList = new ArrayList<>();
			setContagem(new Contagem());
			for(Table table : planilha.getTableList()){
				// Realiza a importação dos dados da contagem
				table.setTableName(removerAcentos(table.getTableName()).toUpperCase());
				if(table.getTableName().equals("SUMARIO") || table.getTableName().equals("IDENTIFICACAO")){
					importaContagem(table, template);
					contagem = SerializationUtils.clone(getContagem());
					if (contagem.getTipoContagem() != null && contagem.getTipoContagem().equals(TipoContagem.ESTIMATIVA_INICIAL)){
						throw new DemoiselleRestException("Tipo de contagem não permitida para importação.");
					}
					if (contagem.getScopeCreep() != null && contagem.getScopeCreep().equals(ScopeCreep.SIM.getDescricao())){
						throw new DemoiselleRestException("Scope Creep deve ser igual a não.");
					}
					if (contagem.getMetodoContagem() != null && !contagem.getMetodoContagem().equals(MetodoContagem.CONTAGEM_DETALHADA)){
						throw new DemoiselleRestException("Método de contagem não permitido para importação.");
					}
				}
				// Realizla a importação das funcionalidades
				for(TemplateFuncionalidade tf : template.getTemplateFuncionalidadeList()){
					switch (tf.getTipoFuncionalidade()) {
						case ALI:
							aliList.addAll(importaFuncionalidade(table, tf, TipoFuncionalidade.ALI));
							break;
						case AIE:
							aieList.addAll(importaFuncionalidade(table, tf, TipoFuncionalidade.AIE));
							break;
						case EE:
							eeList.addAll(importaFuncionalidade(table, tf, TipoFuncionalidade.EE));
							break;
						case CE:
							ceList.addAll(importaFuncionalidade(table, tf, TipoFuncionalidade.CE));
							break;
						case SE:
							seList.addAll(importaFuncionalidade(table, tf, TipoFuncionalidade.SE));
							break;
						default:
							break;
					}
				}
			}

			contagem.setFornecedor(importacao.getFornecedor());
			contagem.setDataImportacao(importacao.getData());
			contagem.setNumeroDemanda(importacao.getNumeroDemanda());
			contagem.setSituacao(Situacao.PENDENTE);
			contagem.getFuncionalidadeList().addAll(aliList);
			contagem.getFuncionalidadeList().addAll(aieList);
			contagem.getFuncionalidadeList().addAll(eeList);
			contagem.getFuncionalidadeList().addAll(ceList);
			contagem.getFuncionalidadeList().addAll(seList);
			
			for(Funcionalidade f : contagem.getFuncionalidadeList()) {
				f.setContagem(contagem);
				f.setId(null);
			}
			
			contagemBusiness.persist(contagem);
	}
	
	/**
	 * Monta o objeto Contagem
	 * @param table
	 * @param template
	 * @param contagem
	 * @throws ParseException 
	 */
	private void importaContagem(Table table, Template template) throws ParseException{
		if(table.getTableName().equals(removerAcentos(template.getTemplateContagem().getArtefatosUsadosAba()).toUpperCase())){
			getContagem().setArtefatosUsadosContagem(table.getCellByPosition(template.getTemplateContagem().getArtefatorUsadosReferencia()).getDisplayText());
		}
		if(table.getTableName().equals(removerAcentos(template.getTemplateContagem().getFronteiraAba()).toUpperCase())){
			getContagem().setFronteira(table.getCellByPosition(template.getTemplateContagem().getFronteiraReferencia()).getDisplayText());
		}
		if(table.getTableName().equals(removerAcentos(template.getTemplateContagem().getLiderProjetoAba()).toUpperCase())){
			getContagem().setLiderProjeto(table.getCellByPosition(template.getTemplateContagem().getLiderProjetoReferencia()).getDisplayText());
		}
		if(table.getTableName().equals(removerAcentos(template.getTemplateContagem().getLinguagemAba()).toUpperCase())){
			getContagem().setLinguagem(table.getCellByPosition(template.getTemplateContagem().getLinguagemReferencia()).getDisplayText());
		}
		if(table.getTableName().equals(removerAcentos(template.getTemplateContagem().getObservacoesAba()).toUpperCase())){
			getContagem().setObservacoes(table.getCellByPosition(template.getTemplateContagem().getObservacoesReferencia()).getDisplayText());
		}
		if(table.getTableName().equals(removerAcentos(template.getTemplateContagem().getPfFuncaoDadosAba()).toUpperCase())){
			String cell = table.getCellByPosition(template.getTemplateContagem().getPfFuncaoDadosReferencia()).getDisplayText();
			BigDecimal valor = BigDecimal.ZERO;
			cell = cell.replaceAll("[^0-9]", "");
			if (!StringUtils.isBlank(cell)){
				valor = valor != null ? new BigDecimal(cell) : BigDecimal.ZERO;
			}
			getContagem().setPfFuncaoDados(valor);
		}
		if(table.getTableName().equals(removerAcentos(template.getTemplateContagem().getPfFuncaoTransacionalAba()).toUpperCase())){
			String cell = table.getCellByPosition(template.getTemplateContagem().getPfFuncaoTransacionalReferencia()).getDisplayText();
			BigDecimal valor = BigDecimal.ZERO;
			cell = cell.replaceAll("[^0-9]", "");
			if (!StringUtils.isBlank(cell)){
				valor = valor != null ? new BigDecimal(cell) : BigDecimal.ZERO;
			}
			getContagem().setPfFuncaoTransacional(valor);
		}
		if(table.getTableName().equals(removerAcentos(template.getTemplateContagem().getPfRetrabalhoAba()).toUpperCase())){
			String cell = table.getCellByPosition(template.getTemplateContagem().getPfRetrabalhoReferencia()).getDisplayText();
			BigDecimal valor = BigDecimal.ZERO;
			cell = cell.replaceAll("[^0-9]", "");
			if (!StringUtils.isBlank(cell)){
				valor = valor != null ? new BigDecimal(cell) : BigDecimal.ZERO;
			}
			getContagem().setPfRetrabalho(valor);
		}
		if(table.getTableName().equals(removerAcentos(template.getTemplateContagem().getPlataformaAba()).toUpperCase())){
			getContagem().setPlataforma(table.getCellByPosition(template.getTemplateContagem().getPlataformaReferencia()).getDisplayText());
		}
		if(table.getTableName().equals(removerAcentos(template.getTemplateContagem().getProjetoAba()).toUpperCase())){
			getContagem().setProjeto(table.getCellByPosition(template.getTemplateContagem().getProjetoReferencia()).getDisplayText());
		}
		if(table.getTableName().equals(removerAcentos(template.getTemplateContagem().getPropositoEscopoAba()).toUpperCase())){
			getContagem().setPropositoEscopo(table.getCellByPosition(template.getTemplateContagem().getPropositoEscopoReferencia()).getDisplayText());
		}
		if(table.getTableName().equals(removerAcentos(template.getTemplateContagem().getScopeCreepAba()).toUpperCase())){
			getContagem().setScopeCreep(table.getCellByPosition(template.getTemplateContagem().getScopeCreepReferencia()).getDisplayText());
		}
		if(table.getTableName().equals(removerAcentos(template.getTemplateContagem().getTipoContagemAba()).toUpperCase())){
			String cell = table.getCellByPosition(template.getTemplateContagem().getTipoContagemReferencia()).getDisplayText();
			if(StringUtils.isNotBlank(cell)){
				getContagem().setTipoContagem(TipoContagem.fromDescricao(cell));
			}
		}
		if(table.getTableName().equals(removerAcentos(template.getTemplateContagem().getTipoProjetoAba()).toUpperCase())){
			String cell = table.getCellByPosition(template.getTemplateContagem().getTipoProjetoReferencia()).getDisplayText();
			if(StringUtils.isNotBlank(cell)){
				getContagem().setTipoProjeto(TipoProjeto.fromDescricao(cell));
			}
		}
		if(table.getTableName().equals(removerAcentos(template.getTemplateContagem().getTotalPfBrutoAba()).toUpperCase())){
			String cell = table.getCellByPosition(template.getTemplateContagem().getTotalPfBrutoReferencia()).getDisplayText();
			BigDecimal valor = BigDecimal.ZERO;
			cell = cell.replaceAll("[^0-9]", "");
			if (!StringUtils.isBlank(cell)){
				valor = valor != null ? new BigDecimal(cell) : BigDecimal.ZERO;
			}
			getContagem().setTotalPfBruto(valor);
		}
		if(table.getTableName().equals(removerAcentos(template.getTemplateContagem().getTotalPfDemandaAba()).toUpperCase())){
			String cell = table.getCellByPosition(template.getTemplateContagem().getTotalPfDemandaReferencia()).getDisplayText();
			BigDecimal valor = BigDecimal.ZERO;
			cell = cell.replaceAll("[^0-9]", "");
			if (!StringUtils.isBlank(cell)){
				valor = valor != null ? new BigDecimal(cell) : BigDecimal.ZERO;
			}
			getContagem().setTotalPfDemanda(valor);
		}
		if(table.getTableName().equals(removerAcentos(template.getTemplateContagem().getMetodoContagemAba()).toUpperCase())){
			String cell = table.getCellByPosition(template.getTemplateContagem().getMetodoContagemReferencia()).getDisplayText();
			if(StringUtils.isNotBlank(cell)){
				getContagem().setMetodoContagem(MetodoContagem.fromDescricao(cell));
			}
		}
	}
	
	public BigDecimal formataDecimal(BigDecimal vlrFator){  
		BigDecimal numFormatado = vlrFator.setScale(2, BigDecimal.ROUND_UP); 
		return numFormatado;		
	}
	
	boolean isDigit(String s) {
	    return s.matches("[0-9]*");
	}
		
	public static String removerAcentos(String str) {
	    return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}
	
	/**
	 * Monta a lista de funcinalidades
	 * @param table
	 * @param templateFuncionalidade
	 * @param tipoFuncionalidade
	 * @return
	 */
	private List<Funcionalidade> importaFuncionalidade(Table table, TemplateFuncionalidade templateFuncionalidade, TipoFuncionalidade tipoFuncionalidade) {
		List<Funcionalidade> lista = new ArrayList<>();
		for(int i=0; i<40; i++) {
			Funcionalidade funcionalidade = new Funcionalidade();
			funcionalidade.setId(TreatNumber.identificador());
			funcionalidade.setTipo(tipoFuncionalidade);
			if(table.getTableName().equals(templateFuncionalidade.getClassificacaoAba())){
				String texto = getCellText(table, templateFuncionalidade.getClassificacaoReferencia(), i);
				if(StringUtils.isNotBlank(texto)){
					try {
						funcionalidade.setClassificacao(Classificacao.fromDescricao(texto));
					} catch (Exception e) {}
				}
			}
			if(table.getTableName().equals(templateFuncionalidade.getComplexidadeAba())){
				String texto = getCellText(table, templateFuncionalidade.getComplexidadeReferencia(), i);
				if(StringUtils.isNotBlank(texto)){
					try {
						funcionalidade.setComplexidade(Complexidade.fromDescricao(texto));
					} catch (Exception e) {}
				}
			}
			if(table.getTableName().equals(templateFuncionalidade.getDivergenciasAba())){
				funcionalidade.setDivergencias(getCellText(table, templateFuncionalidade.getDivergenciasReferencia(), i));
			}
			if(table.getTableName().equals(templateFuncionalidade.getMemoriaRlArAba())){
				funcionalidade.setMemoriaRlAr(getCellText(table, templateFuncionalidade.getMemoriaRlArReferencia(), i));
			}
			if(table.getTableName().equals(templateFuncionalidade.getMemoriaTdAba())){
				funcionalidade.setMemoriaTd(getCellText(table, templateFuncionalidade.getMemoriaTdReferencia(), i));
			}
			if(table.getTableName().equals(templateFuncionalidade.getNomeFuncionalidadeAba())){
				funcionalidade.setNome(getCellText(table, templateFuncionalidade.getNomeFuncionalidadeReferencia(), i));
				if (funcionalidade.getNome().length() > 500){
					throw new DemoiselleRestException("Excede o limite de caracteres para o campo 'Nome da Funcionalidade'.");
				}
			}
			if(table.getTableName().equals(templateFuncionalidade.getPontoFuncaoAba())){
				String texto = getCellText(table, templateFuncionalidade.getPontoFuncaoReferencia(), i);
				if(StringUtils.isNotBlank(texto)){
					try {
						funcionalidade.setPontoFuncao(new BigDecimal(TreatNumber.converte(texto)).setScale(2, RoundingMode.HALF_EVEN));
					} catch (ParseException e) {
						funcionalidade.setPontoFuncao(BigDecimal.ZERO);
					}
				}
			}
			if(table.getTableName().equals(templateFuncionalidade.getRastreabilidadeAba())){
				funcionalidade.setRastreabilidadeJustificativa(getCellText(table, templateFuncionalidade.getRastreabilidadeReferencia(), i));
			}
			if(table.getTableName().equals(templateFuncionalidade.getRlArAba())){
				String texto = getCellText(table, templateFuncionalidade.getRlArReferencia(), i);
				if(StringUtils.isNotBlank(texto)){
					try {
						funcionalidade.setRlAr(Integer.valueOf(texto));
					} catch (Exception e) {}
				}
			}
			if(table.getTableName().equals(templateFuncionalidade.getTdAba())){
				String texto = getCellText(table, templateFuncionalidade.getTdReferencia(), i);
				if(StringUtils.isNotBlank(texto)){
					try {
						funcionalidade.setTd(Integer.valueOf(texto));
					} catch (Exception e) {}
				}
			}
			if(table.getTableName().equals(templateFuncionalidade.getTipoAba())){
				String texto = getCellText(table, templateFuncionalidade.getTipoReferencia(), i);
				if(StringUtils.isNotBlank(texto)){
					try {
						funcionalidade.setTipo(TipoFuncionalidade.fromDescricao(texto));
					} catch (Exception e) {}
				}
			}
			if(table.getTableName().equals(templateFuncionalidade.getTipoDemandaAba())){
				String texto = getCellText(table, templateFuncionalidade.getTipoDemandaReferencia(), i);
				if(StringUtils.isNotBlank(texto)){
					try {
						funcionalidade.setTipoDemanda(TipoDemanda.fromDescricao(texto));
					} catch (Exception e) {}
				}
			}
			
			if(StringUtils.isNotBlank(funcionalidade.getNome())){
				lista.add(funcionalidade);
			}
		}
		return lista;
	}
	
	/**
	 * 
	 * @return
	 */
	private String getCellText(Table table, String position, int index){
		try {
			@SuppressWarnings("unused")
			Cell cell = table.getCellByPosition(position);
		} catch (Exception e) {
			return "";
		}
		int rowIndex = table.getCellByPosition(position).getRowIndex();
		int colIndex = table.getCellByPosition(position).getColumnIndex();
		int nextRow = rowIndex + index;
		
		Cell cell = table.getCellByPosition(colIndex, nextRow);
		return cell.getDisplayText();
	}

	public Contagem getContagem() {
		return contagem;
	}

	public void setContagem(Contagem contagem) {
		this.contagem = contagem;
	}
	
}
