package it.flaminiandrea.jphonesms.export.html;

import java.io.File;
import java.io.FileOutputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.jfree.data.category.DefaultCategoryDataset;

import it.flaminiandrea.jphonesms.domain.ShortMessage;
import it.flaminiandrea.jphonesms.domain.SmsBoard;
import it.flaminiandrea.jphonesms.export.Exporter;
import it.flaminiandrea.jphonesms.logger.RuntimeLogger;

public class HtmlExporter implements Exporter {
	
	private Logger logger = RuntimeLogger.getInstance().getLogger(this.getClass());
	
	private SmsBoard smsBoard;
	private String pathToDirectory;
	private String fileSeparator = System.getProperties().getProperty("file.separator");

	@Override
	public boolean export() {
		try {
			boolean indexFile = makeIndexFile();
			boolean messagesFiles = makeMessagesFiles();
			boolean isStyleCreated = StyleExtractor.unzip("htmlstyle" + this.fileSeparator + "jphone-style.jps", this.pathToDirectory + this.fileSeparator);
			String mess = "SMS(s) exported to " + pathToDirectory;
			JOptionPane.showMessageDialog(null, mess, "Info", JOptionPane.INFORMATION_MESSAGE, null);
			return indexFile && messagesFiles && isStyleCreated;
		} catch (Exception e) {
			this.logger.error("Error Exporting in HTML.", e);
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE, null);
			return false;
		}
	}

	private boolean makeMessagesFiles() throws Exception {
		List<String> nameList = getNameList();
		for (String string : nameList) {
			this.makeMessagesPage(string, nameList);
		}
		return true;
	}

	private void makeMessagesPage(String string, List<String> nameList) throws Exception {
		File directory= new File(this.pathToDirectory + this.fileSeparator + "messages" + this.fileSeparator);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		File attachmentsDir = new File(this.pathToDirectory + this.fileSeparator + "messages" + this.fileSeparator + "attachments" + this.fileSeparator);
		if (!attachmentsDir.exists()) {
			attachmentsDir.mkdirs();
		}
		List<ShortMessage> messages = this.smsBoard.getEntriesBySenderName(string);
		String fileName = string.replace(" ", "").replace("'", "").replace("à", "a").toLowerCase();
		HtmlCodeCreator codeCreator = new HtmlCodeCreator(attachmentsDir);
		String page = codeCreator.createMessagesPageCode(nameList, messages);
		File newFile= new File(this.pathToDirectory + this.fileSeparator + "messages" + this.fileSeparator + fileName + ".html");
		FileOutputStream output = new FileOutputStream(newFile);
		output.write(page.getBytes());
		output.flush();
		output.close();
	}

	private boolean makeIndexFile() throws Exception {
		List<String> nameList = getNameList();
		File directory= new File(this.pathToDirectory + this.fileSeparator);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		File attachmentsDir = new File(this.pathToDirectory + this.fileSeparator + "messages" + this.fileSeparator + "attachments" + this.fileSeparator);
		if (!attachmentsDir.exists()) {
			attachmentsDir.mkdirs();
		}
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (String string : nameList) {
			dataset.addValue(this.smsBoard.getEntriesBySenderName(string).size(), "", string + " " + this.smsBoard.getEntriesBySenderName(string).size());
		}
		BarChart3D chart = new BarChart3D(dataset);
		String pathToImage = this.pathToDirectory + this.fileSeparator + "chart.png";
		chart.saveChartAsPNG(pathToImage);

		File newFile= new File(this.pathToDirectory + this.fileSeparator + "index.html");
		FileOutputStream output = new FileOutputStream(newFile);
		HtmlCodeCreator codeCreator = new HtmlCodeCreator(attachmentsDir);

		String indexCode = codeCreator.createIndexCode(nameList, this.smsBoard);
		output.write(indexCode.getBytes());

		return true;
	}

	private List<String> getNameList() {
		List<ShortMessage> list = this.smsBoard.getEntriesByNameAndByReverseDate();
		List<String> nameList = new ArrayList<String>();
		String tempName = "";
		for (ShortMessage sms : list) {
			if (!(tempName.equals(sms.getContactName()))) {
				tempName = sms.getContactName();
				nameList.add(tempName);
			}
		}
		return nameList;
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
		Format formatter = new SimpleDateFormat("yyyy-MM-dd_HHmm");
		Date data = new Date();
		this.pathToDirectory = pathToDirectory+this.fileSeparator+"jphone-html-sms_"+formatter.format(data);
	}


}
