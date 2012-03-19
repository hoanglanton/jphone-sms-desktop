package it.flaminiandrea.jphonesms.export.txt;

import java.io.File;
import java.io.FileOutputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import it.flaminiandrea.jphonesms.domain.ShortMessage;
import it.flaminiandrea.jphonesms.domain.SmsBoard;
import it.flaminiandrea.jphonesms.export.Exporter;

public class TxtExporter implements Exporter {
	private SmsBoard smsBoard;
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
			output.flush();
			output.close();
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
		for (ShortMessage smsToExport : this.smsBoard.getEntriesByNameAndByReverseDate()) {
			result += 
				"-------------------------------------------------" + lineSeparator +
				smsToExport.toString() +
				"-------------------------------------------------" +lineSeparator + lineSeparator;
		}
		return result;
	}

	public SmsBoard getSmsBoard() {
		return this.smsBoard;
	}

	public void setSmsBoard(SmsBoard smsBoard) {
		this.smsBoard = smsBoard;
	}

	public String getPathToDirectory() {
		return pathToDirectory;
	}

	public void setPathToDirectory(String pathToDirectory) {
		this.pathToDirectory = pathToDirectory;
	}


}
