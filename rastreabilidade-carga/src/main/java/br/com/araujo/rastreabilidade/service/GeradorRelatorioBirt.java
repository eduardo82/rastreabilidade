package br.com.araujo.rastreabilidade.service;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public final class GeradorRelatorioBirt {
	
	private static GeradorRelatorioBirt INSTANCE = new GeradorRelatorioBirt();
	private static final String EXTENSAO_RPT = ".rptdesign";
	
	private GeradorRelatorioBirt() {
	}
	
	public static GeradorRelatorioBirt getIntance() {
		return INSTANCE;
	}
	
	public static void gerarRelatorio(String nomeRelatorio, Map<String, Object> birtParams, ByteArrayOutputStream stream) {
		try {
	        EngineConfig config = new EngineConfig();
	        Resource resource = new ClassPathResource("relatorios/" + nomeRelatorio + EXTENSAO_RPT);

	        Platform.startup(config);
            IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
            IReportEngine engine = factory.createReportEngine(config);
	        
            IReportRunnable reportDesign = engine.openReportDesign(resource.getInputStream()); 
            
            IRunAndRenderTask task = engine.createRunAndRenderTask(reportDesign);
        	task.setParameterValues(birtParams);
            	
            PDFRenderOption options = new PDFRenderOption();
            options.setOutputFormat("pdf");
            options.setOutputStream(stream);

            task.setRenderOption(options);

            task.run();
            task.close();
            engine.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Platform.shutdown();
		}
	}
	
	public static void gerarImprimirRelatorio(String nomeRelatorio, Map<String, Object> birtParams, String nomeImpressora) throws PrintException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		gerarRelatorio(nomeRelatorio, birtParams, stream);
		imprimirReltorio(stream.toByteArray(), nomeImpressora);
	}
	
	private static void imprimirReltorio(byte[] arquivoStream, String nomeImpressora) throws PrintException {
		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		Doc relatorio = new SimpleDoc(arquivoStream, flavor, null);
		
		PrintService impressora = buscarImpressoraDisponivelPorNome(nomeImpressora);
		
		if (impressora != null) {			
			DocPrintJob job = impressora.createPrintJob();
			job.print(relatorio, null);
		}
	}
	
	private static PrintService buscarImpressoraDisponivelPorNome(String name) {
		
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, new HashPrintRequestAttributeSet());

		if (services.length > 0) {
			for (int i = 0; i < services.length; i++) {
				if (services[i].getName().equals(name)) {
					return services[i];
				}
			}
		}
		return null;
	}
}
