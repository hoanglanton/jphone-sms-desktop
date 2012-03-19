package it.flaminiandrea.jphonesms.export.html;

import it.flaminiandrea.jphonesms.domain.Attachment;
import it.flaminiandrea.jphonesms.domain.ShortMessage;
import it.flaminiandrea.jphonesms.domain.SmsBoard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Vector;

public class HtmlCodeCreator {
	private String lineSeparator = System.getProperties().getProperty("line.separator");
	private File attachmentsDir;

	public HtmlCodeCreator(File attachmentsDir) {
		this.setAttachmentsDir(attachmentsDir);
	}

	private String createHeadCode(String pathToStyle) {
		return
				"<head>\n"+lineSeparator+
				"<title>jPhone SMS Desktop</title>"+lineSeparator+
				"<meta http-equiv=\"content-type\" content=\"text/html;charset=iso-8859-1\" />"+lineSeparator+
				"<link rel=\"stylesheet\" href=\""+pathToStyle+"style.css\" type=\"text/css\" />"+lineSeparator+
				"<link rel=\"stylesheet\" type=\"text/css\" href=\""+pathToStyle+"sms-visualization-style.css\"/>"+lineSeparator+
				"</head>";
	}

	private String createDivLogoCode() {
		return 
				"<div id=\"logo\">"+lineSeparator+
				"<h1><a>RedSoft</a></h1>"+lineSeparator+
				"</div>";
	}

	private String createDivIntroCode() {
		return 
				"<div id=\"intro\">"+lineSeparator+
				"</div>";
	}

	public String createMessagesPageCode(List<String> nameList, List<ShortMessage> messages) {
		String result = 
				"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" + lineSeparator +
				"<html>" + lineSeparator +
				this.createHeadCode("../") + lineSeparator +
				"<body>" + lineSeparator +
				"<div id=\"content\">" + lineSeparator +
				this.createDivLogoCode() + lineSeparator +
				this.createDivIntroCode() + lineSeparator +
				this.createDivLeftCode(nameList,"../","") + lineSeparator +
				this.createDivRightCode(messages) + lineSeparator +
				"</div>" + lineSeparator +
				"</body>" + lineSeparator;
		return result;
	}

	private String createDivRightCode(List<ShortMessage> messages) {
		String result = 
				"<div id=\"right\">" + lineSeparator +
				"<h2>Contact: " + messages.get(0).getContactName() + "</h2>";
		for (ShortMessage sms : messages) {
			String direction ="rcvd";
			if(sms.getFlowDescription().equals("Sent to:")) {
				direction = "sent";
			}
			result +=
					"<table id=\"" + direction + "-rounded-corner\">" + lineSeparator +
					"<thead>" + lineSeparator +
					"<tr>" + lineSeparator +
					"<th class=\"rounded-top-left\"></th>" + lineSeparator +
					"<th class=\"rounded-top-center\"></th>" + lineSeparator +
					"<th class=\"rounded-top-right\"></th>" + lineSeparator +
					"</tr>" + lineSeparator +
					"<tfoot>" + lineSeparator + 
					"<tr>" + lineSeparator +
					"<td class=\"rounded-foot-left\"></td>" + lineSeparator +
					"<td class=\"rounded-foot-center\"></td>" + lineSeparator +
					"<td class=\"rounded-foot-right\"> </td>" + lineSeparator +
					"</tr>" + lineSeparator +
					"</tfoot>" + lineSeparator +
					"<tbody>" + lineSeparator +
					"<tr>" + lineSeparator +
					"<td class=\"rounded-middle-left\"></td>" + lineSeparator +
					"<td class=\"rounded-middle-center\">" + createSmsHtmlDescription(sms)+ "</td>" + lineSeparator +
					"<td class=\"rounded-middle-right\"></td>" + lineSeparator +
					"</tr>" + lineSeparator +
					"</tbody>" + lineSeparator +
					"</table>" + lineSeparator;
		}
		result +=
				"</div>" + lineSeparator;
		return result;
	}

