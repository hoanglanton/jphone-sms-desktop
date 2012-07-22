package it.flaminiandrea.jphonesms.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class ShortMessage { 
	private String text;
	private String address;
	private String contactName;
	private boolean isSent;
	private Date date;
	private boolean isIMessage;
	private Vector<Attachment> attachments;

	public ShortMessage(String address, Date date, String text, boolean isSent, boolean isIMessage, String contactName, Vector<Attachment> attachments) {
		super();
		this.text = text;
		this.address = address;
		this.setSent(isSent);
		this.date = date;
		this.setIMessage(isIMessage);
		this.setAttachments(attachments);
		this.contactName = contactName;
	}
	
	public String getFlowDescription() {
		if (!isSent) {
			return "Received from:";
		} else {
			return "Sent to:";
		}
	}
	
	@Override
	public String toString() {
		String lineSeparator = System.getProperties().getProperty("line.separator");
		String direction;
		if (text == null) text = "";
		char[] charArray = text.toCharArray();
		int counter = 40;
		int tempCounter = 0;
		String formattedMessage = "";
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			formattedMessage += c;
			tempCounter++;
			if (tempCounter >= counter && c == ' ') {
				tempCounter = 0;
				formattedMessage += lineSeparator;
			}
		}
		direction = getFlowDescription() + " " + this.contactName + " (" + this.address + ")"+ lineSeparator + lineSeparator;
		return "Date: " + getFormattedDate() + lineSeparator + direction + formattedMessage + lineSeparator;
	}
	
	public String getFormattedDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return formatter.format(this.date);
	}

	public String getText() {
		return text;
	}

	public String getAddress() {
		return address;
	}

	public Date getDate() {
		return date;
	}

	public boolean isIMessage() {
		return isIMessage;
	}

	public void setIMessage(boolean isIMessage) {
		this.isIMessage = isIMessage;
	}

	public boolean isSent() {
		return isSent;
	}

	public void setSent(boolean isSent) {
		this.isSent = isSent;
	}

	public Vector<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Vector<Attachment> attachments) {
		this.attachments = attachments;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}


}
