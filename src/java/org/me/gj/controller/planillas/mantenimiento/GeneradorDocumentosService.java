package org.me.gj.controller.planillas.mantenimiento;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.FileImageProvider;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class GeneradorDocumentosService {

	/**
	 * Toma la ruta de fichero y devuelve un stream abierto para su lectura.
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public InputStream loadDocumentAsStream(String filePath) throws IOException {
		URL url = new File(filePath).toURI().toURL();
		InputStream documentAsStream = url.openStream();
		return documentAsStream;
	}

	/**
	 * Carga en el objeto context el valor de las variables.
	 * 
	 * @param report
	 * @param variablesToBeReplaced
	 * @return
	 * @throws XDocReportException
	 */
	public IContext cargarVariables(Map<String, String> variables, IContext context) throws XDocReportException {
		for (Map.Entry<String, String> variable : variables.entrySet()) {
			context.put(variable.getKey(), variable.getValue());
		}

		return context;
	}

	/**
	 * Carga en el objeto context las im�genes a partir de las rutas dadas y guarda en
	 * el objeto report los metadatos de las im�genes.
	 * 
	 * @param report
	 * @param variables 
	 * @param context
	 */
	public void cargarImagenes(IXDocReport report, Map<String, String> variables, IContext context) {
		if (variables != null && !variables.isEmpty()) {
			FieldsMetadata metadata = report.getFieldsMetadata();
			if (metadata == null) {
				metadata = new FieldsMetadata();
			}
			
			for (Map.Entry<String, String> variable : variables.entrySet()) {
				metadata.addFieldAsImage(variable.getKey());
				context.put(variable.getKey(), new FileImageProvider(new File(variable.getValue()), true));
			}
	
			report.setFieldsMetadata(metadata);
		}
	}

	/**
	 * Toma la plantilla y devuelve un documento de salida como byte[]
	 * 
	 * @param rutaPlantilla
	 * @param templateEngine
	 *            Template Engine: Freemarker or Velocity
	 * @param variablesMap
	 * @param imagenesPathMap
	 * @param convertirPdf
	 *            Indica si se quiere convertir el documento a PDF
	 * @return
	 * @throws IOException
	 * @throws XDocReportException
	 */
	public byte[] generarDocumento(String rutaPlantilla, TemplateEngineKind templateEngine,
			Map<String, String> variablesMap, Map<String, String> imagenesPathMap, boolean convertirPdf)
					throws IOException, XDocReportException {
		return this.generarDocumento(rutaPlantilla, templateEngine, variablesMap, imagenesPathMap, convertirPdf, null);
	}

	/**
	 * Toma la plantilla y devuelve un documento de salida como byte[]
	 * 
	 * @param rutaPlantilla
	 * @param templateEngine
	 *            Template Engine: Freemarker or Velocity
	 * @param variablesMap
	 * @param imagenesPathMap
	 * @param convertirPdf
	 *            Indica si se quiere convertir el documento a PDF
	 * @return
	 * @throws IOException
	 * @throws XDocReportException
	 */
	public byte[] generarDocumento(String rutaPlantilla, TemplateEngineKind templateEngine,
			Map<String, String> variablesMap, Map<String, String> imagenesPathMap, boolean convertirPdf, FieldsMetadata metadatos)
					throws IOException, XDocReportException {                        
            
                /*Configuration freeMarkerConfig = new Configuration(Configuration.getVersion());
                freeMarkerConfig.setNewBuiltinClassResolver(TemplateClassResolver.ALLOWS_NOTHING_RESOLVER);
                freeMarkerConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
                freeMarkerConfig.setLogTemplateExceptions(false);
                freeMarkerConfig.setLocale(Locale.US);
                freeMarkerConfig.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                freeMarkerConfig.setOutputEncoding("UTF-8");                        */
            
		// Cargar el fichero y configurar el Template Engine
		InputStream inputStream = loadDocumentAsStream(rutaPlantilla);
		IXDocReport xdocReport = XDocReportRegistry.getRegistry().loadReport(inputStream, templateEngine);
		
		// Se cargan los metadatos
		xdocReport.setFieldsMetadata(metadatos);
		// Se crea el contexto y se cargan las variables de reemplazo
		IContext context = xdocReport.createContext();		
		cargarVariables(variablesMap, context);
		//cargarImagenes(xdocReport, imagenesPathMap, context);

		// Se genera la salida a partir de la plantilla.
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		if (convertirPdf) {
			// Se configura PDF como el formato de salida del conversor
			Options options = Options.getTo(ConverterTypeTo.PDF);

			// Se genera el documento remplazando las variables y convirtiendo
			// la salida a PDF.
			xdocReport.convert(context, options, out);
		} else {
			// Se genera el documento remplazando las variables manteniendo el
			// formato original.
                    try {
                        xdocReport.process(context, out);
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                        
			
		}

		return out.toByteArray();
	}
}
