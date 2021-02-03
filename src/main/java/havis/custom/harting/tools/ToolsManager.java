package havis.custom.harting.tools;

import havis.custom.harting.tools.exception.ToolsException;
import havis.middleware.misc.TdtInitiator;
import havis.middleware.misc.TdtInitiator.SCHEME;
import havis.middleware.tdt.TdtTranslationException;
import havis.middleware.tdt.TdtTranslator;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

public class ToolsManager {
	private final static Logger log = Logger.getLogger(ToolsManager.class.getName());
	private final static String[] header = new String[] { "URI-TAG", "PC", "HEX", "URI-LEGACY-AI" };

	private CsvPreference preference = CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE;
	private TdtTranslator translator = new TdtTranslator();

	public ToolsManager() {
		for (SCHEME scheme : SCHEME.values()) {
			try {
				log.log(Level.INFO, "Loading TDT scheme {0}", scheme);
				translator.getTdtDefinitions().add(TdtInitiator.get(scheme));
			} catch (IOException e) {
				log.log(Level.SEVERE, "Failed to load TDT scheme " + scheme, e);
			}
		}
	}

	public synchronized void exportCsv(String epc, Writer writer) throws ToolsException {

		try {
			CsvListWriter listWriter = new CsvListWriter(writer, preference);
			CellProcessor[] processors = getProcessors();
			EpcRangeIterator range = null;

			try {
				listWriter.writeHeader(header);
				range = new EpcRangeIterator(epc, translator);

				while (range.hasNext()) {
					listWriter.write(Arrays.asList(range.getTag(), range.getPc(), range.getHex(), range.getAi()), processors);
				}

			} catch (TdtTranslationException exc) {				
				if(listWriter != null) {
					String message = exc.getMessage();

					if(range != null) {
						 message += " --> (" + range.getCurrentEpc() + ")";
					}
					
					listWriter.write(Arrays.asList(message, message, message, message), processors);
				}
			} finally {
				if(listWriter != null) {
					listWriter.close();
				}
			}
		} catch (Exception exc) {
			throw new ToolsException(exc.getMessage());
		}
	}

	private static CellProcessor[] getProcessors() {

		final CellProcessor[] processors = new CellProcessor[] { new NotNull(), // tag
				new NotNull(), // pc
				new NotNull(), // hex
				new NotNull() // ai
		};

		return processors;
	}
}
