package it.flaminiandrea.jphonesms.export.txt;

import java.io.File;
import java.io.FileOutputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import it.flaminiandrea.jphonesms.domain.Data;
import it.flaminiandrea.jphonesms.domain.Entry;
import it.flaminiandrea.jphonesms.export.Exporter;

public class TxtExporter implements Exporter {
	private Data data;
	private String pathToDirectory;
	private String fileSeparator = System.getProperties().getProperty("file.separator");
	private String lineSeparator = System.getProperties().getProperty("line.separator");

	@Override
	public boolean export() {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd_HHmm");
		Date data = new Date();
		try {
			File newFile= new File(this.pathToDirectory + fileSeparator + "iphone_sms_"+formatter.format(data)+".txt");
			FileOutputStream output = new FileOutputStream(newFile);
			output.write(toTxt().getBytes());
			String mess = "SMS(s) exported to " + pathToDirectory + fileSeparator + "exported.txt";
			JOptionPane.showMessageDialog(null, mess, "Info", JOptionPane.INFORMATION_MESSAGE, null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE, null);
			return false;
		}
	}

	public String toTxt() {
		String result = "";
		for (Entry entryToExport : this.data.getEntriesByNameAndByReverseDate()) {
			result += 
				"-------------------------------------------------" + lineSeparator +
				entryToExport.toString() +
				"-------------------------------------------------" +lineSeparator + lineSeparator;
		}
		return result;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public String getPathToDirectory() {
		return pathToDirectory;
	}

	public void setPathToDirectory(String pathToDirectory) {
		this.pathToDirectory = pathToDirectory;
	}


}