	private String createSmsHtmlDescription(ShortMessage sms) {
		Vector<Attachment> attachments = sms.getAttachments();
		String attachmentsContent = "";

		String attachmentsHtml = "<br />";

		if (attachments != null && attachments.size() > 0) {
			int index = 1;
			for (Attachment attachment : attachments) {
				String backupFilePath = attachment.getBackupPath();
				if (backupFilePath != null && !backupFilePath.equalsIgnoreCase("") && (new File(backupFilePath).exists())) {
					if (attachment.getContent() != null && !attachment.getContent().equalsIgnoreCase("")) {
						attachmentsContent += attachment.getContent();
					}
					String mimeType = attachment.getMimeType();
					String destinationFileName = attachmentsDir.getAbsolutePath()+
							System.getProperty("file.separator")+
							attachment.retrieveBackupPathByMobilePath()+
							"."+mimeType.split("/")[1];
					copyFile(backupFilePath,destinationFileName);

					attachmentsHtml += this.lineSeparator +
							"<a href=\""+destinationFileName+"\">Attachment "+index+"</a><br />";
					index++;
				}
			}
		}

		String direction = 
				sms.getFlowDescription() + " " + sms.getContactName() + " (" + sms.getAddress() + ")"
						+ this.lineSeparator 
						+ this.lineSeparator;


		String textContent = sms.getText();

		if(!attachmentsContent.equalsIgnoreCase("")) {
			textContent = sms.getText() + " " + attachmentsContent;
		}

		String result = "Date: " + sms.getFormattedDate() + this.lineSeparator + direction + textContent
				+ this.lineSeparator + attachmentsHtml;

		return result;
	}

	private String createDivLeftCode(List<String> nameList, String pathToIndex, String pathToMessages) {
		String result = 
				"<div id=\"left\">" + lineSeparator +
				"<a href=\""+pathToIndex+"index.html\"><b>jPhone SMS Desktop Stats</b></a><br /><br />" + lineSeparator +
				"<h2>Contacts:</h2>" + lineSeparator +
				"<ul id=\"leftmenu\">";
		for (String name : nameList) {
			String fileName = name.replace(" ", "").replace("'", "").replace("à", "a").toLowerCase();
			result += 
					"<li>" + lineSeparator +
					"<a href=\"" + pathToMessages + fileName + ".html\">" + name + "</a>" + lineSeparator +
					"</li>" + lineSeparator;
		}
		result +=
				"</ul>" + lineSeparator +
				"</div>";
		return result;
	}

	public String createIndexCode(List<String> nameList, SmsBoard smsBoard) {
		String result = 
				"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" + lineSeparator +
				"<html>" + lineSeparator +
				this.createHeadCode("") + lineSeparator +
				"<body>" + lineSeparator +
				"<div id=\"content\">" + lineSeparator +
				this.createDivLogoCode() + lineSeparator +
				this.createDivIntroCode() + lineSeparator +
				this.createDivLeftCode(nameList,"","messages/") + lineSeparator +
				this.createDivRightCode(nameList, smsBoard) + lineSeparator +
				"</div>" + lineSeparator +
				"</body>" + lineSeparator;
		return result;
	}

	private String createDivRightCode(List<String> nameList, SmsBoard smsBoard) {
		List<ShortMessage> list = smsBoard.getEntriesByNameAndByReverseDate();
		String result = "<div id=\"right\">" + lineSeparator;
		result += 
				"<img src=\"chart.png\" /><br /><br />" +
						"<h1>Total Number of Messages: " + list.size() + "</h1>" + lineSeparator;
		return result + lineSeparator + "</div>";
	}

	public File getAttachmentsDir() {
		return attachmentsDir;
	}

	public void setAttachmentsDir(File attachmentsDir) {
		this.attachmentsDir = attachmentsDir;
	}

	private void copyFile(String sourceFile, String destinationFile){
		try{
			File f1 = new File(sourceFile);
			File f2 = new File(destinationFile);
			InputStream in = new FileInputStream(f1);
			OutputStream out = new FileOutputStream(f2);
			byte[] buf = new byte[20480];
			int len;
			while ((len = in.read(buf)) > 0){
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			System.out.println("File copied.");
		}
		catch(FileNotFoundException ex){
			System.out.println(ex.getMessage() + " in the specified directory.");
			System.exit(0);
		}
		catch(IOException e){
			System.out.println(e.getMessage());  
		}
	}

}
