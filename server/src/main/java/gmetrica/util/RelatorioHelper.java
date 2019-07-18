package gmetrica.util;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Map;

import gmetrica.enumeration.TipoExportacao;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

public class RelatorioHelper {
	
	
	
	/**
	 * Gera o PDF
	 * @param vNomeRelatorio
	 * @param pMapParametros
	 * @param datasource
	 * @param nomeArquivo
	 */
	public String relatorioGenerico(String vNomeRelatorio, Map<String, Object> pMapParametros,
			JRBeanCollectionDataSource datasource, TipoExportacao tipoExportacao){
		
		if(tipoExportacao.equals(TipoExportacao.EXCEL)){
			pMapParametros.put("IS_IGNORE_PAGINATION", true);
		}
		
		try {			
			JasperPrint vJp = JasperFillManager.fillReport(vNomeRelatorio + ".jasper", 
					   pMapParametros, 
					   datasource);
			
			if(tipoExportacao.equals(TipoExportacao.PDF)){
				byte[] vResult = JasperExportManager.exportReportToPdf(vJp);
				String pdf = Base64.getEncoder().encodeToString(vResult);
				return pdf;
			}
			else {
				ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
	            
				JRXlsExporter exporter = new JRXlsExporter();
				exporter.setExporterInput(new SimpleExporterInput(vJp));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
				SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
				configuration.setOnePagePerSheet(false);
				configuration.setDetectCellType(true);
				configuration.setCollapseRowSpan(false);
				exporter.setConfiguration(configuration);
				exporter.exportReport();
	            
	            byte[] vResult = xlsReport.toByteArray();
	            String excel = Base64.getEncoder().encodeToString(vResult);
				return excel;
			}
		
		} catch (JRException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
