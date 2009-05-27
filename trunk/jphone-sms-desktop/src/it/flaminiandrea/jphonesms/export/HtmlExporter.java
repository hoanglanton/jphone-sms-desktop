package it.flaminiandrea.jphonesms.export;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import it.flaminiandrea.jphonesms.domain.Data;
import it.flaminiandrea.jphonesms.domain.Entry;

public class HtmlExporter implements Exporter {
	private Data data;
	private String pathToDirectory;
	private String fileSeparator = System.getProperties().getProperty("file.separator");
	private String lineSeparator = System.getProperties().getProperty("line.separator");

	@Override
	public boolean export() {
		try {
			boolean index = makeIndexFile();
			boolean messageSummary = makeMessageSummary();
			boolean sendersAndMessages = makeSendersAndMessages();
			String mess = "SMS(s) exported to " + pathToDirectory;
			JOptionPane.showMessageDialog(null, mess, "Info", JOptionPane.INFORMATION_MESSAGE, null);
			return index && messageSummary && sendersAndMessages;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE, null);
			return false;
		}
	}

	private boolean makeSendersAndMessages() throws Exception {
		File newFile= new File(this.pathToDirectory + fileSeparator + "senders-frame.html");
		FileOutputStream output = new FileOutputStream(newFile);
		List<Entry> list = this.data.getEntriesByNameAndByReverseDate();
		List<String> nameList = new ArrayList<String>();
		String tempName = "";
		for (Entry entry : list) {
			if (!(tempName.equals(entry.getName()))) {
				tempName = entry.getName();
				nameList.add(tempName);
			}
		}
		String leftcolumn = 
			"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" + lineSeparator +
			"<html>" + lineSeparator +
			"<head>" + lineSeparator +
			"<title>Messages</title>" + lineSeparator +
			"<meta name=\"author\" content=\"Andrea Flamini\" >" + lineSeparator +
			"<meta http-equiv=\"content-type\" content=\"text/html; charset=ISO-8859-1\" >" + lineSeparator +
			"</head>" + lineSeparator +
			"<body>" + lineSeparator +
			"<h1> Senders: </h1>";
		for (String string : nameList) {
			leftcolumn += "<a href=\"messages" + fileSeparator + string.toLowerCase() + ".html\" target=\"messagesFrame\">" + 
			string + "</a><br />" + lineSeparator;
			this.makeMessagesPage(string);
		}
		leftcolumn +=
			"<br />" + lineSeparator +
			"<br />" + lineSeparator +
			"<a href=\"message-summary.html\" target=\"messagesFrame\"><b>jPhone SMS Desktop Stats</b></a>" + lineSeparator +
			"</body>" + lineSeparator +
			"</html>" + lineSeparator;
		output.write(leftcolumn.getBytes());
		return true;
	}

	private void makeMessagesPage(String string) throws Exception {
		File directory= new File(this.pathToDirectory + fileSeparator + "messages" + fileSeparator);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		List<Entry> entries = this.data.getEntriesBySenderName(string);
		String page = 
			"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" + lineSeparator +
			"<html>" + lineSeparator +
			"<head>" + lineSeparator +
			"<title>Messages</title>" + lineSeparator +
			"<meta name=\"author\" content=\"Andrea Flamini\" >" + lineSeparator +
			"<meta http-equiv=\"content-type\" content=\"text/html; charset=ISO-8859-1\" >" + lineSeparator +
			"</head>" + lineSeparator +
			"<body>" + lineSeparator;
		for (Entry entry : entries) {
			page += 
				"-------------------------------------------------<br />" +
				entry.toHtmlString() +
				"-------------------------------------------------<br /><br />";
		}
		page +=
			"</body>" + lineSeparator +
			"</html>" + lineSeparator;
		File newFile= new File(this.pathToDirectory + fileSeparator + "messages" + fileSeparator + string.toLowerCase() + ".html");
		FileOutputStream output = new FileOutputStream(newFile);
		output.write(page.getBytes());
	}

	private boolean makeMessageSummary() throws Exception {
		File newFile= new File(this.pathToDirectory + fileSeparator + "message-summary.html");
		FileOutputStream output = new FileOutputStream(newFile);
		String stats = this.getMessagesStats();
		String summary = 
			"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" + lineSeparator +
			"<html>" + lineSeparator +
			"<head>" + lineSeparator +
			"<title>Messages</title>" + lineSeparator +
			"<meta name=\"author\" content=\"Andrea Flamini\" >" + lineSeparator +
			"<meta http-equiv=\"content-type\" content=\"text/html; charset=ISO-8859-1\" >" + lineSeparator +
			"</head>" + lineSeparator +
			"<body>" + lineSeparator +
			stats +
			"</body>" + lineSeparator +
			"</html>" + lineSeparator;
		output.write(summary.getBytes());
		return true;
	}

	private String getMessagesStats() {
		String result;
		List<Entry> list = this.data.getEntriesByNameAndByReverseDate();
		List<String> nameList = new ArrayList<String>();
		String tempName = "";
		for (Entry entry : list) {
			if (!(tempName.equals(entry.getName()))) {
				tempName = entry.getName();
				nameList.add(tempName);
			}
		}
		result = 
			"<h1>jPhone SMS Desktop STATS:</h1>" + lineSeparator;
		for (String string : nameList) {
			result += 
				"Number of Messages from/sent to " + string + ": " + 
				this.data.getEntriesBySenderName(string).size() + 
				"<br />" + lineSeparator;
		}
		return result + "<br/>Total Number of Messages: " + this.data.getEntriesByNameAndByReverseDate().size() + lineSeparator;
	}

	private boolean makeIndexFile() throws Exception {
		File newFile= new File(this.pathToDirectory + fileSeparator + "index.html");
		FileOutputStream output = new FileOutputStream(newFile);
		String index = 
			"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" + lineSeparator +
			"<html>" + lineSeparator +
			"<head>" + lineSeparator +
			"<title>jPhone SMS Desktop</title>" + lineSeparator +
			"<meta name=\"author\" content=\"Andrea Flamini\" >" + lineSeparator +
			"<meta http-equiv=\"content-type\" content=\"text/html; charset=ISO-8859-1\" >" + lineSeparator +
			"</head>" + lineSeparator +
			"<FRAMESET cols=\"20%,80%\" title=\"\" onLoad=\"top.loadFrames()\">" + lineSeparator +
			"<FRAMESET rows=\"100%\" title=\"\" onLoad=\"top.loadFrames()\">" + lineSeparator +
			"<FRAME src=\"senders-frame.html\" name=\"sendersFrame\" title=\"All senders\">" + lineSeparator +
			"</FRAMESET>" + lineSeparator +
			"<FRAME src=\"message-summary.html\" name=\"messagesFrame\" title=\"All Messages\" scrolling=\"auto\">" + lineSeparator +
			"</FRAMESET>" + lineSeparator +
			"</html>" + lineSeparator;
		output.write(index.getBytes());
		return true;
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
