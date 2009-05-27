package it.flaminiandrea.jphonesms.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Entry {
	private String text;
	private String name;
	private String address;
	private String flags;
	private Date date;

	public Entry(ShortMessage shortMessage, String name) {
		this.text = shortMessage.getText();
		this.name = name;
		this.address = shortMessage.getAddress();
		this.flags = flagsToString(shortMessage.getFlags());
		this.date = shortMessage.getDate();
	}

	private String flagsToString(int flags) {
		if (flags == 2) {
			return "Received from:";
		} else {
			return "Sent to:";
		}
	}
	
	public String toHtmlString() {
		String lineSeparator = "<br />" + System.getProperties().getProperty("line.separator");
		String direction;
		direction = this.flags + " " + this.name + " (" + address + ")"+ lineSeparator + lineSeparator;
		return "Date: " + getFormattedDate() + lineSeparator + direction + text + lineSeparator;
	}

	@Override
	public String toString() {
		String lineSeparator = System.getProperties().getProperty("line.separator");
		String direction;
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
		direction = this.flags + " " + this.name + " (" + address + ")"+ lineSeparator + lineSeparator;
		return "Date: " + getFormattedDate() + lineSeparator + direction + formattedMessage + lineSeparator;
	}

	public String getText() {
		return text;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getFlags() {
		return flags;
	}

	public Date getDate() {
		return date;
	}

	public String getFormattedDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return formatter.format(this.date);
	}
}
