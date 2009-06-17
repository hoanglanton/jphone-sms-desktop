package it.flaminiandrea.jphonesms.export.html;

import java.io.File;
import java.io.FileOutputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import it.flaminiandrea.jphonesms.domain.Data;
import it.flaminiandrea.jphonesms.domain.Entry;
import it.flaminiandrea.jphonesms.export.Exporter;

public class HtmlExporter implements Exporter {
	private Data data;
	private String pathToDirectory;
	private String fileSeparator = System.getProperties().getProperty("file.separator");

	@Override
	public boolean export() {
		try {
			boolean indexFile = makeIndexFile();
			boolean messagesFiles = makeMessagesFiles();
			boolean isStyleCreated = StyleExtractor.unzip("htmlstyle" + fileSeparator + "jphone-style.jps", this.pathToDirectory + fileSeparator);
			String mess = "SMS(s) exported to " + pathToDirectory;
			JOptionPane.showMessageDialog(null, mess, "Info", JOptionPane.INFORMATION_MESSAGE, null);
			return indexFile && messagesFiles && isStyleCreated;
		} catch (Exception e) {
			e.printStackTrace();
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
		File directory= new File(this.pathToDirectory + fileSeparator + "messages" + fileSeparator);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		List<Entry> entries = this.data.getEntriesBySenderName(string);
		String fileName = string.replace(" ", "").replace("'", "").replace("à", "a").toLowerCase();
		HtmlCodeCreator codeCreator = new HtmlCodeCreator();
		String page = codeCreator.createMessagesPageCode(nameList, entries);
		File newFile= new File(this.pathToDirectory + fileSeparator + "messages" + fileSeparator + fileName + ".html");
		FileOutputStream output = new FileOutputStream(newFile);
		output.write(page.getBytes());
	}

	private boolean makeIndexFile() throws Exception {
		File directory= new File(this.pathToDirectory + fileSeparator);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		File newFile= new File(this.pathToDirectory + fileSeparator + "index.html");
		FileOutputStream output = new FileOutputStream(newFile);
		HtmlCodeCreator codeCreator = new HtmlCodeCreator();
		List<String> nameList = getNameList();
		String indexCode = codeCreator.createIndexCode(nameList, data);
		output.write(indexCode.getBytes());
		return true;
	}

	private List<String> getNameList() {
		List<Entry> list = this.data.getEntriesByNameAndByReverseDate();
		List<String> nameList = new ArrayList<String>();
		String tempName = "";
		for (Entry entry : list) {
			if (!(tempName.equals(entry.getName()))) {
				tempName = entry.getName();
				nameList.add(tempName);
			}
		}
		return nameList;
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
		Format formatter = new SimpleDateFormat("yyyy-MM-dd_HHmm");
		Date data = new Date();
		this.pathToDirectory = pathToDirectory+this.fileSeparator+"jphone-html-sms_"+formatter.format(data);
	}


}
